import java.util.Random;

/**
 * @author almog
 *
 */
/*
You must NOT change the signatures of classes/methods in this skeleton file.
You are required to implement the methods of this skeleton file according to the requirements.
You are allowed to add classes, methods, and members as required.
 */

/**
 * This class represents a graph that efficiently maintains the heaviest neighborhood over edge addition and
 * vertex deletion.
 *
 */
public class Graph {
	private int num_nodes, num_edges = 0;
	private GraphNode[] graph;
	private MaxHeap maxHeap;
	private Hashtable table;
	
	
    /**
     * Initializes the graph on a given set of nodes. The created graph is empty, i.e. it has no edges.
     * You may assume that the ids of distinct nodes are distinct.
     * <p>
     * Time Complexity: O(N).
     * @param nodes – an array of node objects
     */
    public Graph(Node[] nodes) {
    	int N = nodes.length;
    	Node node;
    	HashNode hashNode;
    	this.graph = new GraphNode[N];
    	this.table = new Hashtable((int) 2.5 * N);
    	HeapNode[] heapArray = new HeapNode[N];
    	for (int i = 0; i < N; i++) {
    		node = nodes[i];
    		this.graph[i] = new GraphNode(i, node);
    		heapArray[i] = new HeapNode(node.getWeight(), node.getId(), i);
    		hashNode = new HashNode(node.getId(), heapArray[i], this.graph[i]);
    		this.table.add(hashNode);
    	}
    	this.maxHeap = new MaxHeap(heapArray);
    	this.num_nodes = N;
    }

    /**
     * This method returns the node in the graph with the maximum neighborhood weight.
     * Note: nodes that have been removed from the graph using deleteNode are no longer in the graph.
     * <p>
     * Time Complexity: Θ(1).
     * @return a Node object representing the correct node. If there is no node in the graph, returns 'null'.
     */
    public Node maxNeighborhoodWeight() {
    	if (this.num_nodes == 0)
    		return null;
    	int node_id = this.maxHeap.getMax().getId();
    	return this.table.get(node_id).getGraphNode().getNode();
    }

    /**
     * given a node id of a node in the graph, this method returns the neighborhood weight of that node.
     * <p>
     * Time Complexity: Θ(1).
     * @param node_id – an id of a node.
     * @return the neighborhood weight of the node of id 'node_id' if such a node exists in the graph.
     * Otherwise, the function returns -1.
     */
    public int getNeighborhoodWeight(int node_id) {
        HashNode hashNode = this.table.get(node_id);
        if (hashNode == null)
        	return -1;
        return hashNode.getHeapNode().getKey();
    }

    /**
     * This function adds an edge between the two nodes whose ids are specified.
     * If one of these nodes is not in the graph, the function does nothing.
     * The two nodes must be distinct; otherwise, the function does nothing.
     * You may assume that if the two nodes are in the graph, there exists no edge between them prior to the call.
     *
     * @param node1_id – the id of the first node.
     * @param node2_id – the id of the second node.
     * @return returns 'true' if the function added an edge, otherwise returns 'false'.
     */
    public boolean addEdge(int node1_id, int node2_id) {
    	if (node1_id == node2_id)
    		return false;
        HashNode hNode1 = this.table.get(node1_id);
        HashNode hNode2 = this.table.get(node2_id);
        if ((hNode1 == null) || (hNode2 == null))
        	return false;
        GraphNode gNode1 = hNode1.getGraphNode();
        GraphNode gNode2 = hNode2.getGraphNode();
        NeighborNode neighbor1 = new NeighborNode(null, gNode2);
        NeighborNode neighbor2 = new NeighborNode(new DLLNode<NeighborNode>(neighbor1), gNode1);
        neighbor1.setNeighbor(new DLLNode<NeighborNode>(neighbor2));
        gNode1.getNeighbors().insertFirst(neighbor2);
        gNode2.getNeighbors().insertFirst(neighbor1);
        this.maxHeap.increaseKey(hNode1.getHeapNode().getIndex(), gNode2.getWeight());
        this.maxHeap.increaseKey(hNode2.getHeapNode().getIndex(), gNode1.getWeight());
    	this.num_edges++;
        return true;
    }

