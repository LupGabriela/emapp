package Model.Stmt;

import Model.PrgState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.*;
import Utils.Exceptions.MyException;

import java.util.Map;

public class forkStmt implements IStmt{

    private IStmt statement;

    public forkStmt(IStmt statement){
        this.statement=statement;
    }

    @Override
    public PrgState execute(PrgState state) {

        MyIDictionary<String, Value> newSymTable=new MyDictionary<>();
        for(Map.Entry<String,Value> entry: state.getSymTable().getContent().entrySet()){
            newSymTable.put(entry.getKey(),entry.getValue());
        }
        MyIStack<IStmt> myStack=new MyStack<>();

        return new PrgState(myStack,newSymTable,state.getOut(),state.getFileTable(),state.getHeapTable(),statement);
    }

    @Override
    public IStmt deepCopy()
    {
        return new forkStmt(statement.deepCopy());
    }

    @Override
    public String toString()
    {
        return String.format("fork(%s)",statement.toString());
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
       statement.typecheck(typeEnv.deepCopy());
       return typeEnv;
    }
}
