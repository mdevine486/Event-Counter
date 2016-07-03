 import java.util.*;
 import java.io.*;
 
 public class RedBlackTree{
	static Node Head = null;
	
	private static final boolean RED   = false;
	private static final boolean BLACK = true;
	
	/*
	initialize function builds a RedBlackTree using pairs of key and value
	sorted by key and returns root of the tree.
	*/	
	public static Node initialize(int level, int f, int l, int[] K, int[] V){

		if (l < f) return null;
        int mid = (l+f)/2;
        
		//Recursively fill the left and right subtrees of middle node.
		Node left  = initialize(level+1, f, mid - 1,K,V);
		Node middle = new Node(K[mid],V[mid]);
		Node right  = initialize(level+1, mid + 1, l,K,V);
		
		if (left != null) {
			middle.left = left;
			left.parent = middle;
		}
		
		if (right != null) {
			middle.right = right;
			right.parent = middle;
		}
		
		// All nodes are colored black by default, to satisfy RBT properties after BST tree 
		// is build recursively the last level(not complete) nodes are colored red.
		double redl = Math.log(K.length+1)/Math.log(2);
		if (level == (int)redl) {
			middle.color = RED;
		}
		
		return middle;
	}
	
	/*
	Increase function finds the node with the specified ID and increases its count by m.
	If the node is not found then insert a new node(ID,m) into RBT.
	*/
	public static void increase(int theID, int m){
		Node node =  binarySearch(Head,theID);
		if(node == null){
			insert(theID,m);
			System.out.println(m);
		}
		else{
			node.count = node.count+m;
			System.out.println(node.count);
		}	
	}
	
	/*
	Insert function inserts the node with given ID and count based on the BST property and 
	the calls insertFix to maintain RBT property.
	*/
	private static void insert(int ID, int count){
		Node node = Head;
		Node parent = null;
		boolean flag = true;
		
		if(Head == null){
			Head = new Node(ID,count);
		}
		else{
			while(node != null && flag){
				parent = node;
				if(ID < node.ID)
					node = node.left;
				else if(ID > node.ID)
					node = node.right;
				else{
					node.count = count;
					flag = false;
				}
			}
			if(flag){
				Node newNode = new Node(ID,count);
				newNode.parent = parent;
				
				if(ID < parent.ID)
					parent.left = newNode;
				else
					parent.right = newNode;
				insertFix(newNode);
			}
		}
	}
	
	/*
	Insert is classified into XYz
	XYr - color flip is done. 
	LRb - rotateLeft and then rotateRight
	LLb - rotateRight
	RLb - rotateRight and then rotateLeft
	RRb - rotateLeft
	*/
	private static void insertFix(Node node){
		node.color = RED;
		
		while (node != null && node != Head && node.parent.color == RED) {
			if (parentOf(node) == leftOf(parentOf(parentOf(node)))) {
                Node d = rightOf(parentOf(parentOf(node)));
				//color flip
                if (colorOf(d) == RED) {
                    setColor(parentOf(node), BLACK);
                    setColor(d, BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    node = parentOf(parentOf(node));
                } 
				else {
					// if LRb both rotateLeft and then rotateRight
					// else if LLb only rotateRight
                    if (node == rightOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateLeft(node);
                    }
                    setColor(parentOf(node), BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    rotateRight(parentOf(parentOf(node)));
                }
            } 
			else {
                Node d = leftOf(parentOf(parentOf(node)));
				//color flip
                if (colorOf(d) == RED) {
                    setColor(parentOf(node), BLACK);
                    setColor(d, BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    node = parentOf(parentOf(node));
                } 
				else {
					// if RLb both rotateRight and then rotateLeft
					// else if RRb only rotateLeft
                    if (node == leftOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateRight(node);
                    }
                    setColor(parentOf(node), BLACK);
                    setColor(parentOf(parentOf(node)), RED);
                    rotateLeft(parentOf(parentOf(node)));
                }
            }
        }
		
		Head.color = BLACK;
	}
	
	/*
	Reduce function finds the node with the specified ID and reduces its count by m.
	If the count becomes less than zero then delete the node with that ID.
	If the node is not found does nothing.
	*/
	public static void reduce(int theID, int m){
		Node node =  binarySearch(Head,theID);
		if(node == null){
			System.out.println("0");
		}
		else{
			int temp = node.count;
			int newCount = temp-m;
			if(newCount>0){
				node.count = newCount;
				System.out.println(node.count);
			}
			else{
				System.out.println("0");
				remove(node);
			}
			
		}	
	}
	
	/*
	If the node is degree 2 then it is replaced by successor which is either degree 0 or 1
	If the node is degree 1 then its node.child is made child of node.parent removing node.
	If the node is degree 0 then node is just removed
	
	If the removed node color is black then RBT property is not satisfied and deleteFix is called.
	*/
	private static void remove(Node node){
		if(node.parent == null && node.left == null && node.right == null){
			Head = null;
			return;
		}
		
		if(node.left != null && node.right != null){
			Node next = succ(node.ID);
			node.ID = next.ID;
			node.count = next.count;
			node = next;	
		}
		
		Node n = null;
		if(node.left != null)
			n = node.left;
		else
			n = node.right;
		
		if(n != null)
			n.parent = node.parent;
		
		if(n == null && node.color == BLACK)
			deleteFix(node);
		
		if(node.parent == null)
			Head = n;
		else if(node == node.parent.left)
			node.parent.left = n;
		else
			node.parent.right = n;
		
		node.left = null;
		node.right = null;
		node.parent = null;
		
		if(n != null && node.color == BLACK)
			deleteFix(n);
	}
	
	/*
	Different delete Casses Rb0,Rb1,Rb2,Rr(n) are tackled with appropriate 
	rotation and color flips. Smilarly for Lcn.
	*/
	private static void deleteFix(Node node) {
        while (node != Head && colorOf(node) == BLACK) {
            if (node == leftOf(parentOf(node))) {
                Node n = rightOf(parentOf(node));
    
				if (colorOf(n) == RED) {
					setColor(n, BLACK);
					setColor(parentOf(node), RED);
					rotateLeft(parentOf(node));
					n = rightOf(parentOf(node));
				}
    
				if (colorOf(leftOf(n))  == BLACK &&
					colorOf(rightOf(n)) == BLACK) {
					setColor(n, RED);
					node = parentOf(node);
                } 
				else {
					if (colorOf(rightOf(n)) == BLACK) {
						setColor(leftOf(n), BLACK);
                        setColor(n, RED);
						rotateRight(n);
						n = rightOf(parentOf(node));
					}
					setColor(n, colorOf(parentOf(node)));
					setColor(parentOf(node), BLACK);
					setColor(rightOf(n), BLACK);
					rotateLeft(parentOf(node));
					node = Head;
				}
            } 
			else {
				Node n = leftOf(parentOf(node));
    
				if (colorOf(n) == RED) {
					setColor(n, BLACK);
					setColor(parentOf(node), RED);
					rotateRight(parentOf(node));
					n = leftOf(parentOf(node));
				}
    
				if (colorOf(rightOf(n)) == BLACK &&
					colorOf(leftOf(n)) == BLACK) {
					setColor(n, RED);
					node = parentOf(node);
				} 
				else {
					if (colorOf(leftOf(n)) == BLACK) {
						setColor(rightOf(n), BLACK);
						setColor(n, RED);
						rotateLeft(n);
						n = leftOf(parentOf(node));
					}
					setColor(n, colorOf(parentOf(node)));
					setColor(parentOf(node), BLACK);
					setColor(leftOf(n), BLACK);
					rotateRight(parentOf(node));
					node = Head;
                }
                
			}
		}
    
		setColor(node, BLACK);
	}
	
	/*
	BinarySearch function perfoms a binary search on the RBT and return the node.
	*/
	private static Node binarySearch(Node node, int theID){
		if(node.ID == theID)
			return node;
		else if(theID < node.ID){
			if (node.left != null)
				return binarySearch(node.left,theID);
			else
				return null;
		}	
		else if(theID > node.ID){
			if (node.right != null)
				return binarySearch(node.right,theID);
			else
				return null;
		}
		return null;
	}
	
	// rotateLeft function perfoms left rotate on the specified node.
	private static void rotateLeft(Node node){
		if(node != null){
			Node node_r = node.right;
			node.right = node_r.left;
			if(node_r.left != null)
				node_r.left.parent = node;
			node_r.parent = node.parent;
			if(node.parent == null)
				Head = node_r;
			else if(node.parent.left == node)
				node.parent.left = node_r;
			else
				node.parent.right = node_r;
			node_r.left = node;
			node.parent = node_r;
		}
	}
	
	// rotateRight function perfoms right rotate on the specified node.
	private static void rotateRight(Node node){
		if(node != null){
			Node node_l = node.left;
			node.left = node_l.right;
			if(node_l.right != null)
				node_l.right.parent = node;
			node_l.parent = node.parent;
			if(node.parent == null)
				Head = node_l;
			else if(node.parent.right == node)
				node.parent.right = node_l;
			else
				node.parent.left = node_l;
			node_l.right = node;
			node.parent = node_l;
		}
	}
	
	/*
	Helper functions colorOf,parentOf,setColor,leftOf,rightOf are 
	used to avoid any possible null pointer exceptions when node = null
	*/
	private static boolean colorOf(Node node) {
		if(node == null)
			return BLACK;
		else
			return node.color;	
    }
	private static Node parentOf(Node node) {
		if(node == null)
			return null;
		else
			return node.parent;
	}
	private static void setColor(Node node, boolean f) {
		if(node != null)
			node.color = f;
	}
	private static Node leftOf(Node node) {
		if(node == null)
			return null;
		else
			return node.left;
	}
	private static Node rightOf(Node node) {
		if(node == null)
			return null;
		else
			return node.right;
	}
	
	// count function returns count of node with given Id if present
	public static void count(int theID){
		Node node =  binarySearch(Head,theID);
		if(node == null){
			System.out.println("0");
		}
		else{
			System.out.println(node.count);			
		}	
	}
	
	private static LinkedList<Integer> list = null;
	/*
	inrange function calls buildList to bulidList with counts of nodes
	with ID range between the given range and finds the sum of these counts.	
	*/
	public static int inrange(int ID1,int ID2){
		list = new LinkedList<Integer>();
		BuildList(Head,ID1,ID2);
		int sum = 0;
		for(int i:list)
			sum = sum+i;
		System.out.println(sum);
		return sum;
	}
	
	// BuildList recursively finds the node with Ids in the given range and stores their counts.
	private static void BuildList(Node node, int ID1, int ID2){
		if (node == null) {
            return;
        }
		
        if (ID1 < node.ID) {
            BuildList(node.left, ID1, ID2);
        }
 
        if (ID1 <= node.ID && ID2 >= node.ID) {
            list.add(node.count);
        }
 
		if (ID2 > node.ID) {
            BuildList(node.right, ID1, ID2);
        }
	}
	
	public static void next(int theID){
		Node next = succ(theID);
		if(next == null)
			System.out.println("0 0");
		else
			System.out.println(next.ID+" "+next.count);	
	}
	
	// succ is an helper function which find successor of the given Id present in RBT
	private static Node succ(int theID){
		Node node = Head;
		Node next = null;
		Boolean flag = true;
		
		while(flag){
			if(theID < node.ID){
				if(node.left != null)
					node = node.left;
				else{
					next = node;
					flag = false;
				}	
			}
			else{
				if(node.right != null)
					node = node.right;
				else{
					Node temp = node;
					while(temp.parent != null && flag){
						if(temp == temp.parent.right){
							temp = temp.parent;	
						}
						else{
							next = temp.parent;
							flag = false;
						}
					}
					flag = false;
				}
			}
		}
		
		return next;
	}
	
	public static void previous(int theID){
		Node prev = prev(theID);
		if(prev == null)
			System.out.println("0 0");
		else
			System.out.println(prev.ID+" "+prev.count);	
	}
	
	// prev is an helper function which find predecessor of the given Id present in RBT
	private static Node prev(int theID){
		Node node = Head;
		Node prev = null;
		Boolean flag = true;
		
		while(flag){
			if(theID > node.ID){
				if(node.right != null)
					node = node.right;
				else{
					prev = node;
					flag = false;
				}	
			}
			else{
				if(node.left != null)
					node = node.left;
				else{
					Node temp = node;
					while(temp.parent != null && flag){
						if(temp == temp.parent.left){
							temp = temp.parent;	
						}
						else{
							prev = temp.parent;
							flag = false;
						}
					}
					flag = false;
				}
			}
		}
		
		return prev;
	}
	
	/*
	Node class define node of the RBT
	*/
	public static class Node{
		int ID;
		int count;
		Node left = null;
		Node right = null;
		Node parent = null;
		boolean color = BLACK;
	
		Node(int ID, int count){	
			this.ID = ID;
			this.count = count;
		}
	}	
}