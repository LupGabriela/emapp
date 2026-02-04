package Model.Stmt;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Types.BoolType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class WhileStmt implements IStmt{
    private Exp condition;
    IStmt stmt;

    public WhileStmt(Exp condition, IStmt stmt){
        this.stmt=stmt;
        this.condition=condition;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        Value val=condition.eval(prgState.getSymTable(),prgState.getHeapTable());
        if(val.getType().equals(new BoolType())){
            BoolValue bValue=(BoolValue)val;
            if(bValue.getVal())
            {
                prgState.getStk().push(this);
                prgState.getStk().push(stmt);
            }
        }
        else throw new MyException("Invalid condition!");
        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type expType=condition.typecheck(typeEnv);
        if(expType.equals(new BoolType()))
        {
            stmt.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else throw new MyException("Invalid condition!");
    }

    @Override
    public IStmt deepCopy()
    {
        return new WhileStmt(condition.deepCopy(), stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "While(" + condition.toString() + "){" + stmt.toString() + "}";
    }
}
