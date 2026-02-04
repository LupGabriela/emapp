package View.GUI;

public class SymEntry {
    private final String name;
    private final String value;

    public SymEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public String getValue() { return value; }
}
