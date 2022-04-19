package cz.cvut.fit.steuejan.travel.api.trip.expense.bussiness;

import java.util.*;

import static java.lang.Math.min;

/**
 * Implementation of algorithm to simplify debts using Dinic's network flow algorithm. The algorithm picks edges one at a time and
 * runs the network flow algorithm to generates the residual graph, which is then again fed back to the network flow algorithm
 * until there are no more non visited edges.
 *
 * <p>Time Complexity: O(E²V²)
 *
 * @author Mithun Mohan K, mithunmk93@gmail.com
 * @link https://github.com/mithun-mohan/Algorithms-Java-Cookbook/blob/master/MaximumFlow/Dinics/SimplifyDebts.java
 */
public class SimplifyDebts {
    private static final long OFFSET = 1000000000L;

    private Set<Long> visitedEdges;
    private final String[] users;

    private final HashMap<String, Integer> usersWithIdx;

    public SimplifyDebts(String[] users) {
        Set<String> tmp = new HashSet<>(List.of(users));
        if (tmp.size() != users.length) {
            throw new IllegalArgumentException("User names must be unique.");
        }

        this.users = users;
        usersWithIdx = new HashMap<>();

        for (int i = 0; i < users.length; i++) {
            usersWithIdx.put(users[i], i);
        }
    }

    public static class Transaction {
        private final String whoPaid;
        private final String whoOwes;
        private final long amountInCents;

        public Transaction(String whoPaid, String whoOwes, long amountInCents) {
            this.whoPaid = whoPaid;
            this.whoOwes = whoOwes;
            this.amountInCents = amountInCents;
        }
    }

    public static class SuggestedPayment {
        public final String from;
        public final String to;
        public final long amountInCents;

        public SuggestedPayment(String from, String to, long amountInCents) {
            this.from = from;
            this.to = to;
            this.amountInCents = amountInCents;
        }

        @Override
        public String toString() {
            return from + " ------ " + amountInCents / 100.0 + " -----> " + to;
        }
    }

    public List<SuggestedPayment> suggestPayments(Transaction[] transactions) {
        //  List of all people in the group
        int n = users.length;
        //  Creating a graph with n vertices
        Dinics solver = new Dinics(n, users);
        //  Adding edges to the graph
        addAllTransactions(solver, transactions);

        //  Map to keep track of visited edges
        visitedEdges = new HashSet<>();
        Integer edgePos;

        while ((edgePos = getNonVisitedEdge(solver.getEdges())) != null) {
            //  Force recomputation of subsequent flows in the graph
            solver.recompute();
            //  Set source and sink in the flow graph
            Dinics.Edge firstEdge = solver.getEdges().get(edgePos);
            solver.setSource(firstEdge.from);
            solver.setSink(firstEdge.to);
            //  Initialize the residual graph to be same as the given graph
            List<Dinics.Edge>[] residualGraph = solver.getGraph();
            List<Dinics.Edge> newEdges = new ArrayList<>();

            for (List<Dinics.Edge> allEdges : residualGraph) {
                for (Dinics.Edge edge : allEdges) {
                    long remainingFlow = ((edge.flow < 0) ? edge.capacity : (edge.capacity - edge.flow));
                    //  If there is capacity remaining in the graph, then add the remaining capacity as an edge
                    //  so that it can be used for optimizing other debts within the graph
                    if (remainingFlow > 0) {
                        newEdges.add(new Dinics.Edge(edge.from, edge.to, remainingFlow));
                    }
                }
            }

            //  Get the maximum flow between the source and sink
            long maxFlow = solver.getMaxFlow();
            //  Mark the edge from source to sink as visited
            int source = solver.getSource();
            int sink = solver.getSink();
            visitedEdges.add(getHashKeyForEdge(source, sink));
            //  Create a new graph
            solver = new Dinics(n, users);
            //  Add edges having remaining capacity
            solver.addEdges(newEdges);
            //  Add an edge from source to sink in the new graph with obtained maximum flow as it's weight
            solver.addEdge(source, sink, maxFlow);
        }
        //  Print the edges in the graph
        return solver.getSuggestedPayments();
    }

