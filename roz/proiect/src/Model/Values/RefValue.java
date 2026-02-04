package Model.Values;

import Model.Types.RefType;
import Model.Types.Type;

public class RefValue implements Value {
    int address;
    Type locationType;

    public int getAddress() {
        return this.address;
    }

    public Type getLocationType(){
        return this.locationType;
    }

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    @Override
    public Type getType()
    {
        return new RefType(locationType);
    }

    @Override
    public String toString()
    {
        return "(" + address + "," + locationType + ")";
    }

    @Override
    public Value deepCopy()
    {
        return new RefValue(address,locationType.deepCopy());
    }
}
