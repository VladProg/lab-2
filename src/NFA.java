import java.util.Arrays;
import java.util.Scanner;

public class NFA extends FA{
    public final boolean[][][] func;

    public NFA(int alphabet, int states, int s0) {
        super(alphabet, states, s0);
        func = new boolean[states][alphabet][states];
    }

    public NFA(Scanner in) {
        super(in);
        func = new boolean[states][alphabet][states];
        while (in.hasNextInt())
            func[in.nextInt()][in.next().charAt(0) - 'a'][in.nextInt()] = true;
    }

    private static boolean getBit(int value, int bit) {
        return ((value >> bit) & 1) != 0;
    }

    private static int setBit(int value, int bit) {
        return value | (1 << bit);
    }

    @Override
    public DFA toDFA() {
        DFA dfa = new DFA(alphabet, 1 << states, 1 << s0);
        for (int from = 0; from < 1 << states; from++) {
            for (int x = 0; x < states; x++)
                if (getBit(from, x) && fin[x])
                    dfa.fin[from] = true;
            for (int ch = 0; ch < alphabet; ch++) {
                int to = 0;
                for (int x = 0; x < states; x++)
                    for (int y = 0; y < states; y++)
                        if (getBit(from, x) && func[x][ch][y])
                            to = setBit(to, y);
                dfa.func[from][ch] = to;
            }
        }
        return dfa;
    }
}
