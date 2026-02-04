package Model.Stmt;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Collections.MyIStack;
import Utils.Exceptions.MyException;

public class AssignStmt implements IStmt{
    String id;
    Exp exp;
    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public IStmt deepCopy()
    {
        return new AssignStmt(this.id, this.exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException
    {
        MyIStack<IStmt> stk=state.getStk();
        MyIDictionary<String, Value> symTbl=state.getSymTable();
        MyIHeap<Value> heapTable=state.getHeapTable();

        if(symTbl.isDefined(id))
        {
            Value val=exp.eval(symTbl,heapTable);
            Type typId=(symTbl.lookUp(id)).getType();
            if(val.getType().equals(typId))
            {
                symTbl.put(id,val);
            }
            else throw new MyException("declared type of variable: "+ id +
                    " and type of the assigned do not match!");
        }
        else throw new MyException("the used variable: " + id + " was never declared before!");

        return null;
    }

    public MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeVar=typeEnv.lookUp(id);
        Type typeExp=exp.typecheck(typeEnv);
        if(typeVar.equals(typeExp))
            return typeEnv;
        else throw new MyException("Assignment: the LHS and RHS have different types!");
    }

    @Override
    public String toString() {
        return id+ " = " + exp.toString();
    }
}
