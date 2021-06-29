import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 */

/**
 * @author almog
 *
 */
public class Measure {
	private static Random rand = new Random();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		experiment();
	}
	
	public static void experiment() {
		int n, v1, v2, max_rank;
		Graph.Node[] nodes;
		Graph g;
		Set<Pair> set_of_edges;		
		Pair e;
		Graph.Node max_node;
		for (int i = 6; i < 22; i++) {
			n = (int) Math.pow(2, i);
			nodes = new Graph.Node[n];
			for (int j = 0; j < n; j++)
				nodes[j] = new Graph.Node(j, 1);
			g = new Graph(nodes);
			set_of_edges = new HashSet<>();
			while (set_of_edges.size() < n) {
				v1 = rand.nextInt(n);
				v2 = rand.nextInt(n);
				if (v1 == v2)
					continue;
				e = new Pair(Math.min(v1, v2), Math.max(v1, v2));
				if (set_of_edges.contains(e))
					continue;
				set_of_edges.add(e);
				g.addEdge(v1, v2);
			}
			max_node = g.maxNeighborhoodWeight();
			max_rank = g.getNeighborhoodWeight(max_node.getId()) - 1;
			System.out.println("i = " + i + ", n = " + n + ", max_rank = " + max_rank);
		}
	}
}
