import java.util.ArrayDeque;
import java.util.Deque;

/*
*/
public class BinarySearchTree <Key extends Comparable<Key>, Value> {

    private class Node{
        private Key key;
        private Value value;
        private Node left = null;
        private Node right = null;
        private int count;

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.count = 1;
        }
    }

    private Node root = null;
    
    // #interface
    public void put(Key key, Value value){
        // if key is already present replace //
        root = put(root,key,value);
    }

    private Node put(Node x, Key key, Value val){

        if(x == null) return new Node(key,val);

        int cmp = key.compareTo((Key) x.key);

        if      (cmp < 0)   x.left = put(x.left,key,val);
        else if (cmp > 0)   x.right = put(x.right,key,val);
        else                x.value = val;
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public Value get(Key key){
        Node x = root;
        while(x != null){
            int cmp = key.compareTo((Key) x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.value;
        }
        return null;
    }

    public Value getMin(){
        Node x = root;
        while(x.left != null) x = x.left;
        return x.value;
    }

    private Node min(Node x){
        while(x.left != null) x = x.left;
        return x;
    }

    public Value getMax(){
        Node x = root;
        while(x.right != null) x = x.right;
        return x.value;
    }

    public Key floor(Key k){
        // key just less than or equal to 'k'.
        Node x = floor(root,k);
        if(x == null) return null;
        return x.key;
    }

    // order statistics

    private Node floor(Node x, Key k){

        if(x == null) return null; // if refrenced passed is null
        int cmp = k.compareTo(x.key); // compare current node

        if (cmp == 0)   return x; // if its k == current node
        if (cmp < 0)    return floor(x.left,k); // if less-> search left subtree

        Node t = floor(x.right,k);// if both above cases fail, check right subtree
        if(t != null) return t;// if found in right subtree i.e., any key <= k but > parent node.
        else return x; // if not any of the case than current node is the floor
    }

    public Key ceiling(Key k){
        // key just greater than or equal to 'k'
        Node x = ceiling(root,k);
        if(x == null) return null;
        return x.key;
    }

    private Node ceiling(Node x, Key k){
        if(x == null) return null;
        int cmp = k.compareTo(x.key);

        if (cmp == 0)   return x;
        if (cmp > 0)    return ceiling(x.right,k);

        Node t = ceiling(x.left,k);
        if(t != null) return t;
        else return x;
    }

    private int size(Node x){
        if(x == null)   return 0;
        else return x.count;
    }

    public int size(){
        return size(root);
    }

    public int rank(Key k){
        return rank(root,k);
    }

    private int rank(Node x, Key k){
        // number of keys < k
        if (x == null) return 0;
        int cmp = k.compareTo(x.key);

        if      (cmp < 0)   return rank(x.left,k); // left recursive call
        else if (cmp > 0)   return 1 + size(x.left) + rank(x.right,k); // k > x.key then 1(size of curr node) + size(x.left) + recursive call to right subtree
        else                return size(x.left); // k == x.key => size of left subtree

    }

    // traversals:

    public Iterable<Key> keys(){
        // traverse left subtre
        // enque key
        // traverse right subtree
        // *returns keys in ascending order*

        ArrayDeque<Key> queue = new ArrayDeque<>();
        inorder(root, queue);
        return queue;

    }

    private void inorder(Node x, Deque<Key> queue){
        if(x == null) return;
        inorder(x.left,queue);
        queue.add(x.key);
        inorder(x.right,queue);
    }

    public Iterable<Key> keysLevelOrder(){
        ArrayDeque<Key> queue = new ArrayDeque<>();
        levelorder(root,queue);
        return queue;
    }

    private void levelorder(Node x, Deque<Key> queue){

        ArrayDeque<Node> q = new ArrayDeque<>();

        Node node;
        if(x == null) return;
        q.addLast(x);
        while (!q.isEmpty()){

            node = q.pollFirst();
            queue.addLast(node.key);
            if(node.left != null)  q.addLast(node.left);
            if(node.right != null) q.addLast(node.right);

        }

    }

    public void deleteMin(){
        root = deleteMin(root);
    }

    private Node deleteMin(Node x){
        // recursive
        if(x.left == null) return x.right;
        //recursive update call to left node
        x.left = deleteMin(x.left);
        // update x.count
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void delete(Key k){
        root = hibbardDelete(root,k);
    }

    private Node hibbardDelete(Node x, Key k){
        // replace node to be deleted with min(rightSubtree) and deleteMin(rightSubtree)
        // and also update size of updated(deleted) node
        if(x == null)   return null;
        int cmp = k.compareTo(x.key);
        if      (cmp < 0)   x = hibbardDelete(x.left, k);
        else if (cmp > 0)   x = hibbardDelete(x.right, k);
        else{

            if(x.left == null)  return x.right; // no left left child
            if(x.right == null) return x.left; // no right child

            // replace with the successor
            Node temp = x;
            x = min(temp.right);
            x.right = deleteMin(temp.right);
            x.left = temp.left;
        }
        x.count = 1 + size(x.right) + size(x.left); // update subtree counts
        return x; // return updated node and temp (which holding reference of node to be deleted) will be garbage collected
    }

}
