package Model.Values;
import Model.Types.*;

public class IntValue implements Value {
    private int val;

    public IntValue(int v) {
        val=v;
    }

    public int getVal() {
        return val;
    }

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }

    @Override
    public String toString()
    {
        return Integer.toString(val);
    }

    @Override
    public Type getType()
    {
        return new IntType();
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof IntValue intV){
            return this.val==intV.val;
        }
        return false;
    }

}
