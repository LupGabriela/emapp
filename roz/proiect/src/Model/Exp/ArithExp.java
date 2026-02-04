package Model.Exp;

import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDictionary;
import Utils.Collections.MyIHeap;
import Utils.Exceptions.MyException;
import Model.Types.*;

public class ArithExp implements Exp {
    Exp e1;
    Exp e2;
    int op; //1-plus, 2-minus, 3-star, 4-divide

    public ArithExp(char c, Exp exp1, Exp exp2)
    {
        this.e1 = exp1;
        this.e2 = exp2;
        if(c=='+')
            this.op=1;
        else if(c=='-')
            this.op=2;
        else if(c=='*')
            this.op=3;
        else if(c=='/')
            this.op=4;
    }

    private String getStringOperator(int op)
    {
        if(op==1)
            return "+";
        if(op==2)
            return "-";
        if(op==3)
            return "*";
        if(op==4)
            return "/";

        return "";
    }

    @Override
    public Exp deepCopy()
    {
        return new ArithExp(getStringOperator(op).charAt(0),e1.deepCopy(),e2.deepCopy());
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
                return new IntType();
            }
            else throw new MyException("second operand is not an integer!");
        }
        else throw new MyException("first operand is not an integer!");
    }

    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> heapTbl) throws MyException
    {
        Value v1,v2;
        v1=e1.eval(tbl,heapTbl);
        if(v1.getType().equals(new IntType()))
        {
            v2=e2.eval(tbl,heapTbl);
            if(v2.getType().equals(new IntType()))
            {
                IntValue i1=(IntValue)v1;
                IntValue i2=(IntValue)v2;
                int n1,n2;
                n1=i1.getVal();
                n2=i2.getVal();

                if(op==1) return new IntValue(n1+n2);
                if(op==2) return new IntValue(n1-n2);
                if(op==3) return new IntValue(n1*n2);
                if(op==4){
                    if(n2==0) throw new MyException("Division by zero!");
                    else return new IntValue(n1/n2);
                }

                throw new MyException("Invalid arithmetic operator!"); //so I don't return null at the end
            }
            else throw new MyException("Second operand is not an integer!");
        }
        else throw new MyException("First operand is not an integer!");

    }

    @Override
    public String toString(){
        return e1.toString()+getStringOperator(op)+e2.toString();
    }

}
