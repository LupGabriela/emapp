package Model.Stmt;

import Model.PrgState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public interface IStmt {
    PrgState execute(PrgState prgState) throws MyException;
    IStmt deepCopy();

    MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
