package Model.PrgState;

import Model.Stmt.CompStmt;
import Model.Stmt.IStmt;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Collections.*;
import Utils.Exceptions.MyException;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

public class PrgState {
    private int ID;
    private static int lastID=0;
    private MyIStack<IStmt> stk;
    private MyIDictionary<String, Value> symTable;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIHeap<Value> heapTable;
    private MyIList<Value> out;
    private IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symTable, MyIList<Value> out,
                    MyIDictionary<StringValue,BufferedReader> fileTable, MyIHeap<Value> heap, IStmt prg) {
        this.stk = stk;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heapTable=heap;
        originalProgram = prg.deepCopy();
        stk.push(prg);
        ID=setID();
    }

    public MyIStack<IStmt> getStk() {return this.stk;}
    public void setStk(MyIStack<IStmt> stk) {this.stk = stk;}
    public MyIDictionary<String, Value> getSymTable() {return this.symTable;}
    public void setSymTable(MyIDictionary<String, Value> symTable) {this.symTable = symTable;}
    public MyIList<Value> getOut() {return this.out;}
    public void setOut(MyIList<Value> out) {this.out = out;}
    public MyIDictionary<StringValue, BufferedReader> getFileTable(){return this.fileTable;}
    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable){this.fileTable = fileTable;}

    public MyIHeap<Value> getHeapTable() {return this.heapTable;}

    public Boolean isNotCompleted()
    {
        return !this.stk.isEmpty();

    }

    public synchronized int setID()
    {
        lastID++;
        return lastID;
    }

    public int getID() {return this.ID;}

    public PrgState oneStep() throws MyException
    {
        if(stk.isEmpty()) throw new MyException("stack is empty");
        IStmt crtStmt=stk.pop();
        return crtStmt.execute(this);

    }

    public void restoreOriginal()
    {
        stk.clear();
        stk.push(originalProgram.deepCopy());
        symTable.clear();
        out.clear();
        fileTable.clear();
        heapTable.clear();
    }

    private String formatSymTable(MyIDictionary<String, Value> tbl) {
        StringBuilder sb = new StringBuilder();
        for (String key : tbl.getKeys())
        {
            Value val=tbl.lookUp(key);
            sb.append(key).append(" -> ").append(val).append("\n");
        }
        return sb.toString();
    }

    private String formatFileTable(MyIDictionary<StringValue, BufferedReader> tbl) {
        StringBuilder sb = new StringBuilder();
        for(StringValue key:tbl.getKeys())
        {
            BufferedReader val=tbl.lookUp(key);
            sb.append(key).append(" -> ").append(val).append("\n");
        }

        return sb.toString();
    }

    private String formatOut(MyIList<Value> out) {
        StringBuilder sb = new StringBuilder();
        for(Value val: out.getList())
        {
            sb.append(val).append("\n");
        }
        return sb.toString();
    }

    private String formatHeap(MyIHeap<Value> heap) {
        StringBuilder sb = new StringBuilder();
        for(Integer key: heap.getKeys())
        {
            Value val=heap.lookUp(key);
            sb.append(key).append(" -> ").append(val).append("\n");
        }

        return sb.toString();
    }

    private String formatExeStack(MyIStack<IStmt> stk)
    {
        return distinctStatementsString();
    }

    @Override
    public String toString()
    {
        return "----------------------------------------------- \n ID: " + ID +"\n"+
                "ExeStack:\n" + formatExeStack(stk) + "\n" +
                "SymTable:\n" + formatSymTable(symTable) + "\n" +
                "Out:\n" + formatOut(out) + "\n" +
                "FileTable:\n" + formatFileTable(fileTable) + "\n" +
                "HeapTable:\n" + formatHeap(heapTable) +"\n";
    }

    private Node<IStmt> toTree(IStmt stmt)
    {
        if (stmt instanceof CompStmt compStmt) {
            Node<IStmt> node = new Node<>(compStmt);
            node.setLeft(toTree(compStmt.getFirstStmt()));
            node.setRight(toTree(compStmt.getSecondStmt()));
            return node;
        } else {
            return new Node<>(stmt);
        }
    }

    private Node<IStmt> buildStackTree(List<IStmt> stackList, List<IStmt> inOrderList)
    {
        Node<IStmt> root=null;
        Node<IStmt> lastNode=null;
        for(IStmt stmt: stackList)
        {
            if(!(stmt instanceof CompStmt))
            {
                inOrderList.add(stmt);
                continue;
            }

            Node<IStmt> node = toTree(stmt);

            if(root==null)
            {
                root=node;
            }
            else{
                Node<IStmt> temp=lastNode;
                while(temp.getRight()!=null)
                {
                    temp=temp.getRight();
                }
                temp.setRight(node);
            }
            lastNode=node;

        }
        return root;
    }


    private List<IStmt> distinctStatementsSplitting()
    {
        MyTree<IStmt> tree = new MyTree<>();
        List<IStmt> inOrderList = new LinkedList<>();
        Node<IStmt> root=buildStackTree(stk.toListSReversed(),inOrderList);
        tree.inorderTraversal(inOrderList,root);

        return inOrderList;
    }

    public String distinctStatementsString()
    {
        List<IStmt> stmts = distinctStatementsSplitting();
        StringBuilder sb = new StringBuilder();
        for (IStmt stmt : stmts) {
            if(!(stmt instanceof CompStmt))
                sb.append(stmt.toString()).append("\n");
        }
        return sb.toString();
    }

}
