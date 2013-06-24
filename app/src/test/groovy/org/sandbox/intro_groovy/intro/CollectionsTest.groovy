package org.sandbox.intro_groovy.intro

import spock.lang.Specification
import spock.util.matcher.IsCloseTo
import static spock.util.matcher.HamcrestMatchers.closeTo

import org.sandbox.intro_groovy.util.Library as Util

class CollectionsTest extends Specification {
	//private def tolerance = 2.0f * Float.MIN_VALUE
	private def epsilon = 1.0e-7f
	private static def floats = [25.7f, 0.1f, 78.5f, 52.3f] as Float[]
	private def ints = [2, 1, 0, 4, 3] as Integer[]
	private def chars = ['a', 'e', 'k', 'p', 'u', 'k', 'a'] as Character[]
	
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
	
	def "deques spec"() {
		given:
		def deque1 = floats as ArrayDeque<Float>
		
		expect:
		def deque0 = [] as ArrayDeque<Float>
		true == deque0.isEmpty()
		floats.length == deque1.size()
		//Util.in_epsilon(floats[0], deque1.peek(), epsilon)
		//floats[0] (new IsCloseTo(deque1.peek(), epsilon))
		floats[0] closeTo(deque1.peek(), epsilon)
		true == deque1.offer(-0.5f)
		floats[0] closeTo(deque1.poll(), epsilon)
		"[0.1, 78.5, 52.3, -0.5]" == deque1.toString()
		
		//where:
	}
	
	def "lists spec"() {
		given:
		def (lst1, nines) = [ints as ArrayList<Integer>, [9, 9, 9, 9]]
		
		expect:
		true == [].isEmpty()
		ints.length == lst1.size()
		ints[0] == lst1[0]
		ints[2] == lst1[2]
		1 == lst1.indexOf(ints[1])
		lst1.addAll(nines)
		(nines.size() + ints.length) == lst1.size()
		//Collections.sort(lst1, new Util.Cmp_rev<Integer>())
		lst1.sort() {a, b -> b.compareTo(a)}
		"[9, 9, 9, 9, 4, 3, 2, 1, 0]" == lst1.toString()
		
		//where:
	}
	
	def "maps spec"() {
		given:
		//def (key1, map1) = ["", new HashMap<String, Character>()]
		def (key1, map1) = ["", [:]]
		
		expect:
		true == map1.isEmpty()
		chars.eachWithIndex {e, i -> map1["ltr ${i % 5}"] = e}
		5 == map1.size()
		null == map1.put("ltr 20", "Z")
		true == map1.containsKey("ltr 2")
		'k' as Character == map1["ltr 2"]
		map1.remove("ltr 2")
		false == map1.containsKey("ltr 2")
		
		//where:
	}
	
	def "priorqueues spec"() {
		given:
		def floats2 = [0.0f, 0.0f, 0.0f, 0.0f] as Float[]
		def pri_q1 = [] as PriorityQueue<Float>
		def pri_q2 = new PriorityQueue<Float>(4, new Util.Cmp_rev<Float>())
		
		expect:
		true == pri_q1.isEmpty()
		floats.eachWithIndex {e, i -> floats2[i] = e}
		floats.each {e -> pri_q1.offer(e) ; pri_q2.offer(e)}
		Arrays.sort(floats2)
		floats2.length == pri_q1.size()
		//Util.in_epsilon(floats2[0], pri_q1.peek(), epsilon)
		floats2[0] closeTo(pri_q1.peek(), epsilon)
		floats2[-1] closeTo(pri_q2.peek(), epsilon)
		floats2[0] closeTo(pri_q1.poll(), epsilon)
		floats2[-1] closeTo(pri_q2.poll(), epsilon)
		pri_q1.offer(-0.5f)
		pri_q2.offer(-0.5f)
		"[-0.5, 25.7, 78.5, 52.3]" == Arrays.toString(pri_q1.toArray())
		"[52.3, 0.1, 25.7, -0.5]" == Arrays.toString(pri_q2.toArray())
		
		//where:
	}
}
