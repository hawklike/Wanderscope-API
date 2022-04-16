package cz.cvut.fit.steuejan.travel.api.expense.bussiness;

import java.math.BigDecimal;
import java.util.*;

/**
 * Implementation of algorithm to simplify debts using Dinic's network flow algorithm. The algorithm picks edges one at a time and
 * runs the network flow algorithm to generates the residual graph, which is then again fed back to the network flow algorithm
 * until there are no more non visited edges.
 *
 * <p>Time Complexity: O(E²V²)
 *
 * @author Mithun Mohan K, mithunmk93@gmail.com
 */
public class SimplifyDebts {
    private static final long OFFSET = 1000000000L;

    private Set<Long> visitedEdges;
    private final String[] users;

    private final HashMap<String, Integer> usersWithIdx;

    SimplifyDebts(String[] users) {
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
        private final Double amount;

        public Transaction(String whoPaid, String whoOwes, Double amount) {
            this.whoPaid = whoPaid;
            this.whoOwes = whoOwes;
            this.amount = amount;
        }
    }

    public static class SuggestedPayment {
        private final String from;
        private final String to;
        private final BigDecimal amount;

        public SuggestedPayment(String from, String to, BigDecimal amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "SuggestedPayment{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }

    public List<SuggestedPayment> createGraphForDebts(Transaction[] transactions) {
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
                    BigDecimal remainingFlow = ((edge.flow.compareTo(BigDecimal.ZERO) < 0) ? edge.capacity : (edge.capacity.subtract(edge.flow)));
                    //  If there is capacity remaining in the graph, then add the remaining capacity as an edge
                    //  so that it can be used for optimizing other debts within the graph
                    if (remainingFlow.compareTo(BigDecimal.ZERO) > 0) {
                        newEdges.add(new Dinics.Edge(edge.from, edge.to, remainingFlow));
                    }
                }
            }

            //  Get the maximum flow between the source and sink
            BigDecimal maxFlow = solver.getMaxFlow();
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
            int whoPaidIdx = usersWithIdx.get(transaction.whoPaid);
            int whoOwesIdx = usersWithIdx.get(transaction.whoOwes);
            solver.addEdge(whoOwesIdx, whoPaidIdx, BigDecimal.valueOf(transaction.amount));
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
            for (BigDecimal f = dfs(s, next, INF); f.compareTo(BigDecimal.ZERO) != 0; f = dfs(s, next, INF)) {
                maxFlow = maxFlow.add(f);
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
                BigDecimal cap = edge.remainingCapacity();
                if (cap.compareTo(BigDecimal.ZERO) > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[node] + 1;
                    q.offer(edge.to);
                }
            }
        }
        return level[t] != -1;
    }

    private BigDecimal dfs(int at, int[] next, BigDecimal flow) {
        if (at == t) return flow;
        final int numEdges = graph[at].size();

        for (; next[at] < numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            BigDecimal cap = edge.remainingCapacity();
            if (cap.compareTo(BigDecimal.ZERO) > 0 && level[edge.to] == level[at] + 1) {

                BigDecimal bottleNeck = dfs(edge.to, next, flow.min(cap));
                if (bottleNeck.compareTo(BigDecimal.ZERO) > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return BigDecimal.ZERO;
    }
}


abstract class NetworkFlowSolverBase {

    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
    protected static final BigDecimal INF = BigDecimal.valueOf(Long.MAX_VALUE / 2);

    public static class Edge {
        public int from, to;
        public Edge residual;
        public BigDecimal flow = BigDecimal.ZERO;
        public final BigDecimal capacity, originalCost;

        public Edge(int from, int to, BigDecimal capacity) {
            this(from, to, capacity, BigDecimal.ZERO /* unused */);
        }

        public Edge(int from, int to, BigDecimal capacity, BigDecimal cost) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.originalCost = cost;
        }

        public boolean isResidual() {
            return capacity.compareTo(BigDecimal.ZERO) == 0;
        }

        public BigDecimal remainingCapacity() {
            return capacity.subtract(flow);
        }

        public void augment(BigDecimal bottleNeck) {
            flow = flow.add(bottleNeck);
            residual.flow = residual.flow.subtract(bottleNeck);
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            return String.format(
                    "Edge %s -> %s | flow = %s | capacity = %s | is residual: %s",
                    u, v, flow.toPlainString(), capacity.toPlainString(), isResidual());
        }
    }

    // Inputs: n = number of nodes, s = source, t = sink
    protected int n, s, t;

    protected BigDecimal maxFlow = BigDecimal.ZERO;

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
    public void addEdge(int from, int to, BigDecimal capacity) {
        if (capacity.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Capacity < 0");
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, BigDecimal.ZERO);
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
    public BigDecimal getMaxFlow() {
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