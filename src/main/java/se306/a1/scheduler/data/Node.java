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
	private int value = 0;
	
	private LinkedList<Node> children;
	private LinkedList<Integer> links;
	
	/**
	 * The default constructor for a Node object. This constructor
	 * instantiates the lists for storing child nodes and the values
	 * associated with traversal to these child nodes.
     */
	public Node() {
		children = new LinkedList<>();
		links = new LinkedList<>();
	}
	
	/**
	 * The basic constructor for a Node object. This constructor instantiates
	 * the lists for storing child nodes and the values associated with traversal
	 * to these child nodes, and also takes in
	 * @param value integer value representing the cost of the task at this node
     */
	public Node(int value) {
		this.value = value;
		children = new LinkedList<>();
		links = new LinkedList<>();
	}
	
	/**
	 * The import constructor for a Node object. This constructor requires all information
	 * to be given to construct a Node object.
	 * @param value the integer value that represents the cost of this node.
	 * @param children A LinkedList representing all the nodes that are the children of this node.
	 * @param links A LinkedList representing the costs associated with the list of child nodes.
     */
	public Node(int value, LinkedList<Node> children, LinkedList<Integer> links) {
		this.value = value;
		this.children = children;
		this.links = links;
	}
	
	/**
	 * Gets the children of this node object.
	 * @return a Collection of the child nodes of this node.
     */
	public Collection<Node> getChildren() {
		return children;
	}
	
	/**
	 * Gets the links of this node object.
	 * @return a Collection of the link values of this node.
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
	 * Gets the link cost I of the parent. This method should
	 * be called in conjunction with the {@link #getChild(int) getChild} method
	 * when computing the cost of a traversal.
	 * @param i position number
	 * @return the link at the position.
	 * @throws IndexOutOfBoundsException if the value is outside the range
	 * of children numbers.
     */
	public int getLink(int i) {
		return links.get(i);
	}
	
	/**
	 * Gets the value of computing the task represented by this node.
	 * @return the value of the task at this node.
     */
	public int getValue() {
		return value;
	}
}
