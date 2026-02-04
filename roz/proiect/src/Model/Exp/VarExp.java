package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public class VarExp implements Exp{
    private String id;

    public VarExp(String id) {
        this.id = id;
    }

    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException
    {
        return typeEnv.lookUp(id);
    }

    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        if(!tbl.isDefined(id))
            throw new MyException("Variable "+id+" is not defined");
        return tbl.lookUp(id);
    }

    @Override
    public Exp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public String toString()
    {
        return id;
    }
}
