package Utils.Collections;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private List<T> list;

    public MyList()
    {
        this.list=new ArrayList<T>();
    }

    @Override
    public void add(T t)
    {
        list.add(t);
    }

    @Override
    public void clear()
    {
        list.clear();
    }

    @Override
    public List<T> getList()
    {
        return list;
    }

    @Override
    public String toString()
    {
        return list.toString();
    }



}
