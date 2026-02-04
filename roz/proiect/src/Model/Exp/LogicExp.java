package Model.Exp;

import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public class LogicExp implements Exp{
    Exp e1;
    Exp e2;
    int op;

    public LogicExp(String operator, Exp e1, Exp e2)
    {
        this.e1=e1;
        this.e2=e2;
        if(operator.equals("&&"))
            this.op=1;
        else if(operator.equals("||"))
            this.op=2;
    }

    private String getStringOperator(int op)
    {
        if(op==1)
            return "&&";
        if(op==2)
            return "||";
        return "";
    }

    @Override
    public Exp deepCopy()
    {
        return new LogicExp(getStringOperator(op), e1.deepCopy(), e2.deepCopy());
    }


    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        Value v1,v2;
        v1=e1.eval(tbl,heapTbl);
        if(v1.getType().equals(new BoolType()))
        {
            v2=e2.eval(tbl,heapTbl);
            if(v2.getType().equals(new BoolType()))
            {
                BoolValue b1=(BoolValue)v1;
                BoolValue b2=(BoolValue)v2;
                boolean n1,n2;
                n1=b1.getVal();
                n2=b2.getVal();
                if(op==1) return new BoolValue(n1 && n2);
                if(op==2) return new BoolValue(n1 || n2);

                throw new MyException("Invalid operand");

            }
            else throw new MyException("Second operand is not a bool!");
        }
        else throw new MyException("First operand is not bool!");
    }

    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException
    {
        Type typ1,typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if(typ1.equals(new IntType()))
        {
            if(typ2.equals(new IntType()))
            {
                return new BoolType();
            }
            else throw new MyException("second operand is not an integer!");
        }
        else throw new MyException("first operand is not an integer!");
    }

    @Override
    public String toString()
    {
        return e1.toString() + " " + getStringOperator(op) + " " + e2.toString();
    }

}
