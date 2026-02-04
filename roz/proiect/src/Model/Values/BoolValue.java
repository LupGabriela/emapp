package Model.Values;

import Model.Types.BoolType;
import Model.Types.Type;

public class BoolValue implements Value {
    private boolean val;
    public BoolValue(boolean val) {
        this.val = val;
    }

    public boolean getVal()
    {
        return val;
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(val);
    }

    @Override
    public String toString() {
        return Boolean.toString(val);
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof BoolValue otherBoolValue)
            return this.val==otherBoolValue.val;
        return false;
    }

}
