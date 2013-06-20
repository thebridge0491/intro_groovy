package org.sandbox.intro_groovy.intro

import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.sandbox.intro_groovy.util.Library as Util
import org.sandbox.intro_groovy.practice.*
import org.sandbox.intro_groovy.practice.Sequenceops as Seqops


enum ConstItems {
	ZERO(0), NUMZ(26)
	private int numVal
	
	ConstItems(int numVal) { this.numVal = numVal }
	
	int getNumVal() { this.numVal }
}

/** DocComment:
 * <p>Introduction, basic syntax/features.</p> */
class Main {
	static {
		// from cmdln: java -Dlog4j.configurationFile=path/log4j2.xml ...
		//System.props["log4j.configurationFile"] = "log4j2.xml"
		// from cmdln: java -Dlogback.configurationFile=path/logback.xml ...
		System.props["logback.configurationFile"] = "logback.xml"
	}
	private static final def rootLogger = LoggerFactory.getLogger(
		Main.class.getName())
	
	private static void run_intro(String progname, String rsrc_path,
			Map<String, String> opts) {
	    def timeIn_mSecs = System.currentTimeMillis()
	    
	    // basic datatypes
	    def isDone = false
	    def numI = 0, arrLen = ConstItems.ZERO.getNumVal()
	    def num = opts["num"].toInteger()
	    final def seedi = (int)timeIn_mSecs, delayMSecs = (int)2.5e3
	    def timeDiff = 0.0f
	    def ch = '\0'
	    
	    // strings & arrays
	    final def noname = "World", greetFile = "greet.txt"
	    def greetStr, buf = "HELP"
	    def str1 = new char[64]
	    int[] numArr = [0b1001, 011, 0x9, 9]
	    
	    // composites
	    def rnd = new java.util.Random(seedi)
	    num = (0 == num) ? rnd.nextInt(18) + 2 : num
	    def user1 = new User() // new User(opts["name"], num)
	    user1.name = opts["name"]
	    user1.num = num
	    user1.timeIn = timeIn_mSecs
	    
	    arrLen = numArr.length
	    
	    for (ival in numArr)
			numI += ival
		assert (arrLen * numArr[0]) == numI
		
		ch = Intro.delay_char(delayMSecs)
		
		while (!isDone) {
			short sh = -1
			long sl = 1L
			double d1 = 100.0, d2 = 1.0e6
			if (true)
				isDone = true
		}
	    
	    def re = java.util.regex.Pattern.compile(
        //	"quit", java.util.regex.Pattern.CASE_INSENSITIVE)
        	"(?i)quit")
        def m = re.matcher(opts["name"])
        printf "%s match: %s to %s\n", m.matches() ? "Good" : "Does not",
			opts["name"], re.pattern()
		
		def dt1 = new java.util.Date(user1.timeIn)
		greetStr = Intro.greeting(rsrc_path, greetFile, user1.name)
		printf "%s\n%s!\n", dt1, greetStr
		
		timeDiff = (System.currentTimeMillis() - user1.timeIn) / 1000.0f
		printf "(program %s) Took %.1f seconds.\n", progname, timeDiff
		println sprintf("-" * 40)
		
		Integer[] ints = [2, 1, 0, 4, 3]
		def lst = Arrays.asList(ints) as ArrayList<Integer>
		
		if (opts["is_expt2"].toBoolean()) {
			printf "expt(2.0, %.1f) = %.1f\n", user1.num.toFloat(),
				Classic.expt_i(2.0f, user1.num.toFloat())
			
			def res0 = Util.mkString(lst)
			printf "reverse(%s): ", res0
			def tmp = lst.collect{ e -> e}
			Seqops.reverse_lp(tmp)
			println Util.mkString(tmp)
			
			printf "Arrays.sort(%s): ", res0
			Arrays.sort(ints)
			println Util.mkString(Arrays.asList(ints))
		} else {
			printf "fact(%d) = %d\n", user1.num, Classic.fact_i(user1.num)
			
			def res0 = Util.mkString(lst)
			def el = 3
			def idx = Seqops.indexOf_lp(el, lst, Util.intCmp)
			printf "indexOf(%d, %s, intCmp): %d\n", el, res0, idx
			
			def new_val = 50
			printf "%s.add(%d): ", res0, new_val
			lst.add(new_val)
			println Util.mkString(lst)
		}
		
		println sprintf("-" * 40)
		def pers = new Person("I.M. Computer", 32)
		assert pers instanceof Object
		try {
			assert pers.getClass() == Class.forName(
				"org.sandbox.intro_groovy.intro.Person")
		} catch (ClassNotFoundException exc) {
			exc.printStackTrace()
		}
		println pers
		pers.age = 33
		printf "pers.age = %d: %s\n", 33, pers
	}
	
