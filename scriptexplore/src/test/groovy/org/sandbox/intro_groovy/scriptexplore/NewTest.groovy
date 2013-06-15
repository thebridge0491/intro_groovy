package org.sandbox.intro_groovy.scriptexplore

import spock.lang.Specification

class NewTest extends Specification {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f
	
    Boolean in_epsilon(Float a, Float b, Float tolerance = 0.001f) {
		def delta = Math.abs(tolerance)
		//return (a - delta) <= b && (a + delta) >= b
		return !((a + delta) < b) && !((b + delta) < a)
	}
	
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
	
	def "classExists spec"() {
		expect:
		Class.forName(sprintf("%s.Library",
			this.getClass().getPackage().getName()))
	}
	
	def "method spec"() {
		given:
		
		when:
		
		then:
		
		expect:
		valA == valB
		
		cleanup:
		
		where:
		valA  |  valB
		4     |  2 * 2
		5     |  2.0 * 2.5
	}
	
	def "dblMethod spec"() {
		expect:
		100.001f == 100.001f
		in_epsilon(100.001f, 100.001f, epsilon)
	}
	
	def "strMethod spec"() {
		expect:
		"Hello" == "Hello"
	}
	
	@spock.lang.Timeout(1000)
	def "timeoutMethod spec"() {
		expect:
		for (int i = 0; 1.0e6f > i; ++i);
	}
	
	@spock.lang.Ignore
	def "ignoreMethod spec"() {
		expect:
		5 == 2 * 2
	}
	
	def "failMethod spec"() {
		expect:
		false
	}
	
	def "exceptionMethod spec"() {
		when:
		throw new IllegalArgumentException()
		
		then:
		thrown(IllegalArgumentException)
	}
}