    private void addAllTransactions(Dinics solver, Transaction[] transactions) {
        for (Transaction transaction : transactions) {
            int whoPaidIdx, whoOwesIdx;

            try {
                whoPaidIdx = usersWithIdx.get(transaction.whoPaid);
            } catch (NullPointerException exception) {
                throw new IllegalArgumentException(String.format("Person %s not found.", transaction.whoPaid));
            }

            try {
                whoOwesIdx = usersWithIdx.get(transaction.whoOwes);
            } catch (NullPointerException exception) {
                throw new IllegalArgumentException(String.format("Person %s not found.", transaction.whoOwes));
            }

            if (whoPaidIdx != whoOwesIdx) {
                solver.addEdge(whoOwesIdx, whoPaidIdx, transaction.amountInCents);
            }
        }
    }

    /**
     * Get any non visited edge in the graph
     *
     * @param edges list of all edges in the graph
     * @return index of a non visited edge
     */
    private Integer getNonVisitedEdge(List<Dinics.Edge> edges) {
        Integer edgePos = null;
        int curEdge = 0;
        for (Dinics.Edge edge : edges) {
            if (!visitedEdges.contains(getHashKeyForEdge(edge.from, edge.to))) {
                edgePos = curEdge;
            }
            curEdge++;
        }
        return edgePos;
    }

    /**
     * Get a unique hash key for a given edge
     *
     * @param u the starting vertex in the edge
     * @param v the ending vertex in the edge
     * @return a unique hash key
     */
    private Long getHashKeyForEdge(int u, int v) {
        return u * OFFSET + v;
    }
}


/**
 * Implementation of Dinic's network flow algorithm. The algorithm works by first constructing a
 * level graph using a BFS and then finding augmenting paths on the level graph using multiple DFSs.
 *
 * <p>Time Complexity: O(EV²)
 *
 * @link https://github.com/williamfiset/Algorithms
 */
class Dinics extends NetworkFlowSolverBase {

    private final int[] level;

    /**
     * Creates an instance of a flow network solver. Use the {@link #addEdge} method to add edges to
     * the graph.
     *
     * @param n - The number of nodes in the graph including source and sink nodes.
     */
    public Dinics(int n, String[] vertexLabels) {
        super(n, vertexLabels);
        level = new int[n];
    }

    @Override
    public void solve() {
        // next[i] indicates the next unused edge index in the adjacency list for node i. This is part
        // of the Shimon Even and Alon Itai optimization of pruning deads ends as part of the DFS phase.
        int[] next = new int[n];

        while (bfs()) {
            Arrays.fill(next, 0);
            // Find max flow by adding all augmenting path flows.
            for (long f = dfs(s, next, INF); f != 0; f = dfs(s, next, INF)) {
                maxFlow += f;
            }
        }

        for (int i = 0; i < n; i++) if (level[i] != -1) minCut[i] = true;
    }

