/**
 * @authors Adam: B00266256, Chloe: B00286864, Paul: B00268411
 */
package binarysearchtreearray;

import java.util.Iterator;

public class BinarySearchTreeArray<E> extends AbstractTree<E> {

    //Private variables:
    protected Entry<E>[] tree; //Array of entries, defines our tree as an array.
    protected int size; //The number of entries in our tree.
    protected int root; //The root entry in our tree.

    //Default constructor...    
    public BinarySearchTreeArray() {
        root = -1;
        size = 0;
        tree = new Entry[50];
    }

    //Adds an entry to the array using the paramenter as its element...
    @Override
    public boolean add(E element) {

        if (element == null) {
            throw new NullPointerException();
        }

        if (root == -1) {
            root = 0;
            tree[0] = new Entry<E>(element, -1);
            size++;
            return true;
        } else {

            int temp = root;
            int comp;

            while (true) {
                comp = ((Comparable) element).compareTo(tree[temp].element);
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (tree[temp].left != -1) {
                        temp = tree[temp].left;
                    } else {
                        tree[size] = new Entry<E>(element, temp);
                        tree[temp].left = size;
                        size++;

                        return true;
                    } // temp.left == null
                } else if (tree[temp].right != -1) {
                    temp = tree[temp].right;
                } else {
                    tree[size] = new Entry<E>(element, temp);
                    tree[temp].right = size;
                    size++;

                    return true;
                }
            }
        }
    } // method add  

    //Check every element in the tree using the iterator... 
    //Returns true if an element matching the parameter is found...
    @Override
    public boolean contains(Object o) {
        return getEntry(o) != null;
    }

    //Returns the number of entries in our tree array...
    @Override
    public int size() {
        return size;
    }

    //Determines and returns the number of leaves in our tree array...
    @Override
    public int getLeaves() {
        int leafCounter = 0;
        TreeIterator itr = (TreeIterator) iterator();

        while (itr.hasNext()) {
            if (tree[itr.next].left == -1 && tree[itr.next].right == -1) {
                leafCounter++;
            }
            itr.next();
        }
        return leafCounter;
    }

    //Prints the tree in the order stored in the array...
    //Plus details for debugging...
    @Override
    public String printTree() {
        String arrayString = new String();

        for (int i = 0; i < size; i++) {
            arrayString += i + ") ";
            arrayString += "Element: ";
            arrayString += (Integer) tree[i].element;
            arrayString += "  Parent: ";
            arrayString += (Integer) tree[i].parent;

            arrayString += "  Left: ";
            arrayString += (Integer) tree[i].left;

            arrayString += "  Right: ";
            arrayString += (Integer) tree[i].right;
            arrayString += System.lineSeparator();
        }
        return arrayString;
    }

    //Uses iterator to print an In-Order traversal of the tree...
    @Override
    public String printInOrder() {
        TreeIterator itr = (TreeIterator) iterator();
        String printable = new String();

        while (itr.hasNext()) {
            printable += tree[itr.next].element;
            printable += ", ";
            itr.next();
        }
        return printable;
    }

    @Override
    public boolean remove(Object obj) {
        Entry<E> e = getEntry(obj);
        if (e == null) {
            return false;
        }
        deleteEntry(e);
        return true;
    } // method remove

    private Entry<E> getEntry(Object obj) {

        TreeIterator itr = (TreeIterator) iterator();

        while (itr.hasNext()) {

            if (((Comparable) obj).compareTo(tree[itr.next].element) == 0) {
                return tree[itr.next];
            }

            itr.next();
        }
        return null;

    }

    private Entry<E> deleteEntry(Entry<E> p) {
        size--;

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (p.left != -1 && p.right != -1) {
            Entry<E> s = successor(p);
            p.element = s.element;
            p = s;
        } // p had two children

        // At this point, p has either no children or one child.
        Entry<E> replacement;

        if (p.left != -1) {
            replacement = tree[p.left];
        } else {
            replacement = tree[p.right];
        }

        // If p has at least one child, link replacement to p.parent.
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == -1) {
                tree[root] = replacement;
            } else if (p == tree[tree[p.parent].left]) {
                tree[tree[p.parent].left] = replacement;
            } else {
                tree[tree[p.parent].right] = replacement;
            }
        } // p has at least one child  
        else if (p.parent == -1) {
            tree[root] = null;
        } else if (p == tree[tree[p.parent].left]) {
            tree[tree[p.parent].left] = null;
        } else {
            tree[tree[p.parent].right] = null;
        } // p has a parent but no children
        return p;
    } // method deleteEntry

    private Entry<E> successor(Entry<E> e) {
        if (e == null) {
            return null;
        } else if (e.right != -1) {
            // successor is leftmost Entry in right subtree of e
            Entry<E> p = tree[e.right];
            while (p.left != -1) {
                p = tree[p.left];
            }
            return p;

        } // e has a right child
        else {

            // go up the tree to the left as far as possible, then go up
            // to the right.
            Entry<E> p = tree[e.parent];
            Entry<E> ch = e;
            while (p != null && ch.element == tree[p.right].element) {
                ch = p;
                p = tree[p.parent];
            } // while
            return p;
        } // e has no right child
    }

    //Iterable to return our iterator...
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator(root);
    }

    //TreeIterator class used to iterate through our array...
    public class TreeIterator<E> implements Iterator {

        //Private variables:
        private int next;   //stores the array position of the next in-order entry.

        //Default constructor...
        public TreeIterator() {
            //...
        }

        //Constructor w. root parameter...
        public TreeIterator(int root) {
            next = root;

            //Checks if tree is empty...
            if (next == -1) {
                return;
            }

            //Initialises 'next' as the tree's leftmost element... 
            while (tree[next].left != -1) {
                next = tree[next].left;
            }
        }

        public boolean hasNext() {
            return next != -1;
        }

        public E next() {
            int r = next;

            // If you traverse the right tree, do so...
            // Otherwise, if you traverse the left tree, do so...
            // Otherwise, move back up the tree until you can...
            if (tree[next].right != -1) {
                next = tree[next].right;
                while (tree[next].left != -1) {
                    next = tree[next].left;
                }
                return (E) tree[r];
            }

            while (true) {
                if (tree[next].parent == -1) {
                    next = -1;
                    return (E) tree[r];
                }
                if (tree[tree[next].parent].left == next) {
                    next = tree[next].parent;
                    return (E) tree[r];
                }
                next = tree[next].parent;
            }
        }
    } //class TreeIterator

    //Entry Class used to define an entry in our tree, contains an element and a position...
    private class Entry<E> {

        //Private variables:
        private E element;  //Generic variable used to define the element.
        private int left, right, parent;    //Defines the entry's position in the tree.

        //Default constructor...
        public Entry() {
            //...
        }

        //Constructor w. parameters...
        public Entry(E element, int parent) {
            this.element = element;
            this.parent = parent;
            this.left = -1;
            this.right = -1;
        }
    } // class Entry.

}//Binary Tree Class
