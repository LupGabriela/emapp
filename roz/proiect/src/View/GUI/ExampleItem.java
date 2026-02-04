package View.GUI;

import Model.Stmt.IStmt;

public class ExampleItem {
    private final int id;
    private final IStmt program;
    private final String logFile;

    public ExampleItem(int id, IStmt program, String logFile) {
        this.id = id;
        this.program = program;
        this.logFile = logFile;
    }

    public int getId() { return id; }
    public IStmt getProgram() { return program; }
    public String getLogFile() { return logFile; }

    @Override
    public String toString() {
        return program.toString();
    }
}
