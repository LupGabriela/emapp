package Utils.Collections;

import Utils.Exceptions.MyException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T>{
    private Stack<T> stack;

    public MyStack()
    {
        this.stack = new Stack<>();
    }

    @Override
    public void push(T t)
    {
        stack.push(t);
    }

    @Override
    public T pop() throws MyException
    {
        if(stack.isEmpty())
            throw new MyException("Nothing to pop!");
        return stack.pop();
    }

    @Override
    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    @Override
    public String toString()
    {
        return stack.toString();
    }

    @Override
    public T peek() throws MyException
    {
        if(stack.isEmpty())
            throw new MyException("Nothing to peek!");
        return stack.peek();
    }

    @Override
    public List<T> toListS()
    {
        return new LinkedList<>(stack);
    }

    @Override
    public List<T> toListSReversed()
    {
        List<T> reversed = new LinkedList<>(stack);
        Collections.reverse(reversed);
        return reversed;
    }

    @Override
    public void clear()
    {
        stack.clear();
    }

}
