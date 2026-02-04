package Model.Exp;

import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;

public class RelationalExp implements Exp{
    private Exp first;
    private Exp second;
    private int operator; // 1-<, 2-<=, 3- ==, 4- !=, 5- >, 6- >=

    public RelationalExp(Exp first, Exp second, String operator) {
        this.first = first;
        this.second=second;
        if(operator.equals("<"))
            this.operator=1;
        else if(operator.equals("<="))
            this.operator=2;
        else if (operator.equals("=="))
            this.operator=3;
        else if(operator.equals("!="))
            this.operator=4;
        else if(operator.equals(">"))
            this.operator=5;
        else if(operator.equals(">="))
            this.operator=6;
    }

    private String getOperatorString(int op)
    {
        if(op==1)
            return "<";
        else if(op==2)
            return "<=";
        else if(op==3)
            return "==";
        else if(op==4)
            return "!=";
        else if (op==5)
            return ">";
        else if(op==6)
            return ">=";
        return "";
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        Value v1 = first.eval(tbl,heapTbl);
        if(v1.getType().equals(new IntType()))
        {
            Value v2=second.eval(tbl,heapTbl);
            if(v2.getType().equals(new IntType()))
            {
                IntValue val1=(IntValue)v1;
                IntValue val2=(IntValue)v2;
                int n1=val1.getVal();
                int n2=val2.getVal();

                if(operator==1) {
                    return new BoolValue(n1<n2);
                }
                else if(operator==2) {
                    return new BoolValue(n1<=n2);
                }
                else if(operator==3) {
                    return new BoolValue(n1==n2);
                }
                else if(operator==4){
                    return new BoolValue(n1!=n2);
                }
                else if(operator==5){
                    return new BoolValue(n1>n2);
                }
                else if(operator==6) {
                    return new BoolValue(n1>=n2);
                }
                else throw new MyException("Invalid operator!");
            }
            else throw new MyException("second expression is invalid!");
        }
        else throw new MyException("first expression is invalid!");
    }

    public Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException
    {
        Type typ1,typ2;
        typ1=first.typecheck(typeEnv);
        typ2=second.typecheck(typeEnv);
        if(typ1.equals(new IntType()))
        {
            if(typ2.equals(new IntType()))
            {
                return new BoolType();
            }
            else throw new MyException("second operand is invalid!");
        }
        else throw new MyException("first operand is invalid!");
    }

    @Override
    public Exp deepCopy()
    {
        return new RelationalExp(first.deepCopy(), second.deepCopy(), getOperatorString(operator));
    }

    @Override
    public String toString()
    {
        return first.toString() + " " + getOperatorString(operator)  + " " + second.toString();
    }

}
