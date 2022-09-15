import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

public class Generator {
    public static void main(String[] args) throws FileNotFoundException {
        int alphabet = Integer.parseInt(args[0]);
        int states = Integer.parseInt(args[1]);
        int fin = Integer.parseInt(args[2]);
        int func = Integer.parseInt(args[3]);

        PrintStream out = new PrintStream(args[4]);

        Random random = new Random();

        out.println(alphabet);
        out.println(states);
        out.println(random.nextInt(states));

        out.print(fin);
        assert fin <= states;
        boolean[] finUsed = new boolean[states];
        for (int i = 0; i < fin; i++) {
            int cur;
            do
                cur = random.nextInt(states);
            while (finUsed[cur]);
            finUsed[cur] = true;
            out.print(" " + cur);
        }
        out.println();

        for (int i = 0; i < func; i++)
            out.println(random.nextInt(states) + " " + (char)('a' + random.nextInt(alphabet)) + " " + random.nextInt(states));

        out.close();
    }
}
