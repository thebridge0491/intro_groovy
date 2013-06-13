#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
	
	private static void run_${name}(String progname, String name) {
	    def re = java.util.regex.Pattern.compile(
        //	"quit", java.util.regex.Pattern.CASE_INSENSITIVE)
        	"(?i)quit")
        def m = re.matcher(name)
        printf "%s match: %s to %s\n", m.matches() ? "Good" : "Does not",
			name, re.pattern()
	}
	
    private static void printUsage(String str, int status) {
        System.err.format("Usage: java %s [-h][-u name]\n",
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
        parse_cmdopts(optsMap, args)
	    
	    def rsrc_path = System.env.getOrDefault("RSRC_PATH",
			System.props.getOrDefault("rsrcPath", "src/main/resources"))
        
        def (iniStrm, jsonStrm, yamlStrm) = [null, null, null]
        try {
			iniStrm = new java.io.FileInputStream(rsrc_path + "/prac.conf")
			jsonStrm = new java.io.FileInputStream(rsrc_path + "/prac.json")
			yamlStrm = new java.io.FileInputStream(rsrc_path + "/prac.yaml")
		} catch (java.io.IOException exc0) {
			printf "(exc: %s) Bad env var RSRC_PATH: %s\n", exc0, rsrc_path
			
			iniStrm = Main.class.getResourceAsStream "/prac.conf"
			jsonStrm = Main.class.getResourceAsStream "/prac.json"
			yamlStrm = Main.class.getResourceAsStream "/prac.yaml"
		}
        
        def ini_cfg = new org.ini4j.Ini()
        
        def (jsonobj, rdr) = [null,null]
        try {
        	ini_cfg.load iniStrm
        	//rdr = new groovy.json.JsonSlurper()
        	//jsonobj = rdr.parse(new java.io.InputStreamReader(jsonStrm))
        	rdr = javax.json.Json.createReader jsonStrm
			jsonobj = rdr.readObject()
        } catch (java.io.IOException exc) {
            exc.printStackTrace()
            System.exit 1
        } finally {
			rdr.close()
		}
		
		def yaml = new org.yaml.snakeyaml.Yaml()
		def yamlmap = yaml.load yamlStrm
        
    	def tup_arr = [
			[ini_cfg, ini_cfg["default"]["domain"], ini_cfg["user1"]["name"]],
			[jsonobj, jsonobj["domain"], jsonobj["user1"]["name"]],
			[yamlmap, yamlmap["domain"], yamlmap["user1"]["name"]]
		]
		
        //printf "ini4j config start section: %s\n",
    	//	ini_cfg.keySet().iterator().next()
    	for (row in tup_arr) {
			printf "config: %s\n", row[0]
			printf "domain: %s\n", row[1]
			printf "user1Name: %s\n\n", row[2]
		}
    	
    	run_${name}(Main.class.getName(), optsMap["name"])
	    
	    rootLogger.debug("exiting main()")
    }
}
