package Model.Exp;

import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public class ReadHeapExp implements Exp
{
    private Exp exp;
    public ReadHeapExp(Exp exp)
    {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        Value val=exp.eval(tbl,heapTbl);
        if(val instanceof RefValue refVal)
        {
            int adr= refVal.getAddress();
            if(!heapTbl.isDefined(adr))
                throw new MyException("Invalid address accessed!");

            return heapTbl.lookUp(adr);
        }
        else throw new MyException("The expression is not a RefValue!");
    }

    @Override
    public Exp deepCopy()
    {
        return new ReadHeapExp(exp.deepCopy());
    }

    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException
    {
        Type typ=exp.typecheck(typeEnv);
        if(typ instanceof RefType refType)
        {
            return refType.getInner();
        }
        else throw new MyException("the rH argument is not a refType!");
    }

    @Override
    public String toString()
    {
        return "readHeap(" + exp.toString()+")";
    }
}
