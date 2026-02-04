package Model.Stmt.IOStatements;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIStack;
import Utils.Exceptions.MyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class openRFileStmt implements IStmt {
    private Exp exp;

    public openRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        MyIStack<IStmt> stk=prgState.getStk();
        Value val=exp.eval(prgState.getSymTable(),prgState.getHeapTable());
        if(val.getType().equals(new StringType()))
        {
            StringValue s=(StringValue)val;
            if(prgState.getFileTable().isDefined(s))
                throw new MyException("file already opened!");
            try
            {
                String path=s.getValue();
                BufferedReader reader=new BufferedReader(new FileReader(path));
                prgState.getFileTable().put(s,reader);
            }
            catch (IOException e){
                throw new MyException("file open failed!");
            }
        }
        else throw new MyException("Expression not of string type");

        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type expType=exp.typecheck(typeEnv);
        if(expType.equals(new StringType()))
            return typeEnv;
        else throw new MyException("Expression not of string type");
    }

    @Override
    public IStmt deepCopy() {
        return new openRFileStmt(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" +  exp.toString() + ')';
    }
}
