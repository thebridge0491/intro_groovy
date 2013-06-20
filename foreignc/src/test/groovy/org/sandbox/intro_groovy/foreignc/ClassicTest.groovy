package org.sandbox.intro_groovy.foreignc

import spock.lang.Specification
import java.lang.reflect.Array

import org.sandbox.intro_groovy.util.Library as Util

class ClassicTest extends Specification {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f
	
    void setupSpec() throws Exception {
    	System.err.println("###setup TestCase###")
    }
    void cleanupSpec() throws Exception {
    	System.err.println("###teardown TestCase###")
    }
    
    void setup() {
    	System.err.println("setup Test ...")
    }
    void cleanup() {
    	System.err.println("... teardown Test")
    }
	
	def "factorial spec"() {
		expect:
		// get closure from method pointer: Classnm.&methodNm
		//[Classic.&fact_i, Classic.&fact_lp].inject(true) { acc, f ->
		//	acc && (120L == f.call(5)) }
		[Classic.&fact_i, Classic.&fact_lp].every { f -> 120L == f.call(5) }
		
		//where:
		
	}
	
	def "exponent spec"() {
		expect:
		def exp = Math.pow(row[0], row[1]) as Float
		// get closure from method pointer: Classnm.&methodNm
		[Classic.&expt_i, Classic.&expt_lp].every { f ->
			Util.in_epsilon(exp, f.call(row[0], row[1]), epsilon * exp as Float) }
		
		where:
		row << Util.cartesian_prod([2.0f, 11.0f, 20.0f] as Float[],
			[3.0f, 6.0f, 10.0f] as Float[])
	}
}
