package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;


public class Bst_tree<String extends Comparable<? super String>> {

	public String name;
	private Node<String> root;
	// used for sending operation
	private Stack<Node<String>> path_sender = new Stack<Node<String>>();
	private Stack<Node<String>> path_rec = new Stack<Node<String>>();

	Bst_tree() {
		this.root = null;
	}

	private void writting(java.lang.String string) {
		try {
			FileWriter myWriter = new FileWriter(((java.lang.String) name).replace(".txt","_bst.txt"), true);
//			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			myWriter.write(string);
			myWriter.close();
//			this.writting("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void add(String x) {
		if (this.root == null) {
			this.root = new Node<>(x);
		} else {
			this.add(x, this.root);
		}
	}

	// r is the root of the tree
	private Node<String> add(String x, Node<String> r) {

		if (r == null) {
			return new Node<>(x);
		} else {
//			<Logging_node_IP>: New Node being added: <IP_Address>
			this.writting(r.ip + ": New node being added with IP:" + x + "\n");
		}

		int compare = x.compareTo(r.ip);
		if (compare < 0) {
			r.left = add(x, r.left);

		} else if (compare > 0) {
			r.right = add(x, r.right);
		}
		// compare can't be zero as each ip is unique
		return r;
	}

	private Node<String> find_min(Node<String> r) {
		if (r != null) {
			if (r.left == null) {
				return r;
			}
			return find_min(r.left);
		}
		return r;

	}

	public void delete(String x) {
		delete(x, this.root, this.root, x);
	}

	private Node<String> delete(String x, Node<String> r, Node<String> father, String del_now) {
		if (r == null) {
//			this.writting("NOT FOUND WHILE DELETING");
			return null;
		}

		int compare = x.compareTo(r.ip);
		// not found yet
		if (compare < 0) {
			r.left = delete(x, r.left, r, del_now);
		} else if (compare > 0) {
			r.right = delete(x, r.right, r, del_now);
		}
		// has two children
		else if (r.left != null && r.right != null) {

			r.ip = this.find_min(r.right).ip;
			r.right = delete(r.ip, r.right, r, del_now);
			// avoid printing the delete of swapping
			if (!father.ip.equals(r.ip) && del_now.equals(x)) {
				this.writting(father.ip + ": Non Leaf Node Deleted; removed: " + x + " replaced: " + r.ip + "\n");
			}
		}
		// has only left children
		else if (r.left != null) {
			if (!father.ip.equals(r.ip) && del_now.equals(x)) {
				this.writting(father.ip + ": Node with single child Deleted: " + r.ip + "\n");
			}
			return r.left;
		}
		// has only right children
		else if (r.right != null) {
			if (!father.ip.equals(r.ip) && del_now.equals(x)) {
				this.writting(father.ip + ": Node with single child Deleted: " + r.ip + "\n");
			}
			return r.right;
		}
		// no children
		else {
			if (!father.ip.equals(r.ip) && del_now.equals(x)) {
				this.writting(father.ip + ": Leaf Node Deleted: " + r.ip + "\n");
			}
			return null;
		}

		return r;
	}

	public void print() {
//		this.writting("this is the min"+this.find_min(this.root).ip);
		this.print(this.root);
	}

	private void print(Node<String> r) {
		if (r == null)
			return;
		print(r.left);
//		this.writting(r.ip);
		print(r.right);
	}

	public void send(String sender_ip, String rec_ip) {
		this.send(sender_ip, rec_ip, this.root);
	}

	private Node<String> send(String sender_ip, String rec_ip, Node<String> r) {

		this.store_path(sender_ip, rec_ip, r, path_sender);
		this.store_path(rec_ip, sender_ip, r, path_rec);
		String previous_sender = sender_ip;
		Node<String> keep = null;
		while (!path_sender.isEmpty() && !path_rec.isEmpty()) {
			if (path_sender.size() == 1 && path_sender.peek().ip.equals(path_rec.peek().ip)) {
				break;
			}
			if (path_rec.size() == 1 && path_sender.peek().ip.equals(path_rec.peek().ip)) {
				path_rec.pop();
				while (!path_sender.isEmpty()) {
					path_rec.push(path_sender.pop());
				}
				break;
			}

			Node<String> sender = path_sender.peek();
			Node<String> rec = path_rec.peek();

			if (sender.ip.equals(rec.ip)) {
				keep = rec;
				path_sender.pop();
				path_rec.pop();
			} else {
				if (keep != null) {
					path_rec.push(keep);
				}
				while (!path_sender.isEmpty()) {
					path_rec.push(path_sender.pop());
				}
			}
		}

		this.writting(sender_ip + ": Sending message to: " + rec_ip + "\n");
		while (!path_rec.isEmpty())

		{
			String new_sender = path_rec.pop().ip;
			if (!new_sender.equals(sender_ip) && !new_sender.equals(rec_ip)) {
				if (!path_rec.isEmpty() && previous_sender.equals(path_rec.peek().ip))
					continue;
				this.writting(new_sender + ": Transmission from: " + previous_sender + " receiver: " + rec_ip
						+ " sender:" + sender_ip + "\n");
			}
			previous_sender = new_sender;
		}
		this.writting(rec_ip + ": Received message from: " + sender_ip + "\n");
		return r;
	}

	private Node<String> store_path(String sender_ip, String rec_ip, Node<String> r, Stack<Node<String>> storage) {

		if (r == null) {
			return null;
		}
		if (r.ip.equals(sender_ip)) {
			storage.push(r);
			return r;
		}
		int compare = sender_ip.compareTo(r.ip);
		// not found yet
		if (compare < 0) {
			r.left = store_path(sender_ip, rec_ip, r.left, storage);
			storage.push(r);
		} else if (compare > 0) {
			r.right = store_path(sender_ip, rec_ip, r.right, storage);
			storage.push(r);
		}
		return r;
	}
}
