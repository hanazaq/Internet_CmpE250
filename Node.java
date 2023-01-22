package src;

public class Node <String>{
	String ip;
	Node<String> left;
	Node<String> right;
	int height;

	public Node(String ip) {
		this(ip, null, null);
		this.height=0;
	}

	public Node(String ip, Node<String> left, Node<String> right) {
		this.ip = ip;
		this.left = left;
		this.right = right;
	}
}
