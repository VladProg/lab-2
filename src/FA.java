import java.util.Scanner;

public abstract class FA {
    public final int alphabet;
    public final int states;
    public final int s0;
    public final boolean[] fin;

    public FA(int alphabet, int states, int s0) {
        this.alphabet = alphabet;
        this.states = states;
        this.s0 = s0;
        this.fin = new boolean[states];
    }

    public FA(Scanner in) {
        this(in.nextInt(), in.nextInt(), in.nextInt());
        int cntFin = in.nextInt();
        for (int i = 0; i < cntFin; i++)
            fin[in.nextInt()] = true;
    }

    public abstract DFA toDFA();

    public static boolean equivalent(FA one, FA two) {
        DFA oneD = one.toDFA();
        DFA twoD = two.toDFA();
        int steps = Math.max(oneD.calcUsedStates().length, twoD.calcUsedStates().length);
        return oneD.calcHash(steps) == twoD.calcHash(steps);
    }
}
