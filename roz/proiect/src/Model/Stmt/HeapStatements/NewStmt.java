package Model.Stmt.HeapStatements;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;
import Utils.Collections.MyHeap;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class NewStmt implements IStmt{
    String name;
    Exp exp;

    public NewStmt(String name,Exp exp){
        this.name=name;
        this.exp=exp;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        Value v=prgState.getSymTable().lookUp(name);
        if(v==null) {
            throw new MyException("Symbol "+name+" not found");
        }

        if(v instanceof RefValue refValue){
            Value val=exp.eval(prgState.getSymTable(),prgState.getHeapTable());
            if(val.getType().equals(refValue.getLocationType())){
                int address=prgState.getHeapTable().put(val);
                prgState.getSymTable().put(name,new RefValue(address,val.getType()));
            }
            else throw new MyException("Inner type does not match");
        }
        else throw new MyException("Variable "+name+" is not of refType");
        return null;
    }

    public MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeVar=typeEnv.lookUp(name);
        Type typeExp=exp.typecheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp))){
            return typeEnv;
        }
        else throw new MyException("New stmt: LHS and RHS have different types!");
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(name,exp.deepCopy());
    }

    @Override
    public String toString()
    {
        return "new(" + name + ", " + exp + ")";
    }
}
