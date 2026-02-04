import Model.Stmt.IStmt;
import View.Commands.ExitCommand;
import View.Commands.RunExample;
import View.TextMenu;
import View.Examples;

public class Interpreter {
    public static void main(String[] args)
    {
        IStmt ex1 = Examples.example1();
        IStmt ex2 = Examples.example2();
        IStmt ex3 = Examples.example3();
        IStmt ex4 = Examples.example4();
        IStmt ex5 = Examples.example5();
        IStmt ex6 = Examples.example6();
        IStmt ex7 = Examples.example7();
        IStmt ex8 = Examples.example8();
        IStmt ex9 = Examples.example9();
        IStmt ex10 = Examples.example10();
        IStmt ex11 = Examples.example11();
        IStmt ex12 = Examples.example12();
        IStmt ex13 = Examples.example13();


        TextMenu menu=new TextMenu();
        menu.addCommand(new ExitCommand("0","exit"));

        menu.addCommand(new RunExample("1", ex1.toString(), ex1, "ex1.txt"));
        menu.addCommand(new RunExample("2", ex2.toString(), ex2, "ex2.txt"));
        menu.addCommand(new RunExample("3", ex3.toString(), ex3, "ex3.txt"));
        menu.addCommand(new RunExample("4", ex4.toString(), ex4, "ex4.txt"));
        menu.addCommand(new RunExample("5", ex5.toString(), ex5, "ex5.txt"));
        menu.addCommand(new RunExample("6", ex6.toString(), ex6, "ex6.txt"));
        menu.addCommand(new RunExample("7", ex7.toString(), ex7, "ex7.txt"));
        menu.addCommand(new RunExample("8", ex8.toString(), ex8, "ex8.txt"));
        menu.addCommand(new RunExample("9", ex9.toString(), ex9, "ex9.txt"));
        menu.addCommand(new RunExample("10", ex10.toString(), ex10, "ex10.txt"));
        menu.addCommand(new RunExample("11", ex11.toString(), ex11, "ex11.txt"));
        menu.addCommand(new RunExample("12", ex12.toString(), ex12, "ex12.txt"));
        menu.addCommand(new RunExample("13", ex13.toString(), ex13, "ex13.txt"));
        menu.show();
    }
}
