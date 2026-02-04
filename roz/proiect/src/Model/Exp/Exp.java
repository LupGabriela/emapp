package Model.Exp;

import Model.Types.Type;
import Model.Values.*;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heapTbl) throws MyException;
    Exp deepCopy();

    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
