#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

class Classic {
	// env LD_LIBRARY_PATH=.:/usr/local/lib
	// or -Djava.library.path=".:/usr/local/lib"
    static {
        System.loadLibrary("${name}_stubs")
    }
    
	static long fact_i(long n) {
		return Classic_c.fact_i((int) n)
	}
	static long fact_lp(long n) {
		return Classic_c.fact_lp((int) n)
	}

	static float expt_i(float b, float n) {
		return Classic_c.expt_i(b, n)
	}
	static float expt_lp(float b, float n) {
		return Classic_c.expt_lp(b, n)
	}
	
	static void main(String[] args) {
		printf("fact(%d): %d\n", 5, fact_i(5))
	}
}

