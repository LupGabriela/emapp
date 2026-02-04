package Model.Values;
import Model.Types.*;

public interface Value {
    Type getType();
    Value deepCopy();
    boolean equals(Object other);
}
