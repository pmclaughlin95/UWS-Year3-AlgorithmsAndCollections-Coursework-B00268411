import java.util.*;
import java.io.*;
public class BinarySearchTreeArray<E extends Comparable<E>>
        extends AbstractSet<E> implements Serializable {

    protected Entry<E>[] tree; // The entries for this binary search tree
	private static final long serialVersionUID = 1234567890L;

    protected int root, // The index of root element (zero when not empty).
                  size;  // The number of elements in this binary search tree.
    
    protected static final int NIL = -1;  // represents a "null reference".
    protected static final int DEFAULT_SIZE = 16;  // For the array, tree.

    // The freeList is a queue of array positions that have been
    // emptied through the removal of an element and are now available
    // to store a new element.
    protected Queue<Integer> freeList = new LinkedList<>();

    protected int modCount; // For fail-fast iteration.
    
 	 
    
	transient Item item;
    /**
     * Initialises this BinarySearchTree object to be empty, to contain only
     * elements of type E, to be ordered by the Comparable interface, and to
     * contain no duplicate elements.
     */
    public BinarySearchTreeArray() {
        this(DEFAULT_SIZE);
    } // default constructor

    /**
     * Initialises this BinarySearchTreeArray object to be empty, to
     * contain only elements of type E, to be ordered by the Comparable
     * interface, and to contain no duplicate elements.
     *
     * @param initialCapacity the initial capacity of the array object
     * used to store the entries of this binary search tree.
     */
    public BinarySearchTreeArray(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        root = NIL;
        size = 0;
        modCount = 0;
        tree = new Entry[initialCapacity];
    } // default constructor

    /**
     * Initialises this BinarySearchTreeArray object to contain a copy of
     * specified BinarySearchTreeArray object.
     *
     * @param otherTree the specified BinarySearchTreeArray object that this
     * BinarySearchTreeArray object will be assigned a copy of.
     */
    public BinarySearchTreeArray(
            BinarySearchTreeArray<? extends E> otherTree) {
        root = otherTree.root;
        modCount = 0;
        size = otherTree.size;
        tree = new Entry[otherTree.tree.length];
        for (int i = 0; i < tree.length; i++) {
            Entry<? extends E> current = otherTree.tree[i];
            if (current != null) {
                tree[i] = new Entry(current.element, current.parent);
                tree[i].left = current.left;
                tree[i].right = current.right;
            }
        }
    } // copy constructor


    // For testing purposes, displays the whole tree array contents and
    // the values of other fields of the class (it would be better
    // to have this return a String for the application to display, but
    // the project specification suggested a display method).
    protected void displayTreeArray() {
        System.out.println("Length = " + tree.length + ", size = " + size
                + ", listSize = " + freeList.size());
        System.out.println("List = " + freeList);
        System.out.println(Arrays.toString(tree));
    }

    /**
     * Returns true if obj is an instance of Set with the same elements as
     * this binary search tree, otherwise returns false.
     *
     * @param obj the object to compare with this binary search tree
     * @return true if obj is a Set equal to this tree, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        // Note: this is an override of the equals() method for the Set
        // interface. The equals() method that compares two trees for
        // structural equality, corresponding to the one in BinarySearchTree,
        // is rewritten below as an overload of equals() that takes a
        // BinarySearchTreeArray instance as parameter, as it does not 
        // conform to the contract for equals() for a Set, which requires
        // only that the two Sets contain exactly the same set of elements.
        if (!(obj instanceof Set)) {
            return false;
        }
        Set<? extends E> other = (Set<E>) obj;
        if (size != other.size()) {
            return false;
        }
        for (E current : this) {
            if (!other.contains(current)) {
                return false;
            }
        }
        return true;
    } // method equals Object

    /**
     * Returns a hash code for this tree, the sum of the hash codes of its
     * elements.
     *
     * @return a hash code for this binary search tree
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (E current : this) {
            hash += current.hashCode();
        }
        return hash;
    }

    /**
     * Returns true if other has the same elements in the same arrangement as
     * this binary search tree, otherwise returns false.
     *
     * @param other the tree to compare with this binary search tree
     * @return true if other is tree equal to this tree, otherwise false
     */
    public boolean equals(BinarySearchTreeArray<E> other) {
        if (root == NIL || other.root == NIL) {
            return root == other.root;
        }
        if (size != other.size) {
            return false;
        }
        return equals(tree[root], other.tree[root], other.tree);
    } // method equals BinarySearchTreeArray

    /*
     * Compares the elements of this tree with those of otherTree for
     * equality.
     */
    protected boolean equals(Entry<E> p, Entry<? extends E> q,
            Entry<? extends E>[] otherTree) {
        if (p == null || q == null) {
            return p == q;
        }
        if (!p.element.equals(q.element)) {
            return false;
        }
        if (p.left != NIL && q.left != NIL && p.right != NIL
                && q.right != NIL) {
            if (equals(tree[p.left], otherTree[q.left], otherTree)
                    && equals(tree[p.right], otherTree[q.right],
                            otherTree)) {
                return true;
            }
        } else {
            return p.left == q.left && p.right == q.right;
        }
        return false;
    } // method 3-parameter equals

    /**
     * Ensures that this BinarySearchTree object contains a specified element.
     *
     * The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     * @param element the element whose presence is ensured in this
     * BinarySearchTree object.
     *
     * @return true if this BinarySearchTree object changed as a result of
     * this method call (that is, if element was actually inserted); otherwise,
     * return false.
     *
     * @throws ClassCastException if element cannot be compared to the
     * elements already in this BinarySearchTree object.
     * @throws NullPointerException if element is null.
     */
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == tree.length) {
            tree = Arrays.copyOf(tree, tree.length * 2);
        }
        if (root == NIL) {
            root = 0;
            tree[root] = new Entry<>(element, NIL);
            size++;
            modCount++;
            return true;
        } // empty tree
        else {
            Entry<E> temp = tree[root];
            int current = root;
            int comp;
            int nextSlot;
            if (freeList.size() > 0) {
                nextSlot = freeList.peek();
            } else {
                nextSlot = size;
            }
            boolean found = false;
            while (!found) {
                comp = element.compareTo(temp.element);
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (temp.left != NIL) {
                        current = temp.left;
                        temp = tree[current];
                    } else {
                        temp.left = nextSlot;
                        found = true;
                    } // temp.left == NIL
                } else if (temp.right != NIL) {
                    current = temp.right;
                    temp = tree[current];
                } else {
                    temp.right = nextSlot;
                    found = true;
                } // temp.right == NIL
            } // while
            tree[nextSlot] = new Entry<>(element, current);
            size++;
            freeList.poll();
            modCount++;
            return true;
        } // root not NIL
    } // method add

    /**
     * Determines if there is an element in this BinarySearchTreeArray
     * object that equals a specified element. The worstTime(n) is O(n) and
     * averageTime(n) is O(log n).
     *
     * @param obj the element sought in this BinarySearchTree object.
     *
     * @return true if there is an element in this BinarySearchTreeArray
     * object that equals obj; otherwise, return false.
     *
     * @throws ClassCastException if obj cannot be compared to the elements in
     * this BinarySearchTree object.
     * @throws NullPointerException if obj is null.
     */
    @Override
    public boolean contains(Object obj) {
        return getEntry(obj) != NIL;
    } // method contains

    /**
     * Returns an iterator positioned at the smallest element in this
     * BinarySearchTreeArray object.
     *
     * @return an iterator positioned at the smallest element in this
     * BinarySearchTreeArray object.
     */
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator();
    }
    
    
    // returns the number of leaves in the BST
    private int countLeaves(int nodeIndex) {
    	if (nodeIndex == NIL) return 0;
    	Entry<E> node = tree[nodeIndex];
    	if (node.isLeaf(node)) return 1;
    	int count = countLeaves(node.left);
    	count += countLeaves(node.right);
    	return count;
    	}
    //recursive call to the private method "countLeaves"
    	protected int leaves() {
    	return countLeaves(root);
    	}
    	
     
    		private void writeObject(ObjectOutputStream os) throws IOException,
            ClassNotFoundException {

    			os.defaultWriteObject();
    			
     				os.writeObject(tree);
    				os.close();
    			
    		}
    	private void readObject(ObjectInputStream is) throws IOException,
        ClassNotFoundException {

    	is.defaultReadObject();
    			is.readObject();
    		
    		
    	}		
    	

    
    
    /**
     * Returns the size of this BinarySearchTreeArray object.
     *
     * @return the size of this BinarySearchTreeArray object.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Ensures that this BinarySearchTree object does not contain a specified
     * element.
     *
     * The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     * @param obj the object whose absence is ensured in this
     * BinarySearchTreeArray object.
     *
     * @return true if this BinarySearchTree object changed as a result of
     * this method call (that is, if obj was actually removed); otherwise,
     * return false.
     *
     * @throws ClassCastException if obj cannot be compared to the elements
     * already in this BinarySearchTree object.
     * @throws NullPointerException if obj is null.
     */
    @Override
    public boolean remove(Object obj) {
        int index = getEntry(obj);
        if (index == NIL) {
            return false;
        }
        deleteEntry(index);
        modCount++;
        return true;
    } // method remove

    /**
     * Removes all of the elements from this collection. The collection will
     * be empty after this method returns.
     */
    @Override
    public void clear() {
        root = NIL;
        size = 0;
        modCount = 0;
        tree = new Entry[tree.length];
    }

    /*
     *  Finds the Entry object that houses a specified element, if  
     *  there is such an Entry.
    
     *  The worstTime(n) is O(n), and averageTime(n) is O(log n).
     *
     *  @param obj the element whose Entry is sought.
     *
     *  @return the Entry object that houses obj if there is such an
     *  Entry; otherwise, return null.  
     *
     *  @throws ClassCastException if obj is not comparable to the 
     *  elements already in this BinarySearchTree object.
     *  @throws NullPointerException if obj is null.
     */
    protected int getEntry(Object obj) {
        int comp, temp = root;
        while (temp != NIL) {
            comp = ((Comparable) obj).compareTo(tree[temp].element);
            if (comp == 0) {
                return temp;
            } else if (comp < 0) {
                temp = tree[temp].left;
            } else {
                temp = tree[temp].right;
            }
        } // while
        return NIL;
    }

    /*
     * Deletes the element in a specified Entry object from this
     * BinarySearchTreeArray.
     *
     * @param pIndex the index of Entry object whose element is to be deleted 
     * from this BinarySearchTreeArray object.
     *
     * @return the index of the Entry object that was actually deleted from 
     * this BinarySearchTreeArray object.
     */
    protected int deleteEntry(int pIndex) {
        size--;
        Entry<E> p = tree[pIndex];

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (p.left != NIL && p.right != NIL) {
            pIndex = successor(pIndex);
            Entry<E> s = tree[pIndex];
            p.element = s.element;
            p = s;
        } // p had two children

        // At this point, p has either no children or one child.
        int replacement;

        if (p.left != NIL) {
            replacement = p.left;
        } else {
            replacement = p.right;
        }

        // If p has at least one child, link replacement to p.parent.
        if (replacement != NIL) {
            tree[replacement].parent = p.parent;
            tree[pIndex] = tree[replacement];
            if (tree[replacement].left != NIL) {
                tree[tree[replacement].left].parent = pIndex;
            }
            if (tree[replacement].right != NIL) {
                tree[tree[replacement].right].parent = pIndex;
            }
            tree[replacement] = null;
            freeList.add(replacement);
            return replacement;
        } // p has at least one child  
        else if (p.parent == NIL) {
            tree[root] = null;
            freeList.add(root);
            root = NIL;
        } else {
            Entry<E> parentP = tree[p.parent];
            freeList.add(pIndex);
            tree[pIndex] = null;
            if (parentP.left == pIndex) {
                parentP.left = NIL;
            } else {
                parentP.right = NIL;
            }
        } // p has a parent but no children
        return pIndex;
    } // method deleteEntry


    /*
     * Finds the successor of a specified Entry object in this
     * BinarySearchTree.
     * The worstTime(n) is O(n) and averageTime(n) is constant.
     *
     * @param eIndex the index of the Entry object whose successor is to 
     * be found.
     *
     * @return the index of the successor of e, if e has a successor;
     * otherwise, return NIL.
     */
    protected int successor(int eIndex) {
        if (eIndex == NIL) {
            return NIL;
        } else if (tree[eIndex].right != NIL) {
            // successor is leftmost Entry in right subtree of e
            int pIndex = tree[eIndex].right;
            while (tree[pIndex].left != NIL) {
                pIndex = tree[pIndex].left;
            }
            return pIndex;
        } // e has a right child
        else {
            // go up the tree to the left as far as possible,
            // then go up to the right.
            int p = tree[eIndex].parent;
            Entry<E> ch = tree[eIndex];
            while (p != NIL && tree[p].right != NIL
                    && ch == tree[tree[p].right]) {
                ch = tree[p];
                p = tree[p].parent;
            } // while
            return p;
        } // e has no right child
    } // method successor

   /*
    * An Iterator class for the BinarySearchTreeArray class. An instance
    * of this class is returned by a call to the iterator() method.
    */
    protected class TreeIterator implements Iterator<E> {

        protected int lastReturned = NIL,
                next = NIL;
        protected final int modCountOnEntry;

        /**
         * Positions this TreeIterator to the smallest element, according to the
         * Comparable interface, in the BinarySearchTreeArray object.
         *
         * The worstTime(n) is O(n) and averageTime(n) is O(log n).
         */
        protected TreeIterator() {
            if (root != NIL) {
                next = root;
                while (tree[next].left != NIL) {
                    next = tree[next].left;
                }
            }
            modCountOnEntry = modCount;
        } // default constructor

        /**
         * Determines if there are still elements, in the BinarySearchTreeArray
         * object this TreeIterator object is iterating over, that have not been
         * accessed by this TreeIterator object.
         *
         * @return true if there are still some elements that have not been
         * accessed by this TreeIterator object; otherwise, return false.
         */
        @Override
        public boolean hasNext() {
            return next != NIL;
        } // method hasNext

        /**
         * Returns the element in the Entry this TreeIterator object was
         * positioned at before this call, and advances this TreeIterator
         * object. The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         * @return the element this TreeIterator object was positioned at 
         * before this call.
         *
         * @throws NoSuchElementException if this TreeIterator object was not
         * positioned at an Entry before this call.
         * @throws ConcurrentModificationException if the binary search
         * tree has been modified since the previous call of the next() 
         * method by a call to one of the mutator methods of the binary
         * search tree.
         */
        @Override
        public E next() {
            if (modCountOnEntry != modCount) {
                throw new ConcurrentModificationException();
            }
            if (next == NIL) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = successor(next);
            return tree[lastReturned].element;
        } // method next

        /**
         * Removes the element returned by the most recent call to this
         * TreeIterator object's next() method. The worstTime(n) is O(n) and
         * averageTime(n) is constant.
         *
         * @throws IllegalStateException - if this TreeIterator's next() 
         * method was not called before this call, or if this TreeIterator's
         * remove() method was called between the call to the next() method 
         * and this call.
         */
        @Override
        public void remove() {
            if (lastReturned == NIL) {
                throw new IllegalStateException();
            }
            if (modCountOnEntry != modCount) {
                throw new ConcurrentModificationException();
            }
            int deleted = deleteEntry(lastReturned);
            if (next == deleted) {
                next = lastReturned;
            }
            lastReturned = NIL;
        } // method remove    

    } // class TreeIterator

    /*
     * A class to represent the entries of the BinarySearchTreeArray
     * class.
     */
    protected static class Entry<E> implements Serializable {
    	private static final long serialVersionUID = 1234567890L;

        protected E element;
 
 
        
        protected int left = NIL,
                right = NIL,
                parent;

        /**
         * Initialises this Entry object.
         *
         * This default constructor is defined for the sake of subclasses
         * of the BinarySearchTreeArray class.
         */
        public Entry() {
            this(null, NIL);
        }

        /**
         * Initialises this Entry object from element and parent.
         */
        public Entry(E element, int parent) {
            this.element = element;
            this.parent = parent;
        } // constructor

        /**
         * For testing and debugging purposes.
         * 
         * @return a String representation of the state of this Entry.
         */
        @Override
        public String toString() {
            return "Element=" + element + " parent=" + parent
                    + " left=" + left + " right=" + right;
        }
        public boolean isLeaf(Entry <E> Entry) {
        	if(Entry == null) 
        		return false;
        	if (Entry.left == NIL && Entry.right == NIL)
        		return true;
        	return false;
        	}
    } // class Entry
    
    
}