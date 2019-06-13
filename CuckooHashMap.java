package tony;

import java.util.ArrayList;

public class CuckooHashMap<K, V> {

	// Member Data
	private Entry<K, V>[] arr1;
	private Entry<K, V>[] arr2;
	private int capacity;
	private int prime;
	private int n;

	// Default Constructor
	@SuppressWarnings("unchecked")
	public CuckooHashMap() {
		capacity = 4;
		prime = 10007;
		arr1 = new Entry[capacity];
		arr2 = new Entry[capacity];
	}

	// Constructor that takes in a certain capacity as a parameter
	@SuppressWarnings("unchecked")
	public CuckooHashMap(int cap) {
		capacity = cap;
		if (capacity % 2 == 0) {
			capacity = capacity + 1;
		}
		arr1 = new Entry[capacity];
		arr2 = new Entry[capacity];

	}

	// Constructor that takes in a capacity and certain prime as a parameter
	@SuppressWarnings("unchecked")
	public CuckooHashMap(int cap, int specPrime) {
		capacity = cap;
		if (capacity % 2 == 0) {
			capacity = capacity + 1;
		}

		prime = specPrime;
		arr1 = new Entry[capacity];
		arr2 = new Entry[capacity];
	}

	// Calculates the loadFactor of the hash map
	public float loadFactor() {
		return ((float)n / (float)capacity);
	}

	// returns the size of the hash map
	public int size() {
		return n;
	}

	// Returns the capacity of the hash map
	public int capacity() {
		return capacity;
	}

	// Gets a certain value from the hash map by hashing the given key and checking
	// its respective array to see if it contains a value at that position, and if neither
	// array contains a value at those positions, it will return null
	public V get(K key) {
		int hash1 = h1(key);
		int hash2 = h2(key);

		if (arr1[hash1] != null && arr1[hash1].getKey().equals(key)) {
			return arr1[hash1].getValue();
		}

		if (arr2[hash2] != null && arr2[hash2].getKey().equals(key)) {
			return arr2[hash2].getValue();
		}

		return null;

	}

	// Removes a value from the hash table by hashing the given key and
	// checking to see if there is a value in the position. If there is a value there, it is removed and the
	// size is decremented. If the loadfactor drops below 25% and the capacity is greater than 4,
	// Each array is halved in size and a set of all the Entries are rehashed into the new arrays
	public V remove(K key) {
		V temp = null;

		int hash1 = h1(key);
		int hash2 = h2(key);

		if (arr1[hash1] != null && arr1[hash1].getKey().equals(key)) {
			temp = arr1[hash1].getValue();
			arr1[hash1] = null;
			n--;

		}

		if (arr2[hash2] != null && arr2[hash2].getKey().equals(key)) {
			temp = arr2[hash2].getValue();
			arr2[hash2] = null;
			n--;
		}

		if (loadFactor() < .25 && capacity > 4) {
			resize(capacity / 2);
		}
		
		
		return temp;
	}
	
	// puts the Entry into the hash map
	// if the capacity is above 50% upon adding the entry, the array will get copied,
	// and a set of all the elements will get rehashed into the new array in order to prevent
	// collisions.
	public V put(K key, V value) {
        Entry<K,V> add = new Entry(key, value);
        Entry<K,V> temp;
        if (arr1[h1(key)] != null && arr1[h1(key)].getKey().equals(key)) {
            temp = arr1[h1(key)];
            arr1[h1(key)] = add;
            return temp.getValue();
        }
        if (arr2[h2(key)] != null && arr2[h2(key)].getKey().equals(key)) {
            temp = arr2[h2(key)];
            arr2[h2(key)] = add;
            return temp.getValue();
        }
        int counter = 0;
        while (counter < capacity) {
            temp = arr1[h1(add.getKey())];
            arr1[h1(add.getKey())] = add;
            if (temp == null) {
                n++;
                if (loadFactor() > 0.5) resize(capacity * 2);
                return null;
            }
            add = temp;
            temp = arr2[h2(add.getKey())];
            arr2[h2(add.getKey())] = add;
            if (temp == null) {
                n++;
                if (loadFactor() > 0.5) resize(capacity * 2);
                return null;
            }
            add = temp;
            counter++;
        }
        rehash();
        return put(add.getKey(), add.getValue());
    }

	// Returns and Iterable set of all of the entries in the hash map
	@SuppressWarnings("rawtypes")
	public Iterable<Entry<K, V>> entrySet() {
		ArrayList<Entry<K, V>> set = new ArrayList<Entry<K, V>>();
		for (int i = 0; i < capacity / 2; i++) {
			if (arr1[i] != null)
				set.add(arr1[i]);
			if (arr2[i] != null && arr2[i] != arr1[i]) {
				set.add(arr2[i]);
			}
		}
		return set;
	}

	// Returns an Iterable set of all of the keys in the hash map
	public Iterable<K> keySet() {
		ArrayList<K> set = new ArrayList<K>();
		for (int i = 0; i < capacity / 2; i++) {
			if (arr1[i] != null)
				set.add(arr1[i].getKey());
			if (arr2[i] != null && arr2[i] != arr1[i]) {
				set.add(arr2[i].getKey());
			}
		}
		return set;
	}

	// Returns and Iterable set of all of the values in the hash map
	public Iterable<V> valueSet() {
		ArrayList<V> set = new ArrayList<V>();
		for (int i = 0; i < capacity / 2; i++) {
			if (arr1[i] != null)
				set.add(arr1[i].getValue());
			if (arr2[i] != null && arr2[i] != arr1[i]) {
				set.add(arr2[i].getValue());
			}
		}
		return set;
	}

	// Helper function that finds the next biggest prime number and rehashes all of the values
	// with this new prime
	// This method is only called when you end up in a loop while putting an entry into the hash map
	private void rehash() {
		int counter = 1;
		while(counter != 0) {
			prime++;
			counter = 0;
			for(int i = 2; i <= Math.sqrt(prime); i++) {
				if(prime % i == 0)
					counter++;
			}
		}
		resize(capacity);
	}


	// Method that resizes the arrays to a given capacity, either because the load factor exceeded 50% due to a put,
	// or dropped below 25% due to a removal
	private void resize(int newCap) {
		ArrayList<Entry<K,V>> buffer = new ArrayList<Entry<K,V>>();
		for (Entry<K,V> e: entrySet()) {
			buffer.add(e);
		}
		capacity = newCap;
		n = 0;
		arr1 = new Entry[capacity / 2];
		arr2 = new Entry[capacity / 2];
		for (Entry<K,V> e: buffer) {
			put(e.getKey(), e.getValue());
		}
	}
	

	// Hash function that hashes a key into the first array
	private int h1(K key) {
		return (Math.abs(key.hashCode()) % prime) % (capacity / 2);
	}

	// Hash function that hashes a key into the second array
	private int h2(K key) {
		return ((Math.abs(key.hashCode()) / prime) % prime) % (capacity / 2);
	}

}
