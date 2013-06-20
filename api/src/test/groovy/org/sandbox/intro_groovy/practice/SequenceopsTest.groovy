package org.sandbox.intro_groovy.practice

import spock.lang.Specification
import java.lang.reflect.Array
import java.util.Comparator
import java.util.Arrays

import org.sandbox.intro_groovy.util.Library as Util
import org.sandbox.intro_groovy.practice.Sequenceops as Seqops

class SequenceopsTest extends Specification {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f
	private int[] ints = [0, 1, 2, 3, 4], ints_rev = [4, 3, 2, 1, 0]
	private def lst_ints = Arrays.asList(ints)
	private def lst_ints_rev = Arrays.asList(ints_rev)
	
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
	
	def "find index spec"() {
		expect:
		// get closure from method pointer: Classnm.&methodNm
		[Seqops.&indexOf_lp].every { f ->
			3 == f.call(el, lst_ints, Util.intCmp)
			1 == f.call(el, lst_ints_rev, Util.intCmp)
			}
		[Seqops.&indexOf_lp].every { f ->
			3 == f.call(el, ints as Integer[], Util.intCmp)
			1 == f.call(el, ints_rev as Integer[], Util.intCmp)
			}
		
		where:
		el << 3
		
	}
	
	def "reverse spec"() {
		expect:
		// get closure from method pointer: Classnm.&methodNm
		[Seqops.&reverse_lp].every { f ->
			def tmpL1 = lst_ints.collect{e -> e}
			f.call(tmpL1)
			lst_ints_rev == tmpL1
			}
		[Seqops.&reverse_lp].every { f ->
			def tmpA1 = ints.collect{e -> e}
			f.call(tmpA1)
			ints_rev == tmpA1
			}
		
		//where:
	}
}
