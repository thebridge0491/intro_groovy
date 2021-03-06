#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import spock.lang.Specification
import java.lang.reflect.Array

class ClassicTest extends Specification {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f
	
    Boolean in_epsilon(Float a, Float b, Float tolerance = 0.001f) {
		def delta = Math.abs(tolerance)
		//return (a - delta) <= b && (a + delta) >= b
		return !((a + delta) < b) && !((b + delta) < a)
	}
	
	public <T> T[][] cartesian_prod(T[] arr1, T[] arr2) {
		//def arr_prod = [arr1, arr2].combinations().findAll { true }
		def arr_prod = arr1.collectMany {x -> arr2.collect {y -> 
			[x, y].findAll { true }}}
		
		return arr_prod
	}
	
    void setupSpec() throws Exception {
    	System.err.println("${symbol_pound}${symbol_pound}${symbol_pound}setup TestCase${symbol_pound}${symbol_pound}${symbol_pound}")
    }
    void cleanupSpec() throws Exception {
    	System.err.println("${symbol_pound}${symbol_pound}${symbol_pound}teardown TestCase${symbol_pound}${symbol_pound}${symbol_pound}")
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
			in_epsilon(exp, f.call(row[0], row[1]), epsilon * exp as Float) }
		
		where:
		row << cartesian_prod([2.0f, 11.0f, 20.0f] as Float[],
			[3.0f, 6.0f, 10.0f] as Float[])
	}
}