    private static void printUsage(String str, int status) {
        System.err.format("Usage: java %s [-h][-u name][-n num][-2]\n",
			Main.class.getName())
        System.err.println(str)
        System.exit(status)
    }

	private static void parse_cmdopts(Map<String, String> optsMap, 
            String[] args) {
        def option = null
		rootLogger.info("parse_cmdopts()")
        for (int i = 0; args.length > i; ++i) {
			option = args[i]
              
            if ('-' != option.charAt(0) || 1 == option.length())
                printUsage("Not an option: " + option, 1)
            switch (option.charAt(1)) {
                case 'h': printUsage("", 0)
                    break
                case 'u': 
                    if ((args.length <= i + 1) || ('-' == args[i + 1].charAt(0)))
                        printUsage("Missing argument for " + option, 1)
                    optsMap["name"] = args[++i]
                    break
                case 'n': 
                    if ((args.length <= i + 1) || ('-' == args[i + 1].charAt(0)))
                        printUsage("Missing argument for " + option, 1)
                    optsMap["num"] = args[++i]
                    break
                case '2': 
                    optsMap["is_expt2"] = "1"
                    break
                default: printUsage("Unknown option: " + option, 1)
          	}
		}
    }

    /** Brief description.
     * @param args - array of command-line arguments */
    static void main(String[] args) {
		def optsMap = new HashMap<String, String>() {
			static final long serialVersionUID = 660L
		}
		optsMap["name"] = "World"
		optsMap["num"] = "0"
		optsMap["is_expt2"] = ""
        parse_cmdopts(optsMap, args)
	    
	    def rsrc_path = System.env.getOrDefault("RSRC_PATH",
			System.props.getOrDefault("rsrcPath", "src/main/resources"))
        
        def (iniStrm, jsonStrm, yamlStrm) = [null, null, null]
        try {
			iniStrm = new java.io.FileInputStream(rsrc_path + "/prac.conf")
			//jsonStrm = new java.io.FileInputStream(rsrc_path + "/prac.json")
			//yamlStrm = new java.io.FileInputStream(rsrc_path + "/prac.yaml")
		} catch (java.io.IOException exc0) {
			printf "(exc: %s) Bad env var RSRC_PATH: %s\n", exc0, rsrc_path
			
			iniStrm = Main.class.getResourceAsStream "/prac.conf"
			//jsonStrm = Main.class.getResourceAsStream "/prac.json"
			//yamlStrm = Main.class.getResourceAsStream "/prac.yaml"
		}
        
        def ini_cfg = new org.ini4j.Ini()
        
        //def (jsonobj, rdr) = [null,null]
        try {
        	ini_cfg.load iniStrm
        	//rdr = new groovy.json.JsonSlurper()
        	//jsonobj = rdr.parse(new java.io.InputStreamReader(jsonStrm))
        	//rdr = javax.json.Json.createReader jsonStrm
			//jsonobj = rdr.readObject()
        } catch (java.io.IOException exc) {
            exc.printStackTrace()
            System.exit 1
        } /*finally {
			rdr.close()
		}*/
		
		//def yaml = new org.yaml.snakeyaml.Yaml()
		//def yamlmap = yaml.load yamlStrm
        
    	def tup_arr = [
			[ini_cfg, ini_cfg["default"]["domain"], ini_cfg["user1"]["name"]]//,
			//[jsonobj, jsonobj["domain"], jsonobj["user1"]["name"]],
			//[yamlmap, yamlmap["domain"], yamlmap["user1"]["name"]]
		]
		
        //printf "ini4j config start section: %s\n",
    	//	ini_cfg.keySet().iterator().next()
    	for (row in tup_arr) {
			printf "config: %s\n", row[0]
			printf "domain: %s\n", row[1]
			printf "user1Name: %s\n\n", row[2]
		}
    	
    	run_intro(Main.class.getName(), rsrc_path, optsMap)
	    
	    rootLogger.debug("exiting main()")
    }
}
