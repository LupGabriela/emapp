package Model.Stmt;

import Model.PrgState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class NopStmt implements IStmt{
    public NopStmt()
    {

    }

    @Override
    public IStmt deepCopy()
    {
        return new NopStmt();
    }

    @Override
    public PrgState execute(PrgState prgState)
    {
        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
