import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * 
 */

/**
 * @author almog
 *
 */

/**
 * This class represents an “optimized” graph that efficiently maintains the heaviest neighborhood over
 * edge addition and vertex deletion.
 * It is more compact due to laziness and also usage of built‐in classes and data structures' implementations,
 * such as ArrayList, HashMap, LinkedList and PriorityQueue.
 * However, while time constants are probably lower, in some operations the time complexity is worse (even 
 * on average case), and it probably takes much more space. 
 *
 */
public class OptGraph {
	private int numEdges = 0;
	private PriorityQueue<OptNode> maxHeap;
	private HashMap<Integer, OptNode> dict;
	
	/**
	 * @param nodes
	 */
	public OptGraph(Graph.Node[] nodes) {
		this.dict = new HashMap<Integer, OptNode>((int) 2.5 * nodes.length);
		ArrayList<OptNode> heaps = new ArrayList<OptNode>();
		OptNode optNode;
		for (Graph.Node node : nodes) {
			optNode = new OptNode(node);
			heaps.add(optNode);
			this.dict.put(optNode.getId(), optNode);
		}
		this.maxHeap = new PriorityQueue<OptNode>(heaps);
	}

    /**
     * This method returns the node in the graph with the maximum neighborhood weight.
     * Note: nodes that have been removed from the graph using deleteNode are no longer in the graph.
     * <p>
     * Time Complexity: Θ(1).
     * @return a Node object representing the correct node. If there is no node in the graph, returns 'null'.
     */
    public Graph.Node maxNeighborhoodWeight() {
    	if (this.dict.isEmpty())
    		return null;
    	int node_id = this.maxHeap.peek().getId();
    	return this.dict.get(node_id).getNode();
    }

    /**
     * given a node id of a node in the graph, this method returns the neighborhood weight of that node.
     * <p>
     * Time Complexity: Θ(1).
     * @param node_id – an id of a node.
     * Otherwise, the function returns -1.
     */
    public int getNeighborhoodWeight(int node_id) {
        OptNode optNode = this.dict.get(node_id);
        if (optNode == null)
        	return -1;
        return optNode.getNeighborWeight();
    }

    /**
     * This function adds an edge between the two nodes whose ids are specified.
     * If one of these nodes is not in the graph, the function does nothing.
     * The two nodes must be distinct; otherwise, the function does nothing.
     * You may assume that if the two nodes are in the graph, there exists no edge between them prior to the call.
     * <p>
     * Time Complexity: Θ(n).
     *
     * @param node1_id – the id of the first node.
     * @param node2_id – the id of the second node.
     * @return returns 'true' if the function added an edge, otherwise returns 'false'.
     */
    public boolean addEdge(int node1_id, int node2_id) {
    	if (node1_id == node2_id)
    		return false;
        OptNode oNode1 = this.dict.get(node1_id); // Θ(1) on average.
        OptNode oNode2 = this.dict.get(node2_id); // Θ(1) on average.
        if ((oNode1 == null) || (oNode2 == null))
        	return false;
        oNode1.addNeighbor(oNode2);
        oNode2.addNeighbor(oNode1);
        this.maxHeap.remove(oNode1);
        this.maxHeap.add(oNode1);
        this.maxHeap.remove(oNode2);
        this.maxHeap.add(oNode2);
        this.numEdges++;
        return true;
    }

    /**
     * Given the id of a node in the graph, deletes the node of that id from the graph, if it exists.
     * <p>
     * Average Time Complexity: Θ(n log(n)).
     *
     * @param node_id – the id of the node to delete.
     * @return returns 'true' if the function deleted a node, otherwise returns 'false'
     */
    public boolean deleteNode(int node_id) {
    	OptNode optNode = this.dict.get(node_id); // Θ(1) on average.
        if (optNode == null)
        	return false;
        this.dict.remove(node_id); // Θ(1) on average.
        this.maxHeap.remove(optNode);
        for (OptNode neighbor : optNode.getNeighbors()) {
        	neighbor.removeNeighbor(optNode);
            this.maxHeap.remove(neighbor);
            this.maxHeap.add(neighbor);
        	this.numEdges--;
        }
        optNode.getNeighbors().clear();
        return true;
    }
	
	/**
	 * Returns the number of nodes currently in the graph.
	 * <p>
     * Time Complexity: Θ(1).
	 * @return the number of nodes in the graph.
	 */
	public int getNumNodes() {
		return this.dict.size();
	}
	
	/**
	 * Returns the number of edges currently in the graph.
	 * <p>
     * Time Complexity: Θ(1).
	 * @return the number of edges currently in the graph.
	 */
	public int getNumEdges() {
		return this.numEdges;
	}
	
	public class OptNode implements Comparable<OptNode> {
		private final int id;
		private final int weight;
		private int neighborWeight;
		private LinkedList<OptNode> neighbors;
		
		/**
		 * @param id
		 * @param weight
		 */
		public OptNode(int id, int weight) {
			this.id = id;
			this.neighborWeight = this.weight = weight;
			this.neighbors = new LinkedList<OptGraph.OptNode>();
		}
		
		/**
		 * @param node
		 */
		public OptNode(Graph.Node node) {
			this.id = node.getId();
			this.neighborWeight = this.weight = node.getWeight();
			this.neighbors = new LinkedList<OptGraph.OptNode>();
		}

		/**
		 * @return the neighborWeight
		 */
		public int getNeighborWeight() {
			return this.neighborWeight;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * @return the weight
		 */
		public int getWeight() {
			return this.weight;
		}

		/**
		 * @return the neighbors
		 */
		public LinkedList<OptNode> getNeighbors() {
			return this.neighbors;
		}

		/**
		 * @return the node
		 */
		public Graph.Node getNode() {
			return new Graph.Node(this.id, this.weight);
		}

		/**
		 * @param neighbor
		 */
		public void addNeighbor(OptNode neighbor) {
			this.neighbors.addFirst(neighbor);
			this.neighborWeight += neighbor.getWeight();
		}

		/**
		 * @param neighbor
		 */
		public void removeNeighbor(OptNode neighbor) {
			this.neighbors.remove(neighbor);
			this.neighborWeight -= neighbor.getWeight();
		}

		@Override
		public int compareTo(OptNode o) {
			return Integer.compare(o.getNeighborWeight(), this.neighborWeight);
		}
	}
}
