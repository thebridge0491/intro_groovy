package org.sandbox.intro_groovy.scriptexplore

import java.nio.file.Paths

/** DocComment:
 * <p>Brief description.</p> */
final class Advanced {
	private static final def default_path_pfx = System.env.getOrDefault(
		"PATH_PFX", System.properties.getOrDefault('path_pfx', null))
	private static final def set_x = 'perl'
	
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
	
    //problem 01: interleave the lines of two files
    //<path>/maynard.pl 01 <path>/data01/arg[1 .. 2]
	static void advanced01(String[] args="data01/filea data01/fileb".split(), String path_pfx=default_path_pfx) {
		def (fh1, fh2) = xform_args(args, path_pfx).collect{p -> new File(p)}
		def (lines1, lines2) = [fh1.readLines(), fh2.readLines()]
		
		while (!lines1.isEmpty() || !lines2.isEmpty()) {
			if (!lines1.isEmpty())
				println lines1[0]
			if (!lines2.isEmpty())
				println lines2[0]
			
			lines1 = lines1.drop(1)
			lines2 = lines2.drop(1)
		}
	}
	
    //problem 02: grep for regexp in list of files fm cmd-line
    //<path>/maynard.pl 02 'ba+d' <path>/data02/arg[1 .. N]
	static void advanced02(String[] args="ba+d data02/filea data02/fileb data02/dir/filec".split(), String path_pfx=default_path_pfx) {
		def rexp = args[0]
		xform_args(args.drop(1), path_pfx).each{ p -> (new File(p)).eachLine{ line ->
			if (line =~ rexp)
				printf "%s:%s\n", p, line
		}}
	}
	
