package Utils.Collections;

import Utils.Collections.MyDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHeap<V> implements MyIHeap<V>
{
    private static int address=0;

    private Map<Integer,V> map;

    public MyHeap()
    {
        map=new HashMap<>();
    }

    private int nextAddress()
    {return ++address;}

    public static void resetAddress()
    {
        address=0;
    }

    @Override
    public int put(V v)
    {
        int adr=nextAddress();
        map.put(adr,v);
        return adr;
    }

    @Override
    public V lookUp(int adr)
    {
        return map.get(adr);
    }

    @Override
    public List<Integer> getKeys()
    {
        return new ArrayList<>(map.keySet());
    }

    @Override
    public boolean isDefined(int adr)
    {
        return map.containsKey(adr);
    }

    @Override
    public void setContent(Map<Integer,V> map)
    {
        this.map=map;
    }

    @Override
    public Map<Integer,V> getContent()
    {
        return map;
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    public void update(int adr, V value)
    {
        map.put(adr,value);
    }


}
