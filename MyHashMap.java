import java.util.LinkedList;

public class MyHashMap<K, V> {
    // Default initial capacity
    private static final int DEFAULT_CAPACITY = 128;
    // Load factor threshold for resizing
    private static final float LOAD_FACTOR_THRESHOLD = 0.75f;

    // Entry class to hold key-value pairs
    class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<K, V>>[] table;
    private int size; // Number of key-value pairs

    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    // Hash function to compute index for a key
    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(K key, V value) {
        int index = hash(key);

        if(table[index] == null){
            table[index] = new LinkedList<>();
        }

        // Check if key already exists and update its value
        for(Entry<K, V> entry : table[index]){
            if(entry.key.equals(key)){
                entry.value = value;
                return;
            }
        }

        // Key does not exist, add new entry
        table[index].add(new Entry<>(key, value));
        size++;

        // Check load factor and resize if necessary
        if((float)size / table.length > LOAD_FACTOR_THRESHOLD){
            resize();
        }
    }

    public V get(K key) {
        int index = hash(key);

        if(table[index] != null){
            for(Entry<K, V> entry : table[index]){
                if(entry.key.equals(key)){
                    return entry.value;
                }
            }
        }
        return null; // Key not found
    }

    public void remove(K key) {
        int index = hash(key);

        if(table[index] != null){
            for(Entry<K, V> entry : table[index]){
                if(entry.key.equals(key)){
                    table[index].remove(entry);
                    size--;
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[oldTable.length * 2];
        size = 0;

        for(LinkedList<Entry<K, V>> bucket : oldTable){
            if(bucket != null){
                for(Entry<K, V> entry : bucket){
                    put(entry.key, entry.value);
                }
            }
        }
    }

    public Iterable<K> keySet(){
        LinkedList<K> keys = new LinkedList<>();

        for(LinkedList<Entry<K, V>> bucket : table){
            if(bucket != null){
                for(Entry<K, V> entry : bucket){
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }
}