package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;


public class Avl_tree<String extends Comparable<? super String>> {
	public java.lang.String file_name_avl;
	private Node<String> root;
	// used for sending operation
	private Stack<Node<String>> path_sender = new Stack<Node<String>>();
	private Stack<Node<String>> path_rec = new Stack<Node<String>>();

	Avl_tree() {
		this.root = null;
	}

	private void writting(java.lang.String string) {
		try {
			FileWriter myWriter = new FileWriter(file_name_avl, true);
//			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			myWriter.write(string);
			myWriter.close();
//			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			this.writting("An error occurred.");
			e.printStackTrace();
		}
	}

	private int height(Node<String> N) {
		if (N == null)
			return -1;

		return N.height;
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
			this.writting(r.ip + ": New node being added with IP:" + x + "\n");
		}

		int compare = x.compareTo(r.ip);
		if (compare < 0) {
			r.left = add(x, r.left);

		} else if (compare > 0) {
			r.right = add(x, r.right);
		}
		// compare can't be zero as each ip is unique
		r.height = Math.max(this.height(r.left), this.height(r.right)) + 1;
//		this.writting("here possible new heasd"+ r.ip+" my height "+r.height);
		return this.balance(r);

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
		r.height = Math.max(height(r.left), height(r.right)) + 1;
		return this.balance(r);
	}

	public void print() {
		this.print(this.root);
	}

	private void print(Node<String> r) {
		if (r == null)
			return;
		print(r.left);
//		this.writting(r.ip + " >>>>>>>> " + r.height+"\n");
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
	

	private Node<String> balance(Node<String> r) {
		if (r == null) {
			return null;
		}

		if (this.height(r.left) - this.height(r.right) > 1) {
			if (this.height(r.left.left) >= this.height(r.left.right)) {
				this.writting("Rebalancing: right rotation" + "\n");
				r = this.rotate_left_child(r);
			} else {
				this.writting("Rebalancing: left-right rotation" + "\n");
				r = this.double_rotate_left_child(r);
			}
		} else if (this.height(r.right) - this.height(r.left) > 1) {
			if (this.height(r.right.right) >= this.height(r.right.left)) {
				this.writting("Rebalancing: left rotation" + "\n");
				r = this.rotate_right_child(r);
			} else {
				this.writting("Rebalancing: right-left rotation" + "\n");
				r = this.double_rotate_right_child(r);
			}
		}
		r.height = Math.max(height(r.left), height(r.right)) + 1;
//		this.writting("#################"+r.ip+" heighhtttt "+r.height+"have left "+this.height(r.left)+"have right"+this.height(r.right));
		return r;
	}

	// rotate in the left child
	private Node<String> rotate_left_child(Node<String> r) {
		Node<String> k = r.left;
		Node<String> change_place = k.right;
		r.left = change_place;
		k.right = r;
		if (r.equals(this.root)) {
			this.root = k;
		}
		r.height = Math.max(this.height(r.left), this.height(r.right)) + 1;
		k.height = Math.max(this.height(k.left), this.height(k.right)) + 1;

		return k;
	}

	private Node<String> rotate_right_child(Node<String> r) {
		Node<String> k = r.right;
		Node<String> change_place = k.left;
		r.right = change_place;
		k.left = r;
		if (r.equals(this.root)) {
			this.root = k;
		}
		r.height = Math.max(this.height(r.left), this.height(r.right)) + 1;
		k.height = Math.max(this.height(k.left), this.height(k.right)) + 1;
//		this.writting("neeew heeead "+k.ip);

		return k;
	}

	private Node<String> double_rotate_left_child(Node<String> r) {
		r.left = this.rotate_right_child(r.left);
		return this.rotate_left_child(r);

	}

	private Node<String> double_rotate_right_child(Node<String> r) {
		r.right = this.rotate_left_child(r.right);
		return this.rotate_right_child(r);
	}

}
