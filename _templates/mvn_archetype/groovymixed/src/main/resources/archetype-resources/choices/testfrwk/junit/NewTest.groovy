#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class NewTest {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f

    NewTest() {
    }

    Boolean in_epsilon(Float a, Float b, Float tolerance = 0.001f) {
		def delta = Math.abs(tolerance)
		//return (a - delta) <= b && (a + delta) >= b
		return !((a + delta) < b) && !((b + delta) < a)
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
    void test_classExists() {
        try {
            Class.forName(sprintf("%s.Library",
                this.getClass().getPackage().getName()))
        } catch(ClassNotFoundException exc) {
            //fail("Class(es) not existent: " + Library.class.getName())
            fail(sprintf("%s %s", "Class(es) not existent:",
                java.util.Arrays.toString([Library.class.getName()] as String[])))
        }
    }

	@Test
    void test_method() { assertEquals(4, 2 * 2)
    }
    @Test
    void test_dblMethod() {
		//assertEquals(100.001f, 100.001f, epsilon)
		assertTrue(in_epsilon(100.001f, 100.001f, epsilon))
    }
    @Test
    void test_strMethod() { assertEquals("Hello", "Hello")
    }
    @org.junit.jupiter.api.Timeout(value = 1000000,
      unit = java.util.concurrent.TimeUnit.MILLISECONDS)
    @Test
    void test_timeoutMethod() { for (int i = 0; 1.0e6f > i; ++i);
    }
    @org.junit.jupiter.api.Disabled @Test
    void test_ignoredMethod() { assertEquals(5, 2 * 2)
    }
    @Test //(expected = AssertionError.class)
    void test_failMethod() { fail()
    }
    @Test //(expected = IllegalArgumentException.class)
    public void test_exceptionMethod() {
        //throw new IllegalArgumentException();
        assertThrows(IllegalArgumentException.class,
          () -> { throw new IllegalArgumentException(); });
    }
}
