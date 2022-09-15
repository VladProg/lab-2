import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lab2 {
    public static void main(String[] args) throws FileNotFoundException {
        FA one;
        try (Scanner in = new Scanner(new File(args[0]))) {
            one = new NFA(in);
        }
        FA two;
        try (Scanner in = new Scanner(new File(args[1]))) {
            two = new NFA(in);
        }
        System.out.printf("FAs '%s' and '%s' are %sequivalent\n", args[0], args[1], FA.equivalent(one, two) ? "" : "not ");
    }
}
