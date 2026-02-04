package Utils.Collections;

import java.util.List;
import java.util.Map;

public interface MyIHeap<V> {
    int put(V v);
    V lookUp(int adr);

    List<Integer> getKeys();

    boolean isDefined(int adr);


    void setContent(Map<Integer,V> map);
    Map<Integer,V> getContent();

    void update(int address, V value);
    void clear();
}
