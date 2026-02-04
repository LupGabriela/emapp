package Model.Stmt.IOStatements;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

import java.io.BufferedReader;

public class closeRFileStmt implements IStmt {
    Exp exp;

    public closeRFileStmt(Exp exp)
    {
        this.exp=exp;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException
    {
        Value val=exp.eval(prgState.getSymTable(),prgState.getHeapTable());
        if(!val.getType().equals(new StringType()))
            throw new MyException("Wrong type of expression");

        StringValue s=((StringValue)val);
        BufferedReader br=prgState.getFileTable().lookUp(s);
        if(br==null)
            throw new MyException("File not found");

        try{
            br.close();
            prgState.getFileTable().remove(s);
        }
        catch(Exception e){
            throw new MyException("Error closing the file");
        }

        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeExp=exp.typecheck(typeEnv);
        if(typeExp.equals(new StringType()))
        {
            return typeEnv;
        }
        else throw new MyException("Expression not of string type");
    }

    @Override
    public IStmt deepCopy()
    {
        return new closeRFileStmt(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }
}
