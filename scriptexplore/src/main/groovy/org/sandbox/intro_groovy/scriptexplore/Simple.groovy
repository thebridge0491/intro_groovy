package org.sandbox.intro_groovy.scriptexplore

/** DocComment:
 * <p>Brief description.</p> */
final class Simple {
	private static final def default_path_pfx = System.env.getOrDefault(
		"PATH_PFX", System.properties.getOrDefault('path_pfx', null))
	private static final def set_x = 'simperl'
	
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
	
    //problem 01: print cmd-line arguments 1 per line
    //<path>/maynard.pl 01 <path>/data01/arg[1 .. N]
	static void simple01(String[] args="data01/file1 data01/file2 data01/file3 data01/file4 data01/file5".split(), String path_pfx=default_path_pfx) {
		for (arg in args)
			println arg
	}
	
    //problem 02: print all lines read with line number, space, line
    //<path>/maynard.pl 02 [<path>/data02/arg[1 .. N]]
	static void simple02(String[] args="data02/file1 data02/file2 data02/file3 data02/file4 data02/file5".split(), String path_pfx=default_path_pfx) {
		def line_num = 1
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ line ->
			printf "%d %s\n", line_num, line
			++line_num
		}}
	}
	
    //problem 03: print logins and names (gcos field) of password-format file
    //<path>/maynard.pl 03 <path>/data03/arg[1 .. N]
	static void simple03(String[] args="data03/file1 data03/file2".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ line ->
			def fields = line.split(':')
			printf "%s\t\t%s\n", fields[0], fields[4]
		}}
	}
	
    //problem 04: print logins and names (gcos field) of password-format file sorted
    //<path>/maynard.pl 04 <path>/data03/arg[1 .. N]
	static void simple04(String[] args="data03/file1 data03/file2".split(), String path_pfx=default_path_pfx) {
		def log_list = []
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ line ->
			def fields = line.split(':')
			log_list.add(sprintf("%s\t\t%s\n", fields[0], fields[4]))
		}}
		log_list.sort().each{ println it }
	}
	
    //problem 05: print all file/directory names in directory from cmd-line
    //<path>/maynard.pl 05 <path>/data05
	static void simple05(String[] args="data05".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ dir ->
			//def d = proc_output(sprintf("/bin/ls %s", dir), new File('.'))
			//d.trim().split().sort().each{ line ->
			//	printf "%s/%s\n", dir, line}
			def d = []
			(new File(dir)).eachFile (groovy.io.FileType.ANY) {
				f -> d.add(f)
			}
			d.sort().each{ line -> println line}
		}
	}
	
    //problem 06: print all regular file names in directory from cmd-line
    //<path>/maynard.pl 06 <path>/data05
	static void simple06(String[] args="data05".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ dir ->
			//def d = proc_output(sprintf("/bin/ls %s", dir), new File('.'))
			//d.trim().split().sort().each{ line ->
			//	line.trim()
			//	if ((new File(dir, line)).isFile())
			//		printf "%s/%s\n", dir, line}
			def d = []
			(new File(dir)).eachFile (groovy.io.FileType.FILES) {
				f -> d.add(f)
			}
			d.sort().each{ line -> println line}
		}
	}
	
    //problem 07: print all directory names in directory from cmd-line
    //<path>/maynard.pl 07 <path>/data05
	static void simple07(String[] args="data05".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ dir ->
			//def d = proc_output(sprintf("/bin/ls %s", dir), new File('.'))
			//d.trim().split().sort().each{ line ->
			//	line.trim()
			//	if ((new File(dir, line)).isDirectory())
			//		printf "%s/%s\n", dir, line}
			def d = []
			(new File(dir)).eachFile (groovy.io.FileType.DIRECTORIES) {
				f -> d.add(f)
			}
			d.sort().each{ line -> println line}
		}
	}
	
    //problem 08: print mv cmds to chg file extension fm "for" to "f"
    //<path>/maynard.pl 08 for f <path>/data08
	static void simple08(String[] args="for f data08".split(), String path_pfx=default_path_pfx) {
		def usage_str = sprintf("Usage: %s 08 <old> <new> <dirX>\n",
			Simple.class.getName())
		if (3 > args.length) {
			System.err.println(usage_str)
			System.exit(1)
		}
		def (ext_old, ext_new, dir1toN) = [args[0], args[1], args.drop(2)]
		xform_args(dir1toN, path_pfx).each{ dir ->
			//def d = proc_output(sprintf("/bin/ls %s", dir), new File('.'))
			//d.trim().split().sort().each{ old_file ->
			//	old_file.trim()
			//	if ((new File(dir, old_file)).isFile()) {
			//		def tmp = new String(old_file)
			//		
			//		if (tmp =~ sprintf("[.]%s\$", ext_old))
			//			printf "mv %s/%s %s/%s\n", dir, old_file, dir,
			//				tmp.replaceFirst(ext_old + "\$", ext_new)
			//	}
			//}
			def d = []
			(new File(dir)).eachFile (groovy.io.FileType.FILES) {
				f -> d.add(f)
			}
			d.sort().each{ old_file ->
				if (old_file.isFile()) {
					def tmp = new String(old_file.path)
					
					if (tmp =~ sprintf("[.]%s\$", ext_old))
						printf "mv %s %s\n", old_file,
							tmp.replaceFirst(ext_old + "\$", ext_new)
				}
			}
		}
	}
	
	static void main(String[] args) {
		def switcher = [
			'01': this.&simple01,
			'02': this.&simple02,
			'03': this.&simple03,
			'04': this.&simple04,
			'05': this.&simple05,
			'06': this.&simple06,
			'07': this.&simple07,
			'08': this.&simple08
		]
		def usage_str = sprintf("  Usage: %s simple<n> <arg1> [arg2 ..]\n\n  Available functions: %s", Simple.class.getName(), switcher.keySet())
		
		def func = switcher.get([] != args ? args[0] : '01', { it -> 
			printf("Invalid function: %s\n%s\n", args[0], usage_str)})
		if ([] != args.drop(1)) {
			func.call(args.drop(1))
		} else {
			func.call()
		}
	}
}