    /**
     * Given the id of a node in the graph, deletes the node of that id from the graph, if it exists.
     *
     * @param node_id – the id of the node to delete.
     * @return returns 'true' if the function deleted a node, otherwise returns 'false'
     */
    public boolean deleteNode(int node_id) {
        HashNode hashNode = this.table.get(node_id);
        if (hashNode == null)
        	return false;
        this.table.remove(hashNode);
        this.maxHeap.delete(hashNode.getHeapNode().getIndex());
        GraphNode gNode = hashNode.getGraphNode();
        int weight = gNode.getWeight(), pos;
        DLLNode<NeighborNode> listNode = gNode.getNeighbors().getFirst(), tmp;
        NeighborNode neighbor;
        GraphNode gNeighbor;
        while (listNode != null) {
        	neighbor = listNode.getData();
        	gNeighbor = neighbor.getNeighborData();
        	gNeighbor.getNeighbors().delete(listNode);
        	pos = this.table.get(gNeighbor.getId()).getHeapNode().getIndex();
        	this.maxHeap.decreaseKey(pos, weight);
        	tmp = listNode;
        	listNode = tmp.getNext();
        	gNode.getNeighbors().delete(listNode);
        	this.num_edges--;
        }
        this.graph[gNode.getIndex()] = null;
    	this.num_nodes--;
        return true;
    }
	
	/**
	 * Returns the number of nodes currently in the graph.
	 * <p>
     * Time Complexity: Θ(1).
	 * @return the number of nodes in the graph.
	 */
	public int getNumNodes() {
		return this.num_nodes;
	}
	
	/**
	 * Returns the number of edges currently in the graph.
	 * <p>
     * Time Complexity: Θ(1).
	 * @return the number of edges currently in the graph.
	 */
	public int getNumEdges() {
		return this.num_edges;
	}

    /**
     * This class represents a node in the graph.
     */
    public static class Node {
    	private final int id;
    	private final int weight;

        /**
		 * Creates a new node object, given its id and its weight.
		 * <p>
	     * Time Complexity: Θ(1).
         * @param id – the id of the node.
		 * @param weight – the weight of the node.
		 */
		public Node(int id, int weight) {
			this.id = id;
			this.weight = weight;
		}

		/**
         * Returns the id of the node.
		 * <p>
	     * Time Complexity: Θ(1).
         * @return the id of the node.
		 */
		public int getId() {
			return this.id;
		}

		/**
         * Returns the weight of the node.
		 * <p>
	     * Time Complexity: Θ(1).
         * @return the weight of the node.
		 */
		public int getWeight() {
			return this.weight;
		}
    }
    
    public class HashFunction {
    	private final int prime;
    	private int a, b, m;
    	    	
    	/**
    	 * @param prime – the prime number.
		 * @param m – the size of the hashtable.
		 */
		public HashFunction(int prime, int m) {
			Random rand = new Random();
			this.prime = prime;
			this.a = rand.nextInt(this.prime - 1) + 1;
			this.b = rand.nextInt(this.prime);
			this.m = m;
		}

		public int hash(int x) {
			return ((int) Math.floorMod(1L * this.a * x + this.b, this.prime)) % this.m;
    	}
    }
    
    public class Hashtable {
    	private final int prime = (int) (Math.pow(10, 9) + 9);
    	private DoublyLinkedList<HashNode>[] arr;
    	private final HashFunction hashFunc;
    	private int size;
    	
		/**
		 * @param size
		 */
		@SuppressWarnings("unchecked")
		public Hashtable(int size) {
			this.hashFunc = new HashFunction(this.prime, size);
			this.arr = (DoublyLinkedList<HashNode>[]) new DoublyLinkedList<?>[size];
			for (int i = 0; i < size; i++)
				this.arr[i] = new DoublyLinkedList<>();
			this.size = 0;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return this.size;
		}
		
		public HashNode get(int node_id) {
			int pos = this.hashFunc.hash(node_id);
			return this.arr[pos].get(node_id).getData();
		}
		
		public void add(HashNode hashNode) {
			int node_id = hashNode.getId();
			int pos = this.hashFunc.hash(node_id);
			if (this.arr[pos].get(node_id) == null) {
				this.arr[pos].insertFirst(hashNode);
				this.size++;
			}
		}
		
		public void remove(HashNode hashNode) {
			int node_id = hashNode.getId();
			int pos = this.hashFunc.hash(node_id);
			DLLNode<HashNode> pointer = this.arr[pos].get(node_id);
			if (pointer != null) {
				this.arr[pos].delete(pointer);
				this.size--;
			}
		}
    }
    
