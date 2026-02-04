package Utils.Collections;

import java.util.List;
import java.util.Map;

public interface MyIDictionary<K,V> {
    void put(K key, V value);
    V lookUp(K key);
    boolean isDefined(K key);
    void update(K key, V value);
    void remove(K key);
    List<K> getKeys();
    void clear();
    Map<K,V> getContent();

    MyIDictionary<K,V> deepCopy();
}
