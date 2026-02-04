package Utils.Collections;

import Model.Values.Value;

public class Node<T> {
    T value;
    Node<T> left;
    Node<T> right;

    public Node(T v)
    {
        this.value = v;
        this.left = null;
        this.right = null;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> getRight() {
        return right;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value=value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
