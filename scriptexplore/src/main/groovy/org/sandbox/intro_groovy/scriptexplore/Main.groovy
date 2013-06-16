package org.sandbox.intro_groovy.scriptexplore

import java.util.*

/** DocComment:
 * <p>Introduction, basic syntax/features.</p> */
class Main {
    private static void printUsage(String str, int status) {
        System.err.format("Usage: java %s [-h][-m mod]\n",
			Main.class.getName())
        System.err.println(str)
        System.exit(status)
    }

	private static void parse_cmdopts(Map<String, String> optsMap, 
            String[] args) {
        def option = null
		optsMap["rest"] = new ArrayList()
        for (int i = 0; args.length > i; ++i) {
			option = args[i]
              
            //if ('-' != option.charAt(0) || 1 == option.length())
            //    printUsage("Not an option: " + option, 1)
            switch (option.charAt(1)) {
                case 'h': printUsage("", 0)
                    break
                case 'm': 
                    if ((args.length <= i + 1) || ('-' == args[i + 1].charAt(0)))
                        printUsage("Missing argument for " + option, 1)
                    optsMap["mod"] = args[++i]
                    break
                //default: printUsage("Unknown option: " + option, 1)
                default: optsMap["rest"].add(args[i])
          	}
		}
    }

    /** Brief description.
     * @param args - array of command-line arguments */
    static void main(String[] args) {
		def chooser = [
			'simple': 'simple',
			'advanced': 'advanced'
		]
		def usage_str = sprintf("  Choose module to run.\n  Available modules: %s", chooser.keySet())
		def optsMap = new HashMap<String, String>() {
			static final long serialVersionUID = 660L
		}
		optsMap["mod"] = "simple"
        parse_cmdopts(optsMap, args)
        
        //System.env['PATH_PFX'] = 'src/main/resources/explore'
        System.properties['path_pfx'] = 'src/main/resources/explore'
        if (!chooser.get(optsMap['mod'])) {
			System.err.printf("Invalid module: %s\n%s\n", optsMap['mod'], 
				usage_str)
			System.exit(1)
		}
        def runmod = new GroovyScriptEngine('src/main/groovy/org/sandbox/intro_groovy/scriptexplore').with {
			loadScriptByName(optsMap['mod'].capitalize() + '.groovy')
		}
		if ([] != optsMap['rest'].toArray()) {
			runmod.main(*optsMap['rest'].toArray())
		} else {
			runmod.main()
		}
    }
}
