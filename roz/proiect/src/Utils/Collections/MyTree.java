package Utils.Collections;

import java.util.ArrayList;
import java.util.List;

public class MyTree<T> {
    private Node<T> root;

    public MyTree() {
        this.root=null;
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    public boolean isEmpty()
    {
        return root==null;
    }

    public void inorderTraversal(List<T> list, Node<T> root)
    {
        if(root==null) return;
        inorderTraversal(list,root.left);
        list.add(root.value);
        inorderTraversal(list,root.right);
    }

    @Override
    public String toString()
    {
        List<T> list=new ArrayList<T>();
        inorderTraversal(list,root);
        return list.toString();
    }
}
