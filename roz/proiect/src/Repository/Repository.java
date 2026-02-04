package Repository;

import Model.PrgState.PrgState;
import Utils.Exceptions.MyException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private List<PrgState> programs;
    private String file;

    public Repository(PrgState prgState, String file) {
        this.file = file=file;
        this.programs = new ArrayList<>();
        this.programs.add(prgState);
    }

    /*
    @Override
    public PrgState getCrtPrg() {
        return this.programs.getFirst();
    }
    */



    @Override
    public List<PrgState> getPrgList() {
        return this.programs;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.programs = prgList;
    }

    @Override
    public void add(PrgState prgState) {
        this.programs.add(prgState);
    }

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException
    {
        try {
            PrintWriter logFile=new
                    PrintWriter(new BufferedWriter
                    (new FileWriter(file,true)));
            logFile.println(prg.toString());
            logFile.close();
        }
        catch(IOException e)
        {
            throw new MyException("Logging failed: " + e.getMessage());
        }
    }
}
