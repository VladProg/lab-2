import java.util.Scanner;

public abstract class FA {

    public final int alphabet;
    public final int states;
    public final int s0;

    private final boolean[] fin;
    public boolean getFinal(int st) {
        return fin[st];
    }
    public void addFinal(int st) {
        fin[st] = true;
    }
    public void delFinal(int st) {
        fin[st] = true;
    }

    protected final int[][] function;
    public abstract boolean getFunction(int from, char ch, int to);
    public abstract void addFunction(int from, char ch, int to);
    public abstract void delFunction(int from, char ch, int to);

    public FA(int alphabet, int states, int s0) {
        this.alphabet = alphabet;
        this.states = states;
        this.s0 = s0;
        fin = new boolean[states];
        function = new int[states][alphabet];
    }

    public FA(Scanner in) {
        this(in.nextInt(), in.nextInt(), in.nextInt());
        int cntFinal = in.nextInt();
        for (int i = 0; i < cntFinal; i++)
            addFinal(in.nextInt());
        while (in.hasNextInt())
            addFunction(in.nextInt(), in.next().charAt(0), in.nextInt());
    }

    public abstract DFA toDFA();

    public static boolean equivalent(FA one, FA two) {
        return DFA.equivalent(one.toDFA(), two.toDFA());
    }
}
