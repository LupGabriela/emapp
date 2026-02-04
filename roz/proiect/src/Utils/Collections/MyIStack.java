package Utils.Collections;

import Utils.Exceptions.MyException;

import java.util.List;

public interface MyIStack<T> {
    public void push(T t);
    public T pop() throws MyException;
    boolean isEmpty();
    public T peek() throws MyException;
    public List<T> toListS();
    public List<T> toListSReversed();
    void clear();

}
