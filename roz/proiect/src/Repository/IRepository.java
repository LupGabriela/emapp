package Repository;

import Model.PrgState.PrgState;
import Utils.Exceptions.MyException;

import java.util.List;

public interface IRepository {
    //PrgState getCrtPrg();
    void add(PrgState prgState);
    void logPrgStateExec(PrgState prg) throws MyException;
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);

}
