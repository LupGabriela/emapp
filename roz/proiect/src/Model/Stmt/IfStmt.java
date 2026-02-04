package Model.Stmt;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Types.BoolType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Collections.MyIStack;
import Utils.Exceptions.MyException;

public class IfStmt implements IStmt {

    Exp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public IStmt deepCopy()
    {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }


    @Override
    public PrgState execute(PrgState state) throws MyException
    {
        MyIStack<IStmt> stk=state.getStk();
        MyIDictionary<String, Value> symTable=state.getSymTable();
        MyIHeap<Value> heapTable=state.getHeapTable();

        Value condition=exp.eval(symTable,heapTable);
        if(!condition.getType().equals(new BoolType())) {
            throw new MyException("Condition is not boolean");
        }

        BoolValue boolCond=(BoolValue)condition;
        if(boolCond.getVal())
            stk.push(thenS);
        else stk.push(elseS);
        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeExp=exp.typecheck(typeEnv);
        if(typeExp.equals(new BoolType())) {
            thenS.typecheck(typeEnv.deepCopy());
            elseS.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else throw new MyException("Condition is not boolean");
    }

    @Override
    public String toString()
    {
        return "(IF(" + exp.toString() + ")THEN("+thenS.toString()
                +")ELSE("+ elseS.toString()+"))";
    }

}
