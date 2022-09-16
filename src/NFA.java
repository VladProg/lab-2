import java.util.Scanner;

public class NFA extends FA{

    private static boolean getBit(int value, int bit) {
        return ((value >> bit) & 1) != 0;
    }
    private static int addBit(int value, int bit) {
        return value | (1 << bit);
    }
    private static int delBit(int value, int bit) {
        return value & ~(1 << bit);
    }

    @Override
    public boolean getFunction(int from, char ch, int to) {
        return getBit(function[from][ch - 'a'], to);
    }
    @Override
    public void addFunction(int from, char ch, int to) {
        function[from][ch - 'a'] = addBit(function[from][ch - 'a'], to);
    }
    @Override
    public void delFunction(int from, char ch, int to) {
        function[from][ch - 'a'] = delBit(function[from][ch - 'a'], to);
    }

    public NFA(int alphabet, int states, int s0) {
        super(alphabet, states, s0);
    }
    public NFA(Scanner in) {
        super(in);
    }

    @Override
    public DFA toDFA() {
        DFA dfa = new DFA(alphabet, 1 << states, 1 << s0);
        for (int from = 0; from < 1 << states; from++) {
            for (int s = 0; s < states; s++)
                if (getBit(from, s) && getFinal(s)) {
                    dfa.addFinal(from);
                    break;
                }
            for (int ch = 0; ch < alphabet; ch++) {
                int to = 0;
                for (int s = 0; s < states; s++)
                    if (getBit(from, s))
                        to |= function[s][ch];
                dfa.addFunction(from, (char)('a' + ch), to);
            }
        }
        return dfa;
    }
}
