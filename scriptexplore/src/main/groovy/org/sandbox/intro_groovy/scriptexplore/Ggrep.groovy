package org.sandbox.intro_groovy.scriptexplore

/** DocComment:
 * <p>Brief description.</p> */
final class Ggrep {
	private static final def default_path_pfx = System.env.getOrDefault(
		"PATH_PFX", System.properties.getOrDefault('path_pfx', null))
	private static final def set_x = 'problems_perl'
	
	static String[] xform_args(String[] args, String path_pfx) {
		args.collect{f -> null == path_pfx ? f : sprintf("%s/%s/%s", path_pfx,
			set_x, f)}
	}
	
    static def proc_output(String cmd, File dir) {
		def errbuf = new StringBuffer()
		def proc = cmd.execute(null, dir)
		proc.consumeProcessErrorStream(errbuf) ; println errbuf.toString()
		proc.text
    }

	static def run_cmd(String cmd, File dir) {
		def proc = cmd.execute(null, dir)
		proc.waitForProcessOutput((Appendable)System.out, System.err)
		if (0 != proc.exitValue()) {
			throw new Exception("Command '${cmd}' exited with code: ${proc.exitValue()}")
		}
	}
		
	//Create files list
	static List<String> _create_files_list(String[] paths) {
		def file_list = []
		paths.each{ p -> 
			def path0 = new File(p)
			if (path0.isDirectory()) {
				path0.eachFile (groovy.io.FileType.FILES) { f -> 
					file_list.add(f.getPath())
				}
			} else if (path0.isFile()) {
				file_list.add(path0.getPath())
			}
		}
		file_list
	}
	
	//Create grep pattern match list
	static List<String> create_grep_match_list(String rexp, String[] paths, boolean is_file_only=false, String path_pfx=default_path_pfx) {
		def match_list = []
		
		_create_files_list(xform_args(paths, path_pfx)).each{ file0 ->
			def not_done = true
			//def f_type = proc_output(sprintf("/usr/bin/file %s", file0), 
			//	new File('.')).trim()
			(new File(file0)).eachLine{ line ->
				if (not_done && line =~ rexp) {
					match_list.add(file0 + (!is_file_only ? ":" + line : ""))
					if (is_file_only)
						not_done = false
				}
			}
		}
		match_list
	}
	
	static void main(String[] args) {
		def params = args.findAll{ e -> '-l' != e}.toArray(new String[0])
		
		// Greps for regular expression in all regular files in cmdline arg 
		// file/directory list as well as files under given directories
		// demo: $ script [-l] 'ba+d' <path>/filea <path>/fileb <path>/dir <path>/data6
		for (line in create_grep_match_list(params[0], params.drop(1),
				args.any{ e -> '-l' == e}))
			println line
	}
}
