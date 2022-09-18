import java.util.*;

public class DFA extends FA{

    @Override
    public int[][] initFunction() {
        int[][] func = new int[states][alphabet];
        for (int[] row: func)
            Arrays.fill(row, -1);
        return func;
    }
    public int getFunction(int from, char ch) {
        return function[from][ch - 'a'];
    }
    @Override
    public boolean getFunction(int from, char ch, int to) {
        return getFunction(from, ch) == to;
    }
    @Override
    public void addFunction(int from, char ch, int to) {
        if (function[from][ch - 'a'] != to && function[from][ch - 'a'] != -1)
            throw new IllegalArgumentException(String.format("Adding function (%d, %c) -> %d to DFA is impossible " +
                    "because it has another function (%d, %c) -> %d",
                    from, ch, to, from, ch, function[from][ch - 'a']));
        function[from][ch - 'a'] = to;
    }

    public DFA(int alphabet, int states, int s0) {
        super(alphabet, states, s0);
    }
    public DFA(Scanner in) {
        super(in);
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

    private void calcUsedStates() {
        if (usedStates != null)
            return;
        boolean[] isReachableFromStart = new boolean[states];
        calcReachable(s0, isReachableFromStart, function);
        List<Integer>[] inv = new List[states];
        for (int s = 0; s < states; s++)
            inv[s] = new ArrayList<>();
        for (int s = 0; s < states; s++)
            for (int to: function[s])
                if (to != -1)
                    inv[to].add(s);
        boolean[] isFinalReachable = new boolean[states];
        for (int s = 0; s < states; s++)
            calcReachable(s, isFinalReachable, inv);
        List<Integer> usedStatesList = new ArrayList<>();
        for (int s = 0; s < states; s++)
            if(isReachableFromStart[s] && isFinalReachable[s])
                usedStatesList.add(s);
        usedStates = usedStatesList.stream().mapToInt(i->i).toArray();
    }

    private static final int HASH_FINAL = 123456789;
    private static final int HASH_EMPTY = 1234567;
    private static final long HASH_BASE = 12345;
    private static final long HASH_MOD = 1_000_000_007;

    private int[] hashes;

    private void initHashes() {
        calcUsedStates();
        hashes = new int[states];
        for (int st = 0; st < states; st++)
            hashes[st] = getFinal(st) ? HASH_FINAL : HASH_EMPTY;
    }

    private void recalcHashes() {
        int[] newHashes = new int[states];
        Arrays.fill(newHashes, HASH_EMPTY);
        for (int st: usedStates)
        {
            int hash = hashes[st];
            boolean is_empty = hash == HASH_EMPTY;
            for (int to: function[st]) {
                int to_hash = to == -1 ? HASH_EMPTY : hashes[to];
                hash = (int)((HASH_BASE * hash + to_hash) % HASH_MOD);
                if (to_hash != HASH_EMPTY)
                    is_empty = false;
            }
            newHashes[st] = is_empty ? HASH_EMPTY : hash;
        }
        hashes = newHashes;
    }

    public static boolean equivalent(DFA one, DFA two) {
        one.initHashes();
        two.initHashes();
        int prevSize = -1;
        while (true) {
            if (one.hashes[one.s0] != two.hashes[two.s0])
                return false;
            Set<Integer> set = new HashSet<>();
            for (int h: one.hashes)
                set.add(h);
            for (int h: two.hashes)
                set.add(h);
            if (prevSize == set.size())
                return true;
            prevSize = set.size();
            one.recalcHashes();
            two.recalcHashes();
        }
    }
}
