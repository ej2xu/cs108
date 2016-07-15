/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/
package assign1;

import java.util.*;

public class Taboo<T> {
	private HashMap<T, HashSet<T>> tabooMap;	
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		tabooMap = new HashMap<T, HashSet<T>>();
		T key = null;
		for (T rule : rules) {
			if (key != null && rule != null) {
				if (!tabooMap.containsKey(key))
					tabooMap.put(key, new HashSet<T>());
				tabooMap.get(key).add(rule);
			}
			key = rule;
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		 if (tabooMap.containsKey(elem))
			 return tabooMap.get(elem);
		 else
			 return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> it = list.iterator();
		T last = null;
		while (it.hasNext()) {
			T curr = it.next();
			if (last != null && tabooMap.containsKey(last) && tabooMap.get(last).contains(curr))
				it.remove();
			else last = curr;
		}
	}
}
