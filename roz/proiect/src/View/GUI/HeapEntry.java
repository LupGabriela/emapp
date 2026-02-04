package View.GUI;

public class HeapEntry {
    private final Integer address;
    private final String value;

    public HeapEntry(Integer address, String value) {
        this.address = address;
        this.value = value;
    }

    public Integer getAddress() { return address; }
    public String getValue() { return value; }
}
