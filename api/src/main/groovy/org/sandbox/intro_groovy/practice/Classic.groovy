package org.sandbox.intro_groovy.practice

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** DocComment:
 * <p>Brief description.</p> */
final class Classic {
	private static final def pracLogger = LoggerFactory.getLogger("prac")
    
    static long fact_lp(long n) {
		long acc = 1L
		pracLogger.info("fact_lp()")
		
		for (long i = n; i > 1; --i)
			acc *= i
		acc
	}
	
	private static long fact_iter(long n, long acc) {
		n > 1 ? fact_iter(n - 1, acc * n) : acc
	}
	static long fact_i(long n) { fact_iter(n, 1L) }
	
	static float expt_lp(float b, float n) {
		float acc = 1.0f
		
		for (float i = n; 0.0f < i; --i)
			acc *= b
		acc
	}
	
	private static float expt_iter(float b, float n, float acc) {
		n > 0.0f ? expt_iter(b, n - 1.0f as float, acc * b as float) : acc
	}
	static float expt_i(float b, float n) { expt_iter(b, n, 1.0f) }
	
	static void main(String[] args) {
		def n = 5
		printf "fact(%d): %d\n", n, fact_i(n)
	}
}
