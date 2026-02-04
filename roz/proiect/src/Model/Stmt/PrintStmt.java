package Model.Stmt;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class PrintStmt implements IStmt {
    Exp exp;
    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public IStmt deepCopy()
    {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        Value val=exp.eval(prgState.getSymTable(),prgState.getHeapTable());
        prgState.getOut().add(val);
        return null;
    }

    public MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}
