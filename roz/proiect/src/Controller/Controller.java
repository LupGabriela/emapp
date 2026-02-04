package Controller;
import Model.PrgState.PrgState;
import Model.Values.RefValue;
import Model.Values.Value;
import Repository.IRepository;
import Utils.Collections.MyHeap;
import Utils.Exceptions.MyException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    /*
    public PrgState oneStep(PrgState state) throws MyException {
        MyIStack<IStmt> stk=state.getStk();
        if(stk.isEmpty()) throw new MyException("prgstate stack is empty");;
        IStmt crtStmt= stk.pop();
        return crtStmt.execute(state);
    }
    */

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p->p.isNotCompleted())
                .collect(Collectors.toList());
    }


    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException, InterruptedException {

        ensureExecutor();

        MyException[] err = new MyException[1];

        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                err[0] = e;
            }
        });
        if (err[0] != null) throw err[0];

        List<PrgState> activePrgs = prgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());

        // build callables only for active programs
        List<Callable<PrgState>> callList = activePrgs.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());

        List<MyException> exceptions = new ArrayList<>();


        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        exceptions.add(new MyException("Thread was interrupted"));
                        return null;
                    } catch (ExecutionException e) {
                        Throwable cause = e.getCause();

                        if (cause instanceof MyException me && "stack is empty".equals(me.getMessage())) {
                            return null;
                        } else if (cause instanceof MyException me) {
                            exceptions.add(me);
                        } else {
                            exceptions.add(new MyException("Exception in oneStep: " + cause.getMessage()));
                        }
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!exceptions.isEmpty()) throw exceptions.get(0);

        prgList.addAll(newPrgList);

        // log again
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                err[0] = e;
            }
        });
        if (err[0] != null) throw err[0];

        repository.setPrgList(prgList);
    }

    public void allStep() throws MyException,InterruptedException
    {
        executor= Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while(prgList.size()>0){
            conservativeGarbageCollector(prgList);
            oneStepForAllPrg(prgList);
            prgList=removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);

        MyHeap.resetAddress();

    }

    /*
    public void allStep() throws MyException
    {
        PrgState prg=repository.getCrtPrg();
        MyHeap.resetAddress();

        repository.logPrgStateExec();


        while(!prg.getStk().isEmpty())
        {

            oneStep(prg);

            repository.logPrgStateExec();
            prg.getHeapTable().setContent(safeGarbageCollector(
                   getAddrFromSymTable(prg.getSymTable().getContent().values()),
                   getAddrFromHeap(prg.getHeapTable().getContent().values()),
                   prg.getHeapTable().getContent()
            ));

            repository.logPrgStateExec();
        }
        prg.restoreOriginal();
    }
    */

    public void conservativeGarbageCollector(List<PrgState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddrFromSymTable(p.getSymTable().getContent().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> p.getHeapTable().setContent((HashMap<Integer, Value>) safeGarbageCollector(symTableAddresses, getAddrFromHeap(p.getHeapTable().getContent().values()), p.getHeapTable().getContent())));
    }

    Map<Integer,Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapTableAddr, Map<Integer,Value> heap)
    {

        return heap.entrySet().stream()
                .filter(e-> (symTableAddr.contains(e.getKey())) || heapTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(Collection<Value> symTableValues)
    {
        return symTableValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v-> {RefValue v1=(RefValue)v; return v1.getAddress(); })
                .collect(Collectors.toList());
    }

    List<Integer> getAddrFromHeap(Collection<Value> heapTableValues)
    {
        return heapTableValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v->{RefValue v1=(RefValue)v; return v1.getAddress(); })
                .collect(Collectors.toList());
    }


    public List<PrgState> getPrgList() {
        return repository.getPrgList();
    }

    public void shutdownExecutor() {
        if (executor != null) executor.shutdownNow();
    }

    private void ensureExecutor() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(2);
        }
    }

}
