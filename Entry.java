package tony;

public class Entry<K, V> {
	
	// Member Data
	private K k;
	private V v;
	
	// Default Constructor that sets the key of an entry to the given key, and the value to the given value
	public Entry(K key, V value) {
		k = key;
		v = value;
	}

	// Method that returns the value of an entry
	public V getValue() {
		return v;
	}
	
	// Method that sets the value of an entry, and returns the old value that used to be associated with that key
	protected V setValue(V value) {
		V old = v;
		v = value;
		return old;
	}
	
	// Method that returns the key of an entry
	public K getKey() {
		return k;
	}
	
	// Method that sets the key of an entry to a given key
	protected void setKey(K key) {
		k = key;
	}
	
	

}
