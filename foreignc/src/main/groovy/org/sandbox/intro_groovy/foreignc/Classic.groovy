package org.sandbox.intro_groovy.foreignc

class Classic {
	static {
        //for (pathX in System.props.getOrDefault("java.library.path", 
        // 		".:/usr/local/lib").split(":"))
		//	com.sun.jna.NativeLibrary.addSearchPath("intro_c-practice", pathX)
		System.props["jna.library.path"] = 
			System.props.getOrDefault("jna.library.path", 
			System.props.getOrDefault("java.library.path", ".:/usr/local/lib"))
    }
    
	private final static def Classic = IClassic_c.Classic
    
	static long fact_i(long n) {
		return Classic.fact_i(n)
	}
	static long fact_lp(long n) {
		return Classic.fact_lp(n)
	}

	static float expt_i(float b, float n) {
		return Classic.expt_i(b, n)
	}
	static float expt_lp(float b, float n) {
		return Classic.expt_lp(b, n)
	}
	
	static void main(String[] args) {
		printf("fact(%d): %d\n", 5, fact_i(5))
	}
}
