import java.util.*;

public class DFA extends FA{
    public final int[][] func;

    public DFA(int alphabet, int states, int s0) {
        super(alphabet, states, s0);
        func = new int[states][alphabet];
        for (int[] row: func)
            Arrays.fill(row, -1);
    }

    public DFA(Scanner in) {
        super(in);
        func = new int[states][alphabet];
        for (int[] row: func)
            Arrays.fill(row, -1);
        while (in.hasNextInt())
            func[in.nextInt()][in.next().charAt(0) - 'a'] = in.nextInt();
    }

    @Override
    public DFA toDFA() {
        return this;
    }

    private static void calcReachable(int s, boolean[] reachable, int[][] graph) {
        if (reachable[s])
            return;
        reachable[s] = true;
        for (int to: graph[s])
            if (to != -1)
                calcReachable(to, reachable, graph);
    }

    private static void calcReachable(int s, boolean[] reachable, List<Integer>[] graph) {
        if (reachable[s])
            return;
        reachable[s] = true;
        for (int to: graph[s])
            if (to != -1)
                calcReachable(to, reachable, graph);
    }

    private int[] usedStates;

    public int[] calcUsedStates() {
        if (usedStates != null)
            return usedStates;
        boolean[] isReachableFromStart = new boolean[states];
        calcReachable(s0, isReachableFromStart, func);
        List<Integer>[] inv = new List[states];
        for (int s = 0; s < states; s++)
            inv[s] = new ArrayList<>();
        for (int s = 0; s < states; s++)
            for (int to: func[s])
                if (to != -1)
                    inv[to].add(s);
        boolean[] isFinalReachable = new boolean[states];
        for (int s = 0; s < states; s++)
            calcReachable(s, isFinalReachable, inv);
        List<Integer> usedStatesList = new ArrayList<>();
        for (int s = 0; s < states; s++)
            if(isReachableFromStart[s] && isFinalReachable[s])
                usedStatesList.add(s);
        return usedStates = usedStatesList.stream().mapToInt(i->i).toArray();
    }

    private static final int HASH_FINAL = 123456789;
    private static final int HASH_EMPTY = 1234567;
    private static final long HASH_BASE = 12345;
    private static final long HASH_MOD = 1_000_000_007;

    public int calcHash(int steps) {
        calcUsedStates();
        int[] hashes = new int[states];
        for (int st = 0; st < states; st++)
            hashes[st] = fin[st] ? HASH_FINAL : HASH_EMPTY;
        for (int i = 0; i < steps; i++) {
            int[] newHashes = new int[states];
            Arrays.fill(newHashes, HASH_EMPTY);
            for (int st: usedStates)
            {
                int hash = hashes[st];
                boolean is_empty = hash == HASH_EMPTY;
                for (int to: func[st]) {
                    int to_hash = to == -1 ? HASH_EMPTY : hashes[to];
                    hash = (int)((HASH_BASE * hash + to_hash) % HASH_MOD);
                    if (to_hash != HASH_EMPTY)
                        is_empty = false;
                }
                newHashes[st] = is_empty ? HASH_EMPTY : hash;
            }
            hashes = newHashes;
        }
        return hashes[s0];
    }
}