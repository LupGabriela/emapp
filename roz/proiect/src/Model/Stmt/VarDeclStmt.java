package Model.Stmt;

import Model.PrgState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

public class VarDeclStmt implements IStmt{
    String name;
    Type type;

    public VarDeclStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

    @Override
    public IStmt deepCopy()
    {
        return new VarDeclStmt(name, type.deepCopy());
    }

    public MyIDictionary<String,Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        typeEnv.put(name, type);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException
    {
        MyIDictionary<String, Value> symTable=prgState.getSymTable();
        if(symTable.isDefined(this.name))
                throw new MyException("Variable already exists!");
        symTable.put(name,type.defaultValue());
        return null;
    }
}
