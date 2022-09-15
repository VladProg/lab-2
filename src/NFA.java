import java.util.Scanner;

public class NFA extends FA{

    private static int setBit(int value, int bit) {
        return value | (1 << bit);
    }

    private static boolean getBit(int value, int bit) {
        return ((value >> bit) & 1) != 0;
    }

    private final int[][] func;

    public void setFunc(int from, char ch, int to) {
        func[from][ch - 'a'] = setBit(func[from][ch - 'a'], to);
    }

    public boolean getFunc(int from, char ch, int to) {
        return getBit(func[from][ch - 'a'], to);
    }

    public NFA(int alphabet, int states, int s0) {
        super(alphabet, states, s0);
        func = new int[states][alphabet];
    }

    public NFA(Scanner in) {
        super(in);
        func = new int[states][alphabet];
        while (in.hasNextInt())
            setFunc(in.nextInt(), in.next().charAt(0), in.nextInt());
    }

    @Override
    public DFA toDFA() {
        DFA dfa = new DFA(alphabet, 1 << states, 1 << s0);
        for (int from = 0; from < 1 << states; from++) {
            for (int s = 0; s < states; s++)
                if (getBit(from, s) && fin[s])
                    dfa.fin[from] = true;
            for (int ch = 0; ch < alphabet; ch++) {
                int to = 0;
                for (int s = 0; s < states; s++)
                    if (getBit(from, s))
                        to |= func[s][ch];
                dfa.func[from][ch] = to;
            }
        }
        return dfa;
    }
}
