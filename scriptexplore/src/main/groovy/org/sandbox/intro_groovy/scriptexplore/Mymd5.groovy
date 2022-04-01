package org.sandbox.intro_groovy.scriptexplore

import java.security.MessageDigest

/** DocComment:
 * <p>Brief description.</p> */
final class Mymd5 {
	private static final def default_path_pfx = System.env.getOrDefault(
		"PATH_PFX", System.properties.getOrDefault('path_pfx', null))
	private static final def set_x = 'problems_perl'
	private static final def script_file = new File(
		Mymd5.class.protectionDomain.codeSource.location.path)
	
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
	
	//Compute hash digest (MD5 or SHA1) of file
	static String _compute_digest(filenm=script_file.name, boolean is_sha1=false) {
		def hash_algo = is_sha1 ? MessageDigest.getInstance("SHA1") :
			MessageDigest.getInstance("MD5")
		(new File(filenm)).eachByte(4096) { buf, num ->
			hash_algo.update(buf, 0, num)
		}
		//(new BigInteger(1, hash_algo.digest())).toString(16).padLeft(
		//	hash_algo.getDigestLength(), '0')
		//String.join("", hash_algo.digest().collect{ b ->
		//	String.format("%02x", b)})
		hash_algo.digest().encodeHex().toString()
	}
	
	//Print hash digest to file
	static void print_digest(String rootpath, String digestpath=null, boolean is_sha1=false, String path_pfx=default_path_pfx) {
		def outfile = null == digestpath ? System.out : 
			new PrintStream(new FileOutputStream(digestpath), true)
		def writer_out = new BufferedWriter(new OutputStreamWriter(outfile))
		xform_args([rootpath].toArray(new String[0]), path_pfx).each{ p -> (new File(p)).eachLine{ dir ->
			dir = xform_args([dir.trim()].toArray(new String[0]), path_pfx)[0]
			//def dir_list = proc_output(sprintf("/usr/bin/find %s -print", dir),
			//	new File('.')).trim().split()
			//def file_list = dir_list.findAll{ e -> (new File(e)).isFile() }
			def file_list = []
			(new File(dir)).eachFileRecurse (groovy.io.FileType.FILES) {
				f -> file_list.add(f.getPath())
			}
			
			file_list.sort().each{ file0 ->
				def fgr_prt = _compute_digest(file0, is_sha1)
				
				if (null == fgr_prt)
					System.err.printf("Error on md5sum for file %s\n", file0)
				writer_out.write(sprintf("%s\t%s\n", fgr_prt, file0))
			}
		}}
		writer_out.close() //writer_out.flush()
	}
	
	//Update two dictionaries of files -> digest
	static void _update_lines_dict(Map<String, String> dict1,
			Map<String, String> dict2, String digestpath) {
		(new File(digestpath)).eachLine{ line ->
			def matches = line =~ /^([^ \t]+)[ \t]*([^ \t]+)$/
			
			if (0 != matches.getCount()) {
				//def (digest, file_name) = matches[0][1..-1]
				def (_, digest, file_name) = matches[0]
				
				if (!dict2.containsKey(file_name))
					dict2[file_name] = ''
				dict1[file_name] = digest
			}
		}
	}
	
	//Verifies hash digest of file(s)
	static void verify_digest(String rootpath, String digestpath, boolean is_sha1=false) {
		def cur_file = java.nio.file.Files.createTempFile(null, ".tmp")
		def (cur_lines, digest_lines) = [[:], [:]]
		
		print_digest(rootpath, cur_file.toString(), is_sha1)
		_update_lines_dict(cur_lines, digest_lines, cur_file.toString())
		_update_lines_dict(digest_lines, cur_lines, digestpath)
		
		//for (tester in digest_lines.sort().keySet()) {
		digest_lines.sort().keySet().each{ tester ->
			if (!(digest_lines[tester] && cur_lines[tester])) {
				if (digest_lines[tester])
					printf "<old>%s  %s\n", tester, digest_lines[tester]
				if (cur_lines[tester])
					printf "<new>%s  %s\n", tester, cur_lines[tester]
			} else if (digest_lines[tester] != cur_lines[tester]) {
				printf "<old>%s  %s\n", tester, digest_lines[tester]
				printf "<new>%s  %s\n", tester, cur_lines[tester]
			}
		}
		cur_file.toFile().delete()
	}
	
	static void main(String[] args) {
		def paths = args.findAll{ e -> '-c' != e && '-s' != e}
		
		if (args.any{ e -> '-c' == e})
			// Verifies the hash digest (MD5 or SHA1) of all regular files under
			// any directories of command-line arg rootfile against the
			// command-line arg digestfile and indicates the status of changed,
			// added or deleted files in pairs
			//
			// Uses the following symbols to indicate status:
			//	<old>{file} {digest}        # for changed
			//	<new>{file} {digest}
			//
			//	<new>{file} {digest}        # for added
			//
			//	<old>{file} {digest}        # for deleted
			// demo: $ script [-s] -c <path>/rootfile.txt <path>/digestfile.txt
			verify_digest(paths.isEmpty() ? "data_md5/rootfile.txt" : paths[0],
				2 > paths.size() ? null : paths[1], args.any{ e -> '-s' == e})
		else
			// Print the hash digest (MD5 or SHA1) of all regular files under any
			// directories of command-line arg file
			// demo: $ script [-s] <path>/rootfile.txt [<path>/digestfile.txt]
			print_digest(paths.isEmpty() ? "data_md5/rootfile.txt" : paths[0],
				2 > paths.size() ? null : paths[1], args.any{ e -> '-s' == e})
	}
}
