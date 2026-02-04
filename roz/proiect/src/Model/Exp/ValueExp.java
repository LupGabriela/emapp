package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public class ValueExp implements Exp {
    private Value e;

    public  ValueExp(Value e) {this.e=e;}

    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        return e;
    }

    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException
    {
        return e.getType();
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(e.deepCopy());
    }

    @Override
    public String toString() {
        return e.toString();
    }
}
