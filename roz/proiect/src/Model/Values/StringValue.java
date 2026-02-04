package Model.Values;

import Model.Types.StringType;
import Model.Types.Type;

public class StringValue implements Value {
    private String val;

    public StringValue(String val) {
        this.val = val;
    }

    @Override
    public Value deepCopy() {
        return new StringValue(val);
    }

    @Override
    public Type getType()
    {
        return new StringType();
    }

    @Override
    public String toString() {
        return val;
    }

    public String getValue() {
        return val;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof StringValue stringVal) {
            return this.val.equals(stringVal.val);
        }
        return false;
    }
}
