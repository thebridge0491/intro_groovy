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

	private static Map<String, Object> deserializeStr(String dataStr,
			String fmt) {
		def blankCfg = ["fmt": fmt]

		if (0 != dataStr.length()) {
			if (fmt in ['yaml', 'json']) {
				def yaml = new org.yaml.snakeyaml.Yaml()
				blankCfg << yaml.load(dataStr)
			} else if ('toml' == fmt) {
				def toml = new com.moandjiezana.toml.Toml()
				blankCfg << toml.read(dataStr).toMap()
			} /*else if ('json' == fmt) {
				def (jsonobj, rdr) = [null, null]
				rdr = javax.json.Json.createReader(new java.io.StringReader(
                    dataStr))
                jsonobj = rdr.readObject()

                def jsonmap = [:]
                for (entryX in jsonobj.entrySet()) {
                	if (jsonobj.getClass() != entryX.getValue().getClass())
                		jsonmap.put(entryX.getKey(), entryX.getValue())
                	else {
                		def jsonsub = entryX.getValue()
                		def jsonsubmap = [:]
                		for (entrySub in jsonsub.entrySet()) {
                			jsonsubmap.put(entrySub.getKey(),
                				entrySub.getValue())
                		}
                		jsonmap.put(entryX.getKey(), jsonsubmap)
                	}
                }
                rdr.close()
                blankCfg << jsonmap

				//def rdr = new groovy.json.JsonSlurper()
				// //blankCfg = rdr.parseText(dataStr)
				//blankCfg << rdr.parseText(dataStr)
			}*/
		}
		return blankCfg
	}

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

        def ini_cfg = new org.ini4j.Ini()
        try {
			ini_cfg.load (new java.io.FileInputStream(rsrc_path + "/prac.conf"))
		} catch (java.io.IOException exc0) {
			printf "(exc: %s) Bad env var RSRC_PATH: %s\n", exc0, rsrc_path
			try {
				ini_cfg.load (Main.class.getResourceAsStream "/prac.conf")
			} catch (java.io.IOException exc1) {
				exc0.printStackTrace()
                exc1.printStackTrace()
                System.exit 1
			}
		}


        def (json_cfg, yaml_cfg, toml_cfg) = [null, null, null]
        try {
			json_cfg = deserializeStr(new String(new java.io.FileInputStream(
                rsrc_path + "/prac.json").readAllBytes()), "json")
            toml_cfg = deserializeStr(new String(new java.io.FileInputStream(
                rsrc_path + "/prac.toml").readAllBytes()), "toml")
            yaml_cfg = deserializeStr(new String(new java.io.FileInputStream(
                rsrc_path + "/prac.yaml").readAllBytes()), "yaml")
		} catch (java.io.IOException exc0) {
			printf "(exc: %s) Bad env var RSRC_PATH: %s\n", exc0, rsrc_path
			try {
				json_cfg = deserializeStr(new String(
					Main.class.getResourceAsStream("/prac.json"
					).readAllBytes()), "json")
				toml_cfg = deserializeStr(new String(
					Main.class.getResourceAsStream("/prac.toml"
					).readAllBytes()), "toml")
				yaml_cfg = deserializeStr(new String(
					Main.class.getResourceAsStream("/prac.yaml"
					).readAllBytes()), "yaml")
			} catch (java.io.IOException exc1) {
				exc0.printStackTrace()
                exc1.printStackTrace()
                //System.exit 1
			}
		}

    	def tup_arr = [
			[ini_cfg, ini_cfg["default"]["domain"], ini_cfg["user1"]["name"]],
			[json_cfg, json_cfg["domain"], json_cfg["user1"]["name"]],
			[toml_cfg, toml_cfg["domain"], toml_cfg["user1"]["name"]],
			[yaml_cfg, yaml_cfg["domain"], yaml_cfg["user1"]["name"]]
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