    public class DoublyLinkedList<K> {
    	private DLLNode<K> first = null;
    	private DLLNode<K> last = null;
    	private int length;
		
		public void insertFirst(K data) {
			DLLNode<K> node = new DLLNode<>(data);
			node.setNext(this.first);
			this.first = node;
			if (node.getNext() == null)
				this.last = node;
			else
				node.getNext().setPrev(node);
			this.length++;
		}
		
		/**
		 * @return the first
		 */
		public DLLNode<K> get(int node_id) {
			DLLNode<K> cur = this.first;
			while (cur != null) {
				if (((HashNode) cur.getData()).getId() == node_id)
					return cur;
				cur = cur.getNext();
			}
			return null;
		}

		public void delete(DLLNode<K> node) {
			if (this.first == node) {
				this.first = node.getNext();
				if (this.last == node)
					this.last = null;
				else
					this.first.setPrev(null);
			}
			else if (this.last == node) {
				this.last = node.getPrev();
				this.last.setNext(null);
			}
			else {
				node.getPrev().setNext(node.getNext());
				node.getNext().setPrev(node.getPrev());
			}
			this.length--;
		}

		/**
		 * @return the first
		 */
		public DLLNode<K> getFirst() {
			return this.first;
		}

		/**
		 * @return the last
		 */
		public DLLNode<K> getLast() {
			return this.last;
		}
    }

	public class DLLNode<K> {
		private DLLNode<K> prev = null;
    	private DLLNode<K> next = null;
    	private K data;
    	
		/**
		 * @param data
		 */
		public DLLNode(K data) {
			this.data = data;
		}

		/**
		 * @return the data
		 */
		public K getData() {
			return this.data;
		}

		/**
		 * @return the prev
		 */
		public DLLNode<K> getPrev() {
			return this.prev;
		}

		/**
		 * @param prev the prev to set
		 */
		public void setPrev(DLLNode<K> prev) {
			this.prev = prev;
		}

		/**
		 * @return the next
		 */
		public DLLNode<K> getNext() {
			return this.next;
		}

		/**
		 * @param next the next to set
		 */
		public void setNext(DLLNode<K> next) {
			this.next = next;
		}
	}
    
    /**
     * This class represents a (binary) max heap.
     */
    public class MaxHeap {
    	private HeapNode[] heap;
    	private int length; // number of nodes currently in the heap.
    	
		/**
         * Constructs a max heap out of array of HeapNodes.
		 * <p>
	     * Time Complexity: O(N).
		 * @param heap
		 */
		public MaxHeap(HeapNode[] heap) {
			this.heap = heap;
			this.length = heap.length;
			this.arrayToMaxHeap();
		}
		
		/**
         * Returns the max node.
		 * <p>
	     * Time Complexity: Θ(1).
		 * @return heap[0] – the max node.
		 */
		public HeapNode getMax() {
			return this.heap[0];
		}
		
		/**
         * Increases the key of heap[index] by inc.
		 * <p>
	     * Time Complexity: Θ(1).
		 * @param index
		 * @param inc
		 */
		public void increaseKey(int index, int inc) {
			int key = this.heap[index].getKey();
			this.heap[index].setKey(key + inc);
			heapify_up(index);
		}
		
		/**
         * Decreases the key of heap[index] by dec.
		 * <p>
	     * Time Complexity: Θ(1).
		 * @param index
		 * @param dec
		 */
		public void decreaseKey(int index, int dec) {
			int key = this.heap[index].getKey();
			this.heap[index].setKey(key - dec);
			heapify_down(index);
		}
		
		/**
         * Deletes heap[index] from the heap.
		 * <p>
	     * Time Complexity: Θ(1).
		 * @param index
		 */
		public void delete(int index) {
			swap(index, this.length - 1);
			this.length--;
			this.heap[this.length] = null;
			int parentIndex = getParentIndex(index);
			if (this.heap[index].getKey() > this.heap[parentIndex].getKey())
				heapify_up(index);
			else
				heapify_down(index);
		}
		
        private int getLeftChildIndex(int parentIndex) {
		    return 2 * parentIndex + 1;
        }

        private int getRightChildIndex(int parentIndex) {
		    return 2 * parentIndex + 2;
        }
		
        private int getParentIndex(int childIndex) {
        	return (childIndex - 1) / 2;
        }
		
