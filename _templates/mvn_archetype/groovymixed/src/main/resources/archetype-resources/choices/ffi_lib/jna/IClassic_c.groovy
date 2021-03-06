#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import com.sun.jna.Library
import com.sun.jna.Native

/** DocComment:
 * <p>Brief description.</p> */
interface IClassic_c extends com.sun.jna.Library {
	// env LD_LIBRARY_PATH=.:/usr/local/lib
    // or
	/* // -D[java | jna].library.path=".:/usr/local/lib"
	static { // inside class file
        //for (pathX in System.props.getOrDefault("java.library.path", 
        // 		".:/usr/local/lib").split(":"))
		//	com.sun.jna.NativeLibrary.addSearchPath("intro_c-practice", pathX)
		System.props["jna.library.path"] = 
			System.props.getOrDefault("jna.library.path", 
			System.props.getOrDefault("java.library.path", ".:/usr/local/lib"))
    }*/
    final static def Classic = (IClassic_c) Native.loadLibrary(
  		"intro_c-practice", IClassic_c.class)
    
    long fact_i(long n)
    long fact_lp(long n)
    
    float expt_i(float b, float n)
    float expt_lp(float b, float n)
}
