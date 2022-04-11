#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*
import java.lang.reflect.Array

class ClassicTest {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f

    ClassicTest() {
    }

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

	@org.junit.jupiter.api.BeforeAll
    static void setUpClass() throws Exception {
    	System.err.println("${symbol_pound}${symbol_pound}${symbol_pound}setup TestCase${symbol_pound}${symbol_pound}${symbol_pound}")
    }
    @org.junit.jupiter.api.AfterAll
    static void tearDownClass() throws Exception {
    	System.err.println("${symbol_pound}${symbol_pound}${symbol_pound}teardown TestCase${symbol_pound}${symbol_pound}${symbol_pound}")
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    	System.err.println("setup Test ...")
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    	System.err.println("... teardown Test")
    }

    @Test
    void test_fact() {
        // get closure from method pointer: Classnm.&methodNm
        for (f in [Classic.&fact_i, Classic.&fact_lp])
        	assertEquals(120L, f.call(5))
    }
    @Test
    void test_expt() {
        Float[] param1 = [2.0f, 11.0f, 20.0f], param2 = [3.0f, 6.0f, 10.0f]

		for (row in cartesian_prod(param1, param2)) {
			def exp = Math.pow(row[0], row[1])
			// get closure from method pointer: Classnm.&methodNm
			for (f in [Classic.&expt_i, Classic.&expt_lp])
        		assertEquals(exp, f.call(row[0], row[1]), exp * epsilon)
        }
    }
}
