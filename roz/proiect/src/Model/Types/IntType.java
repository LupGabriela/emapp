package Model.Types;

import Model.Values.IntValue;
import Model.Values.Value;

public class IntType implements Type {

    @Override
    public boolean equals(Object another)
    {
        if(another instanceof IntType)
            return true;
        return false;
    }

    @Override
    public Value defaultValue()
    {
        return new IntValue(0);
    }


    @Override
    public String toString()
    {
        return "int";
    }

    @Override
    public Type deepCopy()
    {
        return new IntType();
    }
}
