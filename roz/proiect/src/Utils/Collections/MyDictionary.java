package Utils.Collections;

import Model.Types.Type;
import Model.Values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private Map<K,V> map;

    public MyDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public void put(K key, V value){
        map.put(key,value);
    }

    @Override
    public V lookUp(K key){
        return map.get(key);
    }

    @Override
    public boolean isDefined(K key){
        return  map.containsKey(key);
    }

    @Override
    public void update(K key, V value){
        map.put(key,value);
    }

    @Override
    public void remove(K key){
        map.remove(key);
    }

    @Override
    public String toString()
    {
        return map.toString();
    }

    @Override
    public List<K> getKeys()
    {
        return new ArrayList<>(map.keySet());
    }

    @Override
    public Map<K,V> getContent()
    {
        return this.map;
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    public MyIDictionary<K,V> deepCopy() {
        MyIDictionary<K,V> copy = new MyDictionary<>();
        for(K key:map.keySet()) {
            V value = map.get(key);

            K newKey = (key instanceof Value vKey) ? (K) vKey.deepCopy() : key;

            V newValue;
            if (value instanceof Type tValue) {
                newValue = (V) tValue.deepCopy();
            } else if (value instanceof Value vValue) {
                newValue = (V) vValue.deepCopy();
            } else {
                newValue = value;
            }

            copy.put(newKey, newValue);
        }

        return copy;
    }
}
