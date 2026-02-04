package Model.Stmt.IOStatements;

import Model.Exp.Exp;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.IntType;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Exceptions.MyException;

import java.io.BufferedReader;

public class readFileStmt implements IStmt {
    private Exp exp;
    private String varName;

    public readFileStmt(Exp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState prgState) throws MyException {

        if (!prgState.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Type typ = prgState.getSymTable().lookUp(varName).getType();

        if (!typ.equals(new IntType()))
            throw new MyException("Variable " + varName + " is not of type Int");

        Value val = exp.eval(prgState.getSymTable(), prgState.getHeapTable());
        if (!val.getType().equals(new StringType()))
            throw new MyException("Expression " + exp.toString() + " is not of type String");

        StringValue v = (StringValue) val;
        BufferedReader br = prgState.getFileTable().lookUp(v);

        try {
            String line = br.readLine();
            if (line == null)
                prgState.getSymTable().update(varName, new IntValue(0));
            else {
                prgState.getSymTable().update(varName, new IntValue(Integer.parseInt(line)));
            }

        } catch (Exception e) {
            throw new MyException("Error reading file " + v.toString());
        }

        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException
    {
        Type typeExp=exp.typecheck(typeEnv);
        if(typeExp.equals(new StringType()))
        {
            if(typeEnv.lookUp(varName).equals(new IntType()))
            {
                return typeEnv;
            }
            else throw new MyException("Variable " + varName + " is not of type Int");
        }
        else throw new MyException("The file name is not of String Type!");
    }

    @Override
    public IStmt deepCopy() {
        return new readFileStmt(exp.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + "," + varName + ")";
    }

}
