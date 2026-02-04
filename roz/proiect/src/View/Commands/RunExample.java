package View.Commands;


import Controller.Controller;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.Type;
import Repository.IRepository;
import Repository.Repository;
import Utils.Collections.MyDictionary;
import Utils.Collections.MyHeap;
import Utils.Collections.MyList;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;

public class RunExample extends Command {
    private IStmt ex;
    private String logFile;

    public RunExample(String key, String description, IStmt ex, String logFile) {
        super(key, description);
        this.ex=ex;
        this.logFile=logFile;
    }

    @Override
    public void execute() {

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap<>(),
                ex
        );

        IRepository repo = new Repository(state, logFile);
        Controller ctr = new Controller(repo);

        try{
            ex.typecheck(new MyDictionary<>());

            System.out.println("Typechecker done!");

            try{
                ctr.allStep();
                System.out.println("All steps have been executed!");
            }
            catch(MyException e){
                System.out.println(e.getMessage());
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        catch(MyException e){
            System.out.println(e.getMessage());
        }


    }
}
