package se306.a1.scheduler.data;

import java.util.Collection;
import java.util.LinkedList;

/** 
 * This class represents an abstract node object for a graph.
 * This node represents a task, and as such has links to subsequent
 * tasks which are dependent upon it, as well as the cost of transitions
 * to the child tasks/nodes.
 * @author Rodger Gu
 */
public abstract class Node {
	private final String name;
	private final int value;

	private LinkedList<Node> parents;
	private LinkedList<Node> children;
	private LinkedList<Integer> links;

	/**
	 * The default constructor for a Node object. This constructor
	 * instantiates the lists for storing child nodes and the values
	 * associated with traversal to these child nodes.
     */
	public Node() {
		name = "";
		value = 0;
		parents = new LinkedList<>();
		children = new LinkedList<>();
		links = new LinkedList<>();
	}

	/**
	 * The basic constructor for a Node object. This constructor instantiates
	 * the lists for storing child nodes and the values associated with traversal
	 * to these child nodes, and also takes in the name of the node and its value.
	 * @param name string representation. or name, of the task at this node
	 * @param value integer value representing the cost of the task at this node
     */
	public Node(String name, int value) {
		this.name = name;
		this.value = value;
		parents = new LinkedList<>();
		children = new LinkedList<>();
		links = new LinkedList<>();
	}

	/**
	 * The import constructor for a Node object. This constructor requires all information
	 * to be given to construct a Node object.
	 * @param name string representation. or name, of the task at this node
	 * @param value integer value representing the cost of the task at this node
	 * @param parents a LinkedList representing all the nodes that are parents of this node
	 * @param children a LinkedList representing all the nodes that are the children of this node
	 * @param links a LinkedList representing the costs associated with the list of child nodes
     */
	public Node(String name, int value, LinkedList<Node> parents, LinkedList<Node> children, LinkedList<Integer> links) {
		this.name = name;
		this.value = value;
		this.parents = parents;
		this.children = children;
		this.links = links;
	}

	/**
	 * Gets the parents of this node object.
	 * @return a Collection of the parent nodes of this node
	 */
	public Collection<Node> getParents() {
		return parents;
	}

	/**
	 * Gets the children of this node object.
	 * @return a Collection of the child nodes of this node
     */
	public Collection<Node> getChildren() {
		return children;
	}

	/**
	 * Gets the links of this node object.
	 * @return a Collection of the link values of this node
     */
	public Collection<Integer> getLinks() {
		return links;
	}

	/**
	 * Gets the child number I of the parent. This method should
	 * be called in conjunction with the {@link #getLink(int) getLink} method
	 * when computing the cost of a traversal.
	 * @param i position number
	 * @return the child at the position
	 * @throws IndexOutOfBoundsException if the value is outside of
	 * the range of children numbers.
     */
	public Node getChild(int i) {
		return children.get(i);
	}

	/**
	 * Adds the specified node to the child node (this).
	 * @param node the parent node to be added
	 * @return if the parent node was successfully added
	 */
	public boolean addParent(Node node) {
		return parents.add(node);
	}

	/**
	 * Adds the specified node to the parent node (this). This method should
	 * be called in conjunction with the {@link #addLink(Integer) addLink} method
	 * when populating this node's list of child nodes.
	 * @param node child node to be added
	 * @return if the child node was successfully added
	 */
	public boolean addChild(Node node) {
		return children.add(node);
	}

	/**
	 * Gets the link cost i from the parent. This method should
	 * be called in conjunction with the {@link #getChild(int) getChild} method
	 * when computing the cost of a traversal.
	 * @param i position number
	 * @return the link at the position
	 * @throws IndexOutOfBoundsException if the value is outside the range
	 * of children numbers.
     */
	public int getLink(int i) {
		return links.get(i);
	}

	/**
	 * Adds the specified link cost from the parent node (this). This method should
	 * be called in conjunction with the {@link #addChild(Node) addChild} method
	 * when populating this node's list of child nodes.
	 * @param cost link cost from parent node (this) to child
	 * @return if the link cost was successfully added
	 */
	public boolean addLink(Integer cost) {
		return links.add(cost);
	}

	/**
	 * Gets the value of computing the task represented by this node.
	 * @return the value of the task at this node
     */
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		if (this == obj) return true;

		if (obj instanceof Node)
			return name.equals(((Node)obj).name);

		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
