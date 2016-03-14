package org.datastruct;

import java.util.*;

@SuppressWarnings("deprecation")
public class RadixTreeMap<K extends CharSequence, V> implements NavigableMap<K, V>, Iterable<V> {

	private RadixEntry rootNode;
	private Comparator<CharSequence> cmp;

	public RadixTreeMap() {
		rootNode = new RadixEntry();
		cmp = (CharSequence k1, CharSequence k2) -> {
			int numChar = 0;
			while (numChar < k2.length() && numChar < k1.length()) {
				if (k2.charAt(numChar) != k1.charAt(numChar)) {
					break;
				} else {
					numChar++;
				}
			}
			return numChar;
		};
	}

	private K linkKey(K k1, K k2) {
		StringBuilder buff = new StringBuilder();
		buff.append(k1).append(k2);
		return (K) buff.subSequence(0, buff.length());
	}

	public V add(V data, K key) throws NullPointerException {
		if (key == null || data == null) {
			throw new NullPointerException();
		}
		return insert(data, key);
//		return insert(data, key, rootNode);
	}

	/**
	 * Rekurzivna metoda za vnasanje novega elementa
	 *
	 * @param data Nov element
	 * @param key  Kjuc novega elementa
	 * @param node Vozlisce v obdelavi
	 * @return Vrednost starega elementa
	 */
	private V insert(V data, K key, RadixEntry node) {
		int numMatchChar = cmp.compare(node.key, key);
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.setValue(data);
		} else if (numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			K ostanekKljuca = (K) key.subSequence(numMatchChar, key.length());
			RadixEntry chield = node.children.get(ostanekKljuca.charAt(0));
			if (chield != null) {
				return insert(data, ostanekKljuca, chield);
			} else {
				node.children.put(ostanekKljuca.charAt(0), new RadixEntry(data, ostanekKljuca));
				return null;
			}
		} else {
			RadixEntry tmp = new RadixEntry(node.data, (K) node.key.subSequence(numMatchChar, node.key.length()), node.children);
			node.key = (K) key.subSequence(0, numMatchChar);
			node.children = new SkipMap<>(5);
			node.children.put(tmp.key.charAt(0), tmp);
			if (numMatchChar < key.length()) {
				RadixEntry tmp1 = new RadixEntry(data, (K) key.subSequence(numMatchChar, key.length()));
				node.data = null;
				node.children.put(tmp1.key.charAt(0), tmp1);
			} else {
				node.data = data;
			}
			return null;
		}
	}

	/**
	 * Iterativna metoda za vnasanje novega elemnta
	 *
	 * @param key  Kjuc novega elementa
	 * @param data Vrednost novega elementa
	 * @return Vrednost starega elementa
	 */
	private V insert(V data, K key) {
		RadixEntry curr = rootNode;
		K currKey = key;
		while (true) {
			int numMatchChar = cmp.compare(curr.key, currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				return curr.setValue(data);
			} else if (numMatchChar == 0 || (numMatchChar == curr.key.length() && numMatchChar < currKey.length())) {
				currKey = (K) currKey.subSequence(numMatchChar, currKey.length());
				RadixEntry chield = curr.children.get(currKey.charAt(0));
				if (chield != null) {
					curr = chield;
				} else {
					curr.children.put(currKey.charAt(0), new RadixEntry(data, (K) currKey));
					return null;
				}
			} else {
				RadixEntry tmp = new RadixEntry(curr.data, (K) curr.key.subSequence(numMatchChar, curr.key.length()), curr.children);
				curr.key = (K) currKey.subSequence(0, numMatchChar);
				curr.children = new SkipMap<>(5);
				curr.children.put(tmp.key.charAt(0), tmp);
				if (numMatchChar < currKey.length()) {
					RadixEntry tmp1 = new RadixEntry(data, (K) currKey.subSequence(numMatchChar, currKey.length()));
					curr.data = null;
					curr.children.put(tmp1.key.charAt(0), tmp1);
				} else {
					curr.setValue(data);
				}
				return null;
			}
		}
	}

	/**
	 * Metoda, ki iterativno isce element
	 *
	 * @param key Kjuc iskanega elemnta
	 * @return Vrednost elementa
	 */
	private V search(CharSequence key) {
		CharSequence currKey = key;
		RadixEntry curr = rootNode;
		while (curr != null) {
			int numMatchChar = cmp.compare(curr.key, currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				return curr.data;
			} else if (numMatchChar < currKey.length() && numMatchChar == curr.key.length()) {
				currKey = currKey.subSequence(numMatchChar, currKey.length());
				curr = curr.children.get(currKey.charAt(0));
			} else {
				curr = null;
			}
		}
		return null;
	}

	/**
	 * Metoda, ki rekuzivno isce element
	 *
	 * @param node Vozlisce, ki ga pregledujemo
	 * @param key  Kljuc iskanega elementa
	 * @return Vrednost elementa
	 */
	private V search(RadixEntry node, CharSequence key) {
		int numMatchChar = cmp.compare(node.key, key);
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if (numMatchChar < key.length() && numMatchChar == node.key.length()) {
			CharSequence nKey = key.subSequence(numMatchChar, key.length());
			RadixEntry chield = node.children.get(nKey.charAt(0));
			if (chield != null) {
				return search(chield, nKey);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean remove(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		return !isEmpty() && delete(key) != null;
//		return !isEmpty() && delete(key, null, this.rootNode) != null;
	}

	/**
	 * Rekurzivna metoda za brisanje elementa
	 *
	 * @param key    Kjuc elementa
	 * @param parent Predhodnik trenutnga vozlisca
	 * @param node   Trenutno vozlisce v obdelavi
	 * @return Vrednost izbrisanega elementa
	 */
	private V delete(String key, RadixEntry parent, RadixEntry node) {
		int numMatchChar = cmp.compare(node.key, key);
		if (node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar);
			RadixEntry chield = node.children.get(ostanekKljuca.charAt(0));
			if (chield != null) {
				return delete(ostanekKljuca, node, chield);
			} else {
				return null;
			}
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				V data = node.data;
				if (node.children.isEmpty()) {
					parent.children.remove(node.key.charAt(0));
					if (parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						mergeNodes(parent, node);
					}
				} else if (node.children.size() == 1) {
					for (RadixEntry e : node.children.values()) {
						mergeNodes(node, e);
					}
				} else {
					node.data = null;
				}
				return data;
			}
		}
		return null;
	}

	/**
	 * Iterativna metoda za brisanje elementa
	 *
	 * @param key Kjuc elementa
	 * @return Vresnost elementa
	 */
	private V delete(String key) {
		RadixEntry curr = rootNode;
		RadixEntry prev = null;
		String currKey = key;
		V data = null;
		while (curr != null) {
			int numMatchChar = cmp.compare(curr.key, currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				if (curr.data != null) {
					data = curr.data;
					if (curr.children.isEmpty()) {
						prev.children.remove(curr.key.charAt(0));
						if (prev.children.size() == 1 && prev.data == null && prev != rootNode) {
							for (RadixEntry e : prev.children.values()) {
								mergeNodes(prev, e);
							}
						}
					} else if (curr.children.size() == 1 && prev != rootNode) {
						for (RadixEntry e : curr.children.values()) {
							mergeNodes(curr, e);
						}
					} else {
						curr.data = null;
					}
				}
				curr = null;
			} else if (curr.key.equals("") || (numMatchChar < currKey.length() && numMatchChar == curr.key.length())) {
				currKey = currKey.substring(numMatchChar);
				prev = curr;
				curr = curr.children.get(currKey.charAt(0));
			} else {
				curr = null;
			}
		}
		return data;
	}

	private void mergeNodes(RadixEntry parent, RadixEntry child) {
		StringBuilder buff = new StringBuilder();
		buff.append(parent.key).append(child.key);
		parent.key = (K) buff.subSequence(0, buff.length());
		parent.data = child.data;
		parent.children = child.children;
	}

	/**
	 * Metoda za stetje elementov v strukturi
	 *
	 * @return Stevilo elementov v srukturi
	 */
	public int count() {
		int size = 0;
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				size++;
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
			} else {
				curr = null;
			}
		}
		return size;
	}

	public List<V> asList() {
		List<V> list = new ArrayList<>(count());
		asList(list);
//		asList(rootNode, list);
		return list;
	}

	/**
	 * Iterativna metoda za polnjenje Lista
	 *
	 * @param list List, ki ga zelimo napolniti
	 */
	private void asList(List<V> list) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				list.add(curr.data);
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Lista
	 *
	 * @param node Vozlisce, ki ga obdelujemo
	 * @param list List, ki ga zelimo napolniti
	 */
	private void asList(RadixEntry node, List<V> list) {
		node.children.values().forEach(children -> asList(children, list));
		if (node.data != null) {
			list.add(node.data);
		}
	}

	@Deprecated
	public void printTree() {
		printTree(0, this.rootNode);
	}

	private void printTree(int len, RadixEntry node) {
		System.out.print("|");
		for (int i = 0; i < len; i++) {
			System.out.print("_");
		}
		if (node.data != null) {
			System.out.printf("%s => [%s]%n", node.key, node.data.toString());
		} else {
			System.out.printf("%s%n", node.key);
		}
		for (RadixEntry child : node.children.values()) {
			printTree(len + node.key.length(), child);
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new RadixTreeIterator();
	}

	@Override
	public int size() {
		return count();
	}

	@Override
	public boolean isEmpty() {
		return this.rootNode.children.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) throws NullPointerException, ClassCastException {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException();
		}
//		return search(value);
		return search(rootNode, value);
	}

	/**
	 * Iterativna metoda za iskanje elementa
	 *
	 * @param value Vrednost elementa
	 * @return Ali element obstaja v strukturi
	 */
	private boolean search(Object value) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null && curr.data.equals(value)) {
				return true;
			} else {
				for (RadixEntry chield : curr.children.values()) {
					stack.push(chield);
				}
				if (!stack.isEmpty()) {
					curr = stack.pop();
				} else {
					curr = null;
				}
			}
		}
		return false;
	}

	/**
	 * Rekurzivna metoda za iskanje elementa
	 *
	 * @param node  Vozlisce, ki ga pregledujemo
	 * @param value Vrednost elementa
	 * @return Ali element obstaja v strukturi
	 */
	private boolean search(RadixEntry node, Object value) {
		if (node.data != null && node.data.equals(value)) {
			return true;
		} else {
			for (RadixEntry child : node.children.values()) {
				if (this.search(child, value)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public V get(Object key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			String sKey = (String) key;
			return search(sKey);
//			return search(rootNode, sKey);
		}
	}

	@Override
	public V put(K key, V value) throws NullPointerException {
		return add(value, key);
	}

	@Override
	public V remove(Object key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		String sKey = (String) key;
		return !isEmpty() ? delete(sKey) : null;
//		return !isEmpty() ? delete(sKey, null, rootNode) : null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		rootNode = new RadixEntry();
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>(count());
		keySet(set);
//		keySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta s kjuci
	 *
	 * @param set Set, ki ga zelimo napolniti
	 */
	private void keySet(Set<K> set) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<K> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(key);
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = linkKey(kStack.pop(), curr.key);
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Seta s kjuci
	 *
	 * @param node Vozlisce, ki ga pregledujemo
	 * @param key  Kjuc, vozlisca (v trenutnem vozliscu se nahaj samo delcek kljuca)
	 * @param set  Set, ki ga zelimo napolniti
	 */
	private void keySet(RadixEntry node, K key, Set<K> set) {
		if (node.data != null) {
			set.add(linkKey(key, node.key));
		}
		node.children.values().forEach(children -> keySet(children, linkKey(key, node.key), set));
	}

	@Override
	public Collection<V> values() {
		return asList();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>(count());
		entrySet(set);
//		entrySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta z zapisi v nasi strukturi
	 *
	 * @param set Set, ki ga zelmo napolniti
	 */
	private void entrySet(Set<Map.Entry<K, V>> set) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<K> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(new RadixEntry(curr.data, key, null));
			}
			for (RadixEntry chiled : curr.children.values()) {
				stack.push(chiled);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = linkKey(kStack.pop(), curr.key);
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Seta z zapisi v nasi podatkovni srukturi
	 *
	 * @param node Vozlisce, ki ga obdelujemo
	 * @param key  Kjuc trenutnega vozlisca (v trenutnem vozliscu je samo del kjuca)
	 * @param set  Set, ki ga zelmo napolniti
	 */
	private void entrySet(RadixEntry node, K key, Set<Map.Entry<K, V>> set) {
		if (node.data != null) {
			StringBuilder buff = new StringBuilder();
			set.add(new RadixEntry(node.data, linkKey(key, node.key), null));
		}
		node.children.values().forEach(children -> entrySet(children, linkKey(key, node.key), set));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		makeString(builder);
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	private void makeString(StringBuilder builder) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<K> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				builder.append(key).append('=').append(curr.data).append(", ");
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = linkKey(kStack.pop(), curr.key);
			} else {
				curr = null;
			}
		}
	}

	@Override
	public Entry<K, V> lowerEntry(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public K lowerKey(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			Map.Entry<K, V> e = lowerEntry(key);
			return e != null ? e.getKey() : null;
		}
	}

	@Override
	public Entry<K, V> floorEntry(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public K floorKey(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			Map.Entry<K, V> e = floorEntry(key);
			return e != null ? e.getKey() : null;
		}
	}

	@Override
	public Entry<K, V> ceilingEntry(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public K ceilingKey(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			Map.Entry<K, V> e = ceilingEntry(key);
			return e != null ? e.getKey() : null;
		}
	}

	@Override
	public Entry<K, V> higherEntry(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public K higherKey(K key) {
		if (isEmpty()) {
			return null;
		} else if (key == null) {
			throw new NullPointerException();
		} else {
			Map.Entry<K, V> e = higherEntry(key);
			return e != null ? e.getKey() : null;
		}
	}

	@Override
	public Entry<K, V> firstEntry() {
		if (isEmpty()) {
			return null;
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public Entry<K, V> lastEntry() {
		if (isEmpty()) {
			return null;
		} else {
			// TODO: 2/1/16
			return null;
		}
	}

	@Override
	public Entry<K, V> pollFirstEntry() {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public Entry<K, V> pollLastEntry() {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		// TODO: 2/1/16
		return null;
	}

	@Override
	public Comparator<? super K> comparator() {
		return cmp;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		// TODO: 2/1/16
		return subMap(fromKey, true, toKey, false);
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		// TODO: 2/1/16
		return headMap(toKey, false);
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		// TODO: 2/1/16
		return tailMap(fromKey, true);
	}

	@Override
	public K firstKey() {
		return isEmpty() ? null : firstEntry().getKey();
	}

	@Override
	public K lastKey() {
		return isEmpty() ? null : lastEntry().getKey();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		RadixTreeMap tree = new RadixTreeMap();
		tree.rootNode = rootNode != null ? (RadixEntry) rootNode.clone() : null;
		return tree;
	}

	class RadixEntry implements Map.Entry<K, V> {

		Map<Character, RadixEntry> children;
		V data;
		K key;

		private RadixEntry(V data, K key, Map<Character, RadixEntry> children) {
			this.data = data;
			this.key = key;
			this.children = children;
		}

		private RadixEntry(V data, K key) {
			this(data, key, new SkipMap<>(5));
		}

		private RadixEntry() {
			this(null, null);
			StringBuilder buff = new StringBuilder();
			buff.append("");
			key = (K) buff.subSequence(0, buff.length());
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.data;
		}

		@Override
		public V setValue(V v) throws NullPointerException {
			if (v == null) {
				throw new NullPointerException();
			}
			V ret = this.data;
			this.data = v;
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Entry)) return false;
			if (o == this) return true;
			Entry that = (Entry) o;
			return (getKey() != null ? getKey().equals(that.getKey()) : null == that.getKey()) && (getValue() != null ? getValue().equals(that.getValue()) : null == that.getValue());
		}

		@Override
		public int hashCode() {
			return getKey() != null ? getKey().hashCode() : 0;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			RadixEntry entryClone = new RadixEntry(data, key);
			Map<Character, RadixEntry> map = new SkipMap<>(5);
			for (Map.Entry<Character, RadixEntry> e : children.entrySet()) {
				map.put(e.getKey(), (RadixEntry) e.getValue().clone());
			}
			entryClone.children = map;
			return entryClone;
		}
	}

	private class RadixTreeIterator implements Iterator<V> {

		private org.datastruct.Stack<Iterator<RadixEntry>> stackIt;
		private RadixEntry next;

		private RadixTreeIterator() {
			this.stackIt = new org.datastruct.Stack<>();
			this.stackIt.push(rootNode.children.values().iterator());
			if (!stackIt.peek().hasNext()) {
				next = null;
			} else {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public V next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			V tmp = (V) next.data;
			if (!next.children.isEmpty()) {
				stackIt.push(next.children.values().iterator());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			} else if (stackIt.peek().hasNext()) {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			} else {
				do {
					stackIt.pop();
					if (stackIt.isEmpty()) {
						next = null;
						return tmp;
					}
				} while (!stackIt.peek().hasNext());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
			return tmp;
		}
	}
}