    //problem 03: concatenate all files whose names given in files fm cmd-line
    //<path>/maynard.pl 03 <path>/data03/arg[1 .. N]
	static void advanced03(String[] args="data03/files1a data03/files2a data03/files3a".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ filenm ->
			xform_args([filenm.trim()].toArray(new String[0]), path_pfx).each{ p1 -> (new File(p1)).eachLine{ line ->
				println line
			}}
		}}
	}
	
    //problem 04: locate all core files in directories fm cmd-line
    //<path>/maynard.pl 04 <path>/data04/{a,b,a/d}
	static void advanced04(String[] args="data04/a data04/b data04/a/d".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ dir ->
			//def d = proc_output(sprintf("/usr/bin/find %s -name core", dir), new File('.'))
			//d.trim().split().sort().each{ line ->
			//	line.trim()
			//	if (line =~ '/core$')
			//		printf "rm %s\n", line}
			def d = []
			(new File(dir)).eachFileRecurse (groovy.io.FileType.FILES) { f -> 
				if (f =~ '/core$')
					d.add(f)
			}
			d.sort().each{ line -> println "rm " + line}
		}
	}
	
    //problem 05: locate all core files in directories fm file fm cmd-line
    //<path>/maynard.pl 05 <path>/data05/dirs
	static void advanced05(String[] args="data05/dirsa".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ dir ->
			dir = xform_args([dir.trim()].toArray(new String[0]), path_pfx)[0]
			//def d = proc_output(sprintf("/usr/bin/find %s -name core", dir), new File('.'))
			//d.trim().split().sort().each{ line ->
			//	line.trim()
			//	if (line =~ '/core$')
			//		printf "rm %s\n", line}
			def d = []
			(new File(dir)).eachFileRecurse (groovy.io.FileType.FILES) { f -> 
				if (f =~ '/core$')
					d.add(f)
			}
			d.sort().each{ line -> println "rm " + line}
		}}
	}
	
    //problem 06: grep f/all occur's of regexp fm cmd-line in all text files
    //<path>/maynard.pl 06 'ba+d' <path>/data06
	static void advanced06(String[] args="ba+d data06".split(), String path_pfx=default_path_pfx) {
		def rexp = args[0]
		xform_args(args.drop(1), path_pfx).each{ p ->
			//def fh1 = proc_output(sprintf("/bin/ls %s", p), new File('.'))
			//fh1.trim().split().sort().each{ file0 ->
			//	file0 = p + '/' + file0.trim()
			//	if ((new File(file0)).isFile() && (proc_output(sprintf("/usr/bin/file %s", file0), new File('.')) =~ 'text')) {
			//		(new File(file0)).eachLine{ line ->
			//			line = line.trim()
			//			if (line =~ rexp)
			//				printf "%s:%s\n", file0, line}
			//		}
			//	}
			def d = []
			(new File(p)).eachFile (groovy.io.FileType.FILES) { file0 ->
				file0.eachLine{ line ->
					if (line =~ rexp)
						d.add(sprintf("%s:%s", file0.getPath(), line))
				}
			}
			d.sort().each{ line -> println line}
		}
	}
	
    //problem 07: print mv cmds to chg basename for a set of files fm cmd-line
    //<path>/maynard.pl 07 solar <path>/data07/data.*
	static void advanced07(String[] args="solar data07/data.*".split(), String path_pfx=default_path_pfx) {
		def new_base = args[0]
		xform_args(args.drop(1), path_pfx).each{ p -> 
			def filenm_list = (new FileNameFinder()).getFileNames('.', p)
			for (f in filenm_list) {
				def file0 = new File(f)
				def f_base = file0.getName()
				def f_ext = (file0.getName() =~ '[.][^.]*$')[0]
				def f_grandparent = Paths.get(file0.getParent()).toFile().getParent()
				def rel_name = Paths.get(f_grandparent).relativize(Paths.get(file0.getPath())).toString()
				def new_name = rel_name.replaceFirst(f_base, new_base + f_ext)
				printf "mv %s %s\n", rel_name, new_name
			}
		}
	}
	
    //problem 08: print cmds to list users of sendmail (possible spammers)
    //<path>/maynard.pl 08 <path>/data08
	static void advanced08(String[] args="data08".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> (new File(p)).eachLine{ line ->
			def m = line =~ /(\d+) .+ sendmail: \w+ (\S+).: user (.*)/
			if (m)
			    printf "%s   %s\n", m[0][2], m[0][1]
			def n = line =~ /(\d+) .+ sendmail: server (\S+@)?(\S+) (\S+) cmd (.*)/
			if (n)
			    printf "%s   %s\n", n[0][3], n[0][1]
		}}
	}
	
    //problem 09: print user names in each event with quotes in ISP file
    //<path>/maynard.pl 09 <path>/data09
	static void advanced09(String[] args="data09".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p ->
			def user_list = []
			(new File(p)).eachLine{ line ->
				def m = line =~ /^\s+User-Name = (.*)$/
				if (m)
					user_list.add(m[0][1])
			}
			println String.join('\n', user_list)
		}
	}
	
    //problem 10: print user names in each event without quotes in ISP file
    //<path>/maynard.pl 10 <path>/data10
	static void advanced10(String[] args="data10".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> 
			def user_list = []
			(new File(p)).eachLine{ line ->
				def m = line =~ /^\s+User-Name = "(.*)"(.*)$/
				if (m)
					user_list.add(m[0][1])
			}
			println String.join('\n', user_list)
		}
	}
	
    //problem 11: compute total user minutes in ISP file
    //<path>/maynard.pl 11 <path>/data11
	static void advanced11(String[] args="data11".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p -> 
			def time_tot = 0
			(new File(p)).eachLine{ line ->
				def m = line =~ /Acct-Session-Time = (\d+)/
				if (m)
					time_tot += m[0][1].toInteger()
			}
			printf "Total User Session Time = %d\n", time_tot
		}
	}
	
    //problem 12: print number of connections with designated connect rates
    //<path>/maynard.pl 12 <path>/data12
	static void advanced12(String[] args="data12".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p ->
			def (rate144, rate192, rate288, rate336, rate_hi) = [0, 0, 0, 0, 0]
			(new File(p)).eachLine{ line ->
				def m = line =~ /Ascend-Data-Rate = (\d+)/
				if (m) {
					def choice = m[0][1].toInteger()
					
					switch (choice) {
						case { 14400 >= it }: rate144 += 1 ; break
						case { 19200 >= it }: rate192 += 1 ; break
						case { 28800 >= it }: rate288 += 1 ; break
						case { 33600 >= it }: rate336 += 1 ; break
						default: rate_hi += 1
					}
				}
			}
			printf "0-14400\t\t%d\n", rate144
			printf "14401-19200\t\t%d\n", rate192
			printf "19201-28800\t\t%d\n", rate288
			printf "28801-33600\t\t%d\n", rate336
			printf "above 33600\t\t%d\n", rate_hi
		}
	}
	
    //problem 13: print total input and output bandwidth usage in packets
    //<path>/maynard.pl 13 <path>/data13
	static void advanced13(String[] args="data13".split(), String path_pfx=default_path_pfx) {
		xform_args(args, path_pfx).each{ p ->
			def (usage_in, usage_out) = [0, 0]
			(new File(p)).eachLine{ line ->
				def m = line =~ /Acct-Input-Packets = (\d+)/
				if (m)
					usage_in += m[0][1].toInteger()
				def n = line =~ /Acct-Output-Packets = (\d+)/
				if (n)
					usage_out += n[0][1].toInteger()
			}
			printf "Total Input Bandwidth Usage = %d packets\n", usage_in
            printf "Total Output Bandwidth Usage = %d packets\n", usage_out
		}
	}
	
    //problem 14: print ???
    //<path>/maynard.pl 14 <path>/data14
	static void advanced14(String[] args="data14".split(), String path_pfx=default_path_pfx) {
		
	}
	
    //problem 15: ???
    //<path>/maynard.pl 15 <path>/data15
	static void advanced15(String[] args="data15".split(), String path_pfx=default_path_pfx) {
		
	}
	
	static void main(String[] args) {
		def switcher = [
			'01': this.&advanced01,
			'02': this.&advanced02,
			'03': this.&advanced03,
			'04': this.&advanced04,
			'05': this.&advanced05,
			'06': this.&advanced06,
			'07': this.&advanced07,
			'08': this.&advanced08,
			'09': this.&advanced09,
			'10': this.&advanced10,
			'11': this.&advanced11,
			'12': this.&advanced12,
			'13': this.&advanced13,
			'14': this.&advanced14,
			'15': this.&advanced15
		]
		def usage_str = sprintf("  Usage: %s advanced<n> <arg1> [arg2 ..]\n\n  Available functions: %s", Advanced.class.getName(), switcher.keySet())
		
		def func = switcher.get([] != args ? args[0] : '01', { it -> 
			printf("Invalid function: %s\n%s\n", args[0], usage_str)})
		if ([] != args.drop(1)) {
			func.call(args.drop(1))
		} else {
			func.call()
		}
	}
}
