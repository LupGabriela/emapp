package Model.Stmt.HeapStatements;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class WriteHeapStmt implements IStmt {
    private String name;
    private Exp exp;

    public WriteHeapStmt(String name, Exp exp) {
        this.name = name;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "writeHeap(" +  name + ", " + exp.toString() + ")";
    }

    @Override
    public IStmt deepCopy()
    {
        return new WriteHeapStmt(name, exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException
    {
        Value val=prgState.getSymTable().lookUp(name);
        if(val==null)
            throw new MyException("Variable not declared!");

        if(val instanceof RefValue refV)
        {
            if(!prgState.getHeapTable().isDefined(refV.getAddress()))
                throw new MyException("Memory wasn't allocated!");

            Value expValue=exp.eval(prgState.getSymTable(),prgState.getHeapTable());
            if(!expValue.getType().equals(refV.getLocationType()))
                throw new MyException("Inner type mismatch!");

            prgState.getHeapTable().update(refV.getAddress(),expValue);
        }
        else throw new MyException("The value is not a ref value, thus the type is not ref type!");

        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeVar=typeEnv.lookUp(name);
        Type typeExp=exp.typecheck(typeEnv);
        if(typeVar instanceof RefType refTypeVar)
        {
            if(refTypeVar.getInner().equals(typeExp))
                return typeEnv;
            else throw new MyException("wH stmt: LHS and RHS don't match!");
        }
        else  throw new MyException("First operand is not of the type RefType!");
    }

}
