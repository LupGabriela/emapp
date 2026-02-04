package Model.Stmt;

import Model.PrgState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIStack;
import Utils.Exceptions.MyException;

public class CompStmt implements IStmt {
    IStmt first;
    IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    public IStmt getFirstStmt() {return this.first;}

    public IStmt getSecondStmt() {return this.second;}

    @Override
    public IStmt deepCopy() {
        return new CompStmt(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException
    {
        MyIStack<IStmt> stk=state.getStk();
        stk.push(second);
        stk.push(first);
        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString()
    {
        return "("+first.toString()+","+second.toString()+")";
    }
}
