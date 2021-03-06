#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import java.util.Comparator
import java.util.Arrays

/** DocComment:
 * <p>Brief description.</p> */
final class Library {
    static class Cmp<T extends Comparable<T>> implements Comparator<T> {
        @Override
        int compare(T o1, T o2) {
            return o1.compareTo(o2)
        }
    }
    
    static class Cmp_rev<T extends Comparable<T>> implements Comparator<T> {
        @Override
        int compare(T o1, T o2) {
            return -(o1.compareTo(o2))
            //return (o1.key < o2.key) ? -1 : ((o1.key > o2.key) ? 1 : 0)
        }
    }
    
	static def floatCmp = new Cmp<Float>()
	static def intCmp = new Cmp<Integer>()
    
    static <T> String mkStringInit(java.util.Collection<T> coll, String beg,
			String sep, String stop) {
        def strBldr = new StringBuilder()
        
        for (ival in coll)
            strBldr.append(((0 < strBldr.length()) ? sep : "") + ival)
        return beg + strBldr.toString() + stop
    }
    
    static <T> String mkString(java.util.Collection<T> coll) {
        return mkStringInit(coll, "[", ", ", "]")
    }
    
    static <K, V> String mkStringInit(java.util.Map<K, V> map, String beg,
			String sep, String stop, String mapsep) {
        def coll = new java.util.ArrayList<String>()
        
        for (entry in map)
            coll.add(entry.key + mapsep + entry.value)
        return mkStringInit(coll, beg, sep, stop)
    }
    
    static Boolean in_epsilon(Float a, Float b, Float tolerance = 0.001f) {
		def delta = Math.abs(tolerance)
		//return (a - delta) <= b && (a + delta) >= b
		return !((a + delta) < b) && !((b + delta) < a)
	}
	
	//static float[][] cartesian_prod_floats(float[] arr1, float[] arr2) {
	static <T> T[][] cartesian_prod(T[] arr1, T[] arr2) {
		//def arr_prod = [arr1, arr2].combinations().findAll { true }
		def arr_prod = arr1.collectMany {x -> arr2.collect {y -> 
			[x, y].findAll { true }}}
		
		return arr_prod
	}
	
	static void main(String[] args) {
		def (Integer[] arr1, Integer[] arr2) = [[0, 1, 2], [10, 20, 30]]
        def strBldr = new StringBuilder()
		
        for (arr in cartesian_prod(arr1, arr2))
            strBldr.append(((0 < strBldr.length()) ? ", " : "") + 
                Arrays.toString(arr))
		printf "cartesian_prod(%s, %s): [%s]\n", Arrays.toString(arr1),
            Arrays.toString(arr2), strBldr.toString()
	}
}