    // Do a BFS from source to sink and compute the depth/level of each node
    // which is the minimum number of edges from that node to the source.
    private boolean bfs() {
        Arrays.fill(level, -1);
        level[s] = 0;
        Deque<Integer> q = new ArrayDeque<>(n);
        q.offer(s);
        while (!q.isEmpty()) {
            int node = q.poll();
            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[node] + 1;
                    q.offer(edge.to);
                }
            }
        }
        return level[t] != -1;
    }

    private long dfs(int at, int[] next, long flow) {
        if (at == t) return flow;
        final int numEdges = graph[at].size();

        for (; next[at] < numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            long cap = edge.remainingCapacity();
            if (cap > 0 && level[edge.to] == level[at] + 1) {

                long bottleNeck = dfs(edge.to, next, min(flow, cap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }
}


abstract class NetworkFlowSolverBase {

    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
    protected static final long INF = Long.MAX_VALUE / 2;

    public static class Edge {
        public int from, to;
        public Edge residual;
        public long flow;
        public final long capacity, originalCost;

        public Edge(int from, int to, long capacity) {
            this(from, to, capacity, 0 /* unused */);
        }

        public Edge(int from, int to, long capacity, long cost) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.originalCost = cost;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleNeck) {
            flow += bottleNeck;
            residual.flow -= bottleNeck;
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            return String.format(
                    "Edge %s -> %s | flow = %d | capacity = %d | is residual: %s",
                    u, v, flow, capacity, isResidual());
        }
    }

    // Inputs: n = number of nodes, s = source, t = sink
    protected int n, s, t;

    protected long maxFlow;

    protected boolean[] minCut;
    protected List<Edge>[] graph;
    protected String[] vertexLabels;
    protected List<Edge> edges;

    // Indicates whether the network flow algorithm has run. We should not need to
    // run the solver multiple times, because it always yields the same result.
    protected boolean solved;

    /**
     * Creates an instance of a flow network solver. Use the {@link #addEdge} method to add edges to
     * the graph.
     *
     * @param n - The number of nodes in the graph including source and sink nodes.
     */
    public NetworkFlowSolverBase(int n, String[] vertexLabels) {
        this.n = n;
        initializeGraph();
        assignLabelsToVertices(vertexLabels);
        minCut = new boolean[n];
        edges = new ArrayList<>();
    }

    // Construct an empty graph with n nodes including the source and sink nodes.
    @SuppressWarnings("unchecked")
    private void initializeGraph() {
        graph = new List[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    }

    // Add labels to vertices in the graph.
    private void assignLabelsToVertices(String[] vertexLabels) {
        if (vertexLabels.length != n)
            throw new IllegalArgumentException(String.format("You must pass %s number of labels", n));
        this.vertexLabels = vertexLabels;
    }

    /**
     * Adds a list of directed edges (and residual edges) to the flow graph.
     *
     * @param edges - A list of all edges to be added to the flow graph.
     */
    public void addEdges(List<Edge> edges) {
        if (edges == null) throw new IllegalArgumentException("Edges cannot be null");
        for (Edge edge : edges) {
            addEdge(edge.from, edge.to, edge.capacity);
        }
    }

    /**
     * Adds a directed edge (and residual edge) to the flow graph.
     *
     * @param from     - The index of the node the directed edge starts at.
     * @param to       - The index of the node the directed edge ends at.
     * @param capacity - The capacity of the edge.
     */
    public void addEdge(int from, int to, long capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity < 0");
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
        edges.add(e1);
    }

    /**
     * Returns the graph after the solver has been executed. This allow you to inspect the {@link
     * Edge#flow} compared to the {@link Edge#capacity} in each edge. This is useful if you want to
     * figure out which edges were used during the max flow.
     */
    public List<Edge>[] getGraph() {
        execute();
        return graph;
    }

    /**
     * Returns all edges in this flow network
     */
    public List<Edge> getEdges() {
        return edges;
    }

    // Returns the maximum flow from the source to the sink.
    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    /**
     * Used to set the source for this flow network
     */
    public void setSource(int s) {
        this.s = s;
    }

    /**
     * Used to set the sink for this flow network
     */
    public void setSink(int t) {
        this.t = t;
    }

    /**
     * Get source for this flow network
     */
    public int getSource() {
        return s;
    }

    /**
     * Get sink for this flow network
     */
    public int getSink() {
        return t;
    }

    /**
     * Set 'solved' flag to false to force recomputation of subsequent flows.
     */
    public void recompute() {
        solved = false;
    }

    /**
     * @return suggested payments
     */
    public List<SimplifyDebts.SuggestedPayment> getSuggestedPayments() {
        List<SimplifyDebts.SuggestedPayment> payments = new ArrayList<>();
        for (Edge edge : edges) {
            payments.add(new SimplifyDebts.SuggestedPayment(vertexLabels[edge.from], vertexLabels[edge.to], edge.capacity));
        }
        return payments;
    }

    // Wrapper method that ensures we only call solve() once
    private void execute() {
        if (solved) return;
        solved = true;
        solve();
    }

    // Method to implement which solves the network flow problem.
    public abstract void solve();
}