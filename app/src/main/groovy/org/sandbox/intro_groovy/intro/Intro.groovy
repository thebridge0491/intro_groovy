package org.sandbox.intro_groovy.intro

//import java.util.Scanner
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** DocComment:
 * <p>Brief description.</p> */
final class Intro {
    private static final def rootLogger = LoggerFactory.getLogger("root")
	
	static String greeting(String rsrc_path, String greetFile, String name) {
		def buf = "HELP"
		java.io.InputStream istrm = null
		
		rootLogger.info("greeting()")
		
		try {
			istrm = new java.io.FileInputStream(new java.io.File(rsrc_path +
				"/" + greetFile))
			//istrm = this.getClass().getClassLoader().getResourceAsStream(
			//	rsrc_path + "/" + greetFile)
			//istrm = this.getClass().getResourceAsStream(
			//	rsrc_path + "/" + greetFile)
		} catch (java.io.IOException exc0) {
			printf "(exc: %s) Bad env var RSRC_PATH: %s\n", exc0, rsrc_path
			
			try {
				istrm = Intro.class.getResourceAsStream("/" + greetFile)
			} catch (Exception exc1) {
				exc0.printStackTrace()
				exc1.printStackTrace()
			}
		}
		try {
			assert null != istrm
			def fileBuffer = new java.io.BufferedReader(
				new java.io.InputStreamReader(istrm))
			buf = fileBuffer.readLine()
			istrm.close()
		} catch (Exception exc) {
			exc.printStackTrace()
		}
		buf + name
	}
	
	static char delay_char(int msecs) {
		def ch = '\0'
		Scanner keyboard = null
		
		//keyboard = new Scanner(System.in)
		if (null != System.console())
			keyboard = new Scanner(System.console().reader())
		while (true) {
			try {
				Thread.sleep(msecs)
			} catch (InterruptedException exc) {
				exc.printStackTrace()
			}
			if (null != keyboard) {
				println "Type any character when ready."
				ch = keyboard.next().charAt(0)
				
				if ('\n' == ch || '\0' == ch)
					continue
			} else
				println "Console not available."
			break
		}
		ch
	}
	
	static void main(String[] args) {
		def ch = delay_char(3000)
		printf "delay_char(%d)\n", 3000
	}
}