        private void swap(int index1, int index2) {
		    HeapNode tmp = this.heap[index1];
		    this.heap[index1] = this.heap[index2];
		    this.heap[index2] = tmp;
		    this.heap[index1].index = index1;
		    this.heap[index2].index = index2;
        }
		
		private void heapify_up(int index) {
			int parentIndex = getParentIndex(index);
			while ((index > 0) && (this.heap[index].getKey() > this.heap[parentIndex].getKey())) {
				swap(index, parentIndex);
				index = parentIndex;
				parentIndex = getParentIndex(index);
			}
		}
		
		private void heapify_down(int index) {
			int leftIndex = getLeftChildIndex(index);
			int rightIndex = getRightChildIndex(index);
			int max = index;
			if (rightIndex < this.length) {
				if (this.heap[leftIndex].getKey() > this.heap[rightIndex].getKey())
					max = leftIndex;
				else
					max = rightIndex;
			}
			else if (leftIndex < this.length)
				max = leftIndex;
			if (this.heap[max].getKey() > this.heap[index].getKey()) {
				swap(index, max);
				heapify_down(max);
			}
		}
		
		public void arrayToMaxHeap() {
			int last = this.length / 2;
			for (int i = last; i > 0; i--)
				heapify_down(i);
		}
    }
	
	public class HeapNode {
		private int key; // the neighborhood weight of the graph node.
		private final int id; // the id of the graph node.
		private int index; // index in heap array.
		
		/**
		 * @param key
		 * @param id
		 * @param index
		 */
		public HeapNode(int key, int id, int index) {
			this.key = key;
			this.id = id;
			this.index = index;
		}

		/**
		 * @return the key
		 */
		public int getKey() {
			return this.key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(int key) {
			this.key = key;
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return this.index;
		}

		/**
		 * @param index the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}
	}
    
    public interface MyNode {}

    public class GraphNode {
    	private final int index;
    	private final Node node;
    	private DoublyLinkedList<NeighborNode> neighbors;
    	
		/**
		 * @param index
		 * @param node
		 */
		public GraphNode(int index, Node node) {
			this.index = index;
			this.node = node;
			this.neighbors = new DoublyLinkedList<>();
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return this.index;
		}

		/**
         * Returns the node.
		 * <p>
	     * Time Complexity: Θ(1).
		 * @return the node
		 */
		public Node getNode() {
			return this.node;
		}

		/**
         * Returns the id of the node.
		 * <p>
	     * Time Complexity: Θ(1).
         * @return the id of the node.
		 */
		public int getId() {
			return this.node.getId();
		}

		/**
         * Returns the weight of the node.
		 * <p>
	     * Time Complexity: Θ(1).
         * @return the weight of the node.
		 */
		public int getWeight() {
			return this.node.getWeight();
		}

		/**
		 * @return the neighbors
		 */
		public DoublyLinkedList<NeighborNode> getNeighbors() {
			return this.neighbors;
		}
    }
    

    public class NeighborNode implements MyNode {
    	private DLLNode<NeighborNode> neighbor;
    	private final GraphNode neighborData;

		/**
		 * @param neighbor
		 * @param neighborData
		 */
		public NeighborNode(DLLNode<NeighborNode> neighbor, GraphNode neighborData) {
			this.neighbor = neighbor;
			this.neighborData = neighborData;
		}

		/**
		 * @return the neighbor
		 */
		public DLLNode<NeighborNode> getNeighbor() {
			return this.neighbor;
		}

		/**
		 * @param neighbor the neighbor to set
		 */
		public void setNeighbor(DLLNode<NeighborNode> neighbor) {
			this.neighbor = neighbor;
		}

		/**
		 * @return the neighborData
		 */
		public GraphNode getNeighborData() {
			return this.neighborData;
		}
    }

    public class HashNode implements MyNode {
    	private final int id;
    	private final HeapNode heapNode;
    	private final GraphNode graphNode;
    	
		/**
		 * @param id
		 * @param heapNode
		 * @param graphNode
		 */
		public HashNode(int id, HeapNode heapNode, GraphNode graphNode) {
			this.id = id;
			this.heapNode = heapNode;
			this.graphNode = graphNode;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * @return the heapNode
		 */
		public HeapNode getHeapNode() {
			return this.heapNode;
		}

		/**
		 * @return the graphNode
		 */
		public GraphNode getGraphNode() {
			return this.graphNode;
		}
    }
}


