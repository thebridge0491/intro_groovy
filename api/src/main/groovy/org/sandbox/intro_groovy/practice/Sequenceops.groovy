package org.sandbox.intro_groovy.practice

import java.util.Comparator
import java.util.Arrays
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** DocComment:
 * <p>Brief description.</p> */
final class Sequenceops {
	private static final def pracLogger = LoggerFactory.getLogger("prac")
    
    static <T> void swapItems(int a, int b, List<T> lst) {
		T swap = lst[a]; lst[a] = lst[b]; lst[b] = swap
	}
	
	static <T> int indexOf_lp(T data, List<T> lst, Comparator<? super T> cmp) {
		for (int i = 0; lst.size() > i; ++i)
			if (0 == cmp.compare(data, lst[i]))
				return i
		return -1
	}
	
	static <T> void reverse_lp(List<T> lst) {
		int i = 0, j = lst.size() - 1
		pracLogger.info("reverse_lp(List)")
		
		while (j > i)
			swapItems(i++, j--, lst)
	}
    
    static <T> void swapItems(int a, int b, T[] arr) {
		T swap = arr[a]; arr[a] = arr[b]; arr[b] = swap
	}
	
	static <T> int indexOf_lp(T data, T[] arr, Comparator<? super T> cmp) {
		for (int i = 0; arr.length > i; ++i)
			if (0 == cmp.compare(data, arr[i]))
				return i
		return -1
	}
	
	static <T> void reverse_lp(T[] arr) {
		int i = 0, j = arr.length - 1
		pracLogger.info("reverse_lp()")
		
		while (j > i)
			swapItems(i++, j--, arr)
	}
	
	static void main(String[] args) {
		def Integer[] ints = [0, 1, 2, 3]
        def strBldr = new StringBuilder()
        reverse_lp(ints)
		
        for (ival in Arrays.asList(ints))
            strBldr.append(((0 < strBldr.length()) ? ", " : "") + ival)
		printf "reverse(%s): [%s]\n", Arrays.toString(ints), strBldr.toString()
	}
}
