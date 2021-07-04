import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * 
 */

/**
 * @author almog
 *
 */
public class Tester {
	private static final Random rand = new Random();
	private static HashMap<Integer, Integer> myNodes = new HashMap<>();
	private static HashSet<Pair> myEdges = new HashSet<>();
	private static Graph g;
	private static OptGraph og;
	private static Graph.Node node;
	private static Pair pair;
	private static final int bound = (int) Math.pow(10, 9);
	private static int[] arr;
	private static int N;
	private static int[] sizes = {0, 0, 0};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testExceptions();
		N = rand.nextInt(5000);
		createGraphs(N);
		if (N == 0)
			return;
		sizes[0] = sizes[1] = N;
		int s = rand.nextInt(1);
		seqOfActions(s);
//		int m = rand.nextInt(Math.min(100 * N, (N * (N + 1)) / 2));
//		addEdges(m);
//		int d = rand.nextInt(N + 1);
//		removeNodes(d);
	}
	
	/**
	 * @param N
	 */
	public static void createGraphs(int N) {
		Graph.Node[] nodes = new Graph.Node[N];
		arr = new int[N];
		int a, b;
		for (int i = 0; i < N; i++) {
			do {
				a = rand.nextInt(bound);
			} while (myNodes.containsKey(a));
			arr[i] = a;
			b = rand.nextInt(bound);
			myNodes.put(a, b);
			node = new Graph.Node(a, b);
			nodes[i] = node;
		}
//		System.out.println(myNodes);
		g = new Graph(nodes);
		og = new OptGraph(nodes);
		System.out.println("N = " + N);
		if (myNodes.size() != N)
			System.out.println("|myNodes| = " + myNodes.size());
		if ((g.getNumEdges() != og.getNumEdges()) || (g.getNumEdges() != 0))
			System.out.println(g.getNumEdges() + ", " + og.getNumEdges());
		if ((g.getNumNodes() != og.getNumNodes()) || (g.getNumNodes() != N))
			System.out.println(g.getNumNodes() + ", " + og.getNumNodes());
		for (int key : myNodes.keySet())
			if ((g.getNeighborhoodWeight(key) != og.getNeighborhoodWeight(key)) || 
					(g.getNeighborhoodWeight(key) != myNodes.get(key)))
				System.out.println(key);
		System.out.println("Done! Graphs created.");
	}
	
	/**
	 * @param m
	 */
	public static void addEdges(int m) {
		int pos, a, b;
		for (int i = 0; i < m; i++) {
			do {
				pos = rand.nextInt(N);
				a = arr[pos];
			} while (!myNodes.containsKey(a));
			do {
				pos = rand.nextInt(N);
				b = arr[pos];
			} while ((b == a) || (!myNodes.containsKey(b)));
			pair = new Pair(Math.min(a, b), Math.max(a, b));
			if (myEdges.contains(pair)) {
				i--;
				continue;
			}
			myEdges.add(pair);
			g.addEdge(a, b);
			og.addEdge(a, b);
		}
		sizes[2] += m;
		System.out.println("m = " + sizes[2]);
		if (myEdges.size() != sizes[2])
			System.out.println("|myEdges| = " + myEdges.size());
		if ((g.getNumEdges() != og.getNumEdges()) || (g.getNumEdges() != sizes[2]))
			System.out.println(g.getNumEdges() + ", " + og.getNumEdges());
		if ((g.getNumNodes() != og.getNumNodes()) || (g.getNumNodes() != N))
			System.out.println(g.getNumNodes() + ", " + og.getNumNodes());
		for (int key : myNodes.keySet())
			if (g.getNeighborhoodWeight(key) != og.getNeighborhoodWeight(key))
				System.out.println(key);
		System.out.println("Done! " + m + " Edges added.");
	}
	
	/**
	 * @param d
	 */
	public static void removeNodes(int d) {
		int pos, a;
		for (int i = 0; i < d; i++) {
			do {
				pos = rand.nextInt(N);
				a = arr[pos];	
			} while (!myNodes.containsKey(a));
			myNodes.remove(a);
			g.deleteNode(a);
			og.deleteNode(a);
			Iterator<Pair> itr = myEdges.iterator();
			while (itr.hasNext()) {
				Pair pair = itr.next();
				if ((pair.getFirst() == a) || (pair.getSecond() == a))
					itr.remove();
			}
		}
		int m = myEdges.size();
		int n = sizes[1];
		System.out.println("n = " + (n - d) + ", m = " + m);
//		System.out.println("|myEdges| = " + m);
		if ((g.getNumEdges() != og.getNumEdges()) || (g.getNumEdges() != m))
			System.out.println(g.getNumEdges() + ", " + og.getNumEdges());
		if ((g.getNumNodes() != og.getNumNodes()) || (g.getNumNodes() != (n - d)))
			System.out.println(g.getNumNodes() + ", " + og.getNumNodes());
		for (int key : myNodes.keySet())
			if (g.getNeighborhoodWeight(key) != og.getNeighborhoodWeight(key))
				System.out.println(key);
		System.out.println("Done! " + d + " Nodes removed.");
	}
	
	/**
	 * @param s
	 */
	public static void seqOfActions(int s) {
		int n = N;
		int c = 0;
		while (n > s) {
			if (myEdges.size() == (n * (n + 1)) / 2) {
				do {
					c = rand.nextInt(n + 1);
					removeNodes(c);
					n -= c;
				} while (n > s);
				return;
			}
			else if (rand.nextBoolean()) {
				c = rand.nextInt(Math.max(1, myEdges.size() - ((n * (n + 1)) / 2)));
				addEdges(c);
			}
			else {
				c = rand.nextInt(n + 1);
				removeNodes(c);
				n -= c;
			}
		}
	}
	
	public static void testExceptions() {
		N = 2;
		Graph.Node[] nodes = new Graph.Node[N];
		arr = new int[N];
		for (int i = 0; i < N; i++) {
			arr[i] = i;
			myNodes.put(i, i);
			node = new Graph.Node(i, i);
			nodes[i] = node;
		}
		g = new Graph(nodes);
		myEdges.add(new Pair(0, 1));
		g.addEdge(0, 1);
		myNodes.remove(0);
		g.deleteNode(0);
	}
}
