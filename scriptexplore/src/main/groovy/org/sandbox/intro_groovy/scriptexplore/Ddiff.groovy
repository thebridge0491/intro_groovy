package org.sandbox.intro_groovy.scriptexplore

import java.nio.file.Paths
import java.nio.file.Files

/** DocComment:
 * <p>Brief description.</p> */
final class Ddiff {
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
	
	//Create dictionaries of all files(directories 1 & 2) for two directories
	static Map<String, String>[] _create_files_dicts(String dir1, String dir2) {
		def (files_dict_a, files_a) = [[:], _create_files_list([dir1].toArray(new String[0]))]
		def (files_dict_b, files_b) = [[:], _create_files_list([dir2].toArray(new String[0]))]
		
		files_a.each{ p -> 
			def file0 = new File(p)
			if (file0.isFile()) {
				files_dict_a[file0.getName()] = file0.getPath()
				files_dict_b[file0.getName()] = null
			}
		}
		files_b.each{ p -> 
			def file0 = new File(p)
			if (file0.isFile()) {
				files_dict_b[file0.getName()] = file0.getPath()
				if (!files_dict_a[file0.getName()])
					files_dict_a[file0.getName()] = null
			}
		}
		[files_dict_a, files_dict_b]
	}
	
	//Create diff list
	static List<String> create_diff_list(String[] dirs, Map<String, Boolean> opts_hash, String path_pfx=default_path_pfx) {
		def diff_list = []
		def (files_dict_a, files_dict_b) = _create_files_dicts(*xform_args(
			dirs, path_pfx))
		
		//for (tester in files_dict_a.sort().keySet()) {
		files_dict_a.sort().keySet().each{ tester ->
			def test_a = files_dict_a.get(tester, null)
			def test_b = files_dict_b.get(tester, null)
			
			def coexist = files_dict_a[tester] && files_dict_b[tester]
			
			if (opts_hash['opt_dir1'] && !files_dict_b[tester])
				diff_list.add(sprintf("<<< %s", tester))
			if (opts_hash['opt_dir2'] && !files_dict_a[tester])
				diff_list.add(sprintf(">>> %s", tester))
			
			if (coexist) {
				//def is_diff = "" == proc_output(sprintf("diff -q %s %s", 
				//	test_a, test_b), new File('.')) ? false : true
				def hash_algo = java.security.MessageDigest.getInstance("MD5")
				def digest_a = hash_algo.digest(Files.readAllBytes(Paths.get(test_a))).encodeHex().toString()
				def digest_b = hash_algo.digest(Files.readAllBytes(Paths.get(test_b))).encodeHex().toString()
				def is_diff = digest_a != digest_b
				if (opts_hash['opt_differ'] && is_diff)
					diff_list.add(sprintf("< %s >", tester))
				if (opts_hash['opt_same'] && !is_diff)
					diff_list.add(sprintf("> %s <", tester))
			}
		}
		diff_list
	}
	
	static void main(String[] args) {
		def paths = args.findAll{ e -> !(e =~ /^-.*/)}.toArray(new String[0])
		def opts = args.findAll{ e -> e =~ /^-.*/}
		def opts_hash = 0 == opts.size() ? ['opt_differ': true,
			'opt_same': true, 'opt_dir1': true, 'opt_dir2': true] :
			['opt_differ': opts.any{ e -> e =~ /d/},
			'opt_same': opts.any{ e -> e =~ /s/},
			'opt_dir1': opts.any{ e -> e =~ /1/},
			'opt_dir2': opts.any{ e -> e =~ /2/}]
		
		// Performs diff on similar named files in two cmdline arg directories and
		// indicates status if file names and/or contents do/don't match.
		//
		// Uses the following symbols around file names to indicate status:
		// unmatched file in dir1      : <<< file1
		//	unmatched file in dir2      : >>> file2
		//	similar name but different  : < file >
		//	similar name and same       : > file <
		// demo: $ script [-ds12] <path>/dataA <path>/dataB
		for (line in create_diff_list(1 > paths.size() ? ["data_diff/dataA",
				"data_diff/dataB"].toArray(new String[0]) : paths, opts_hash))
			println line
	}
}
