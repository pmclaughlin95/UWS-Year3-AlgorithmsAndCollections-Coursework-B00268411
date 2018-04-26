/**
 * @authors Adam: B00266256, Chloe: B00286864, Paul: B00268411
 */
package binarysearchtreearray;

import java.util.*;

public class BinarySearchTree<E> extends AbstractTree<E> {

    //Private variables:
    protected Entry<E> root;    //The root node (Entry) of our tree.
    protected int size; //The number of entries in our tree.

    //Default constructor...
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    //Adds an entry to the tree defined by the element parameter...
    @Override
    public boolean add(E element) {
        Entry<E> temp = root;

        if (temp != null) {
            int comp = ((Comparable) element).compareTo(temp.element);
            if (comp == 0) {
                return false;   //duplicate value
            } else if (comp < 0) {
                if (temp.left == null) {
                    size++;
                    temp.left = new Entry<E>(element, temp);
                } else {
                    return recursiveADD(element, temp.left, root);
                }

            } else if (comp > 0) {
                if (temp.right == null) {
                    size++;
                    temp.right = new Entry<E>(element, temp);
                } else {
                    return recursiveADD(element, temp.right, root);
                }
            }
        } else {
            root = new Entry<E>(element, null);
            size++;
        }
        return false;
    }

    //A recursive method for add elements to the BST...
    private boolean recursiveADD(E element, Entry<E> tempe, Entry<E> parent) {
        Entry<E> temp = tempe;

        if (temp != null) {
            int comp = ((Comparable) element).compareTo(temp.element);
            if (comp == 0) {
                return false;
            } else if (comp < 0) {
                if (temp.left == null) {
                    size++;
                    temp.left = new Entry<E>(element, temp);
                } else {
                    return recursiveADD(element, temp.left, temp.left.parent);
                }
            } else if (comp > 0) {
                if (temp.right == null) {
                    size++;
                    temp.right = new Entry<E>(element, temp);
                } else {
                    return recursiveADD(element, temp.right, temp.right.parent);
                }
            }
        }
        return false;
    }

    //Returns true if an element matching the parameter is found...
    @Override
    public boolean contains(Object o) {
        return getEntry(o) != null;
    }

    //Returns the size of our tree...
    @Override
    public int size() {
        return size;
    }

    //Returns the number of leaves in our tree...
    @Override
    public int getLeaves() {
        return getLeavesR(root);
    }

    //Recursive traversal of tree to find leaves... (used by our public function call)
    private int getLeavesR(Entry node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        } else {
            return getLeavesR(node.left) + getLeavesR(node.right);
        }
    }

    //Returns the contents of our tree as a string...
    @Override
    public String printTree() {
        
         String arrayString = new String();
         BinarySearchTree.TreeIterator itr = (BinarySearchTree.TreeIterator) iterator();
        
        while (itr.hasNext()) {
            //arrayString += i + ") ";
            arrayString += "Element: ";
            arrayString += (Integer) itr.next.element;
            
            arrayString += "  Parent: ";
            if(itr.next.parent!= null)
            {
                  if(itr.next.parent.element!= null)
                arrayString += (Integer)itr.next.parent.element;
            }   
            else
                 arrayString += "null";

            arrayString += "  Left: ";
            if(itr.next.left != null)
            {
                if(itr.next.left.element != null)
                arrayString += (Integer)itr.next.left.element;
            } 
            else
                arrayString += "null";

            arrayString += "  Right: ";
            if(itr.next.right != null)
            {
                    if(itr.next.right.element != null)
            arrayString += (Integer)itr.next.right.element;
            } 
            else
                arrayString += "null";
            
            arrayString += System.lineSeparator();
            
              itr.next();
        }
        return arrayString;
    }

    //Returns an in-order traversal of our tree... 
    @Override
    public String printInOrder() {
        BinarySearchTree.TreeIterator itr = (BinarySearchTree.TreeIterator) iterator();
        String printable = new String();

        while (itr.hasNext()) {
            printable += itr.next.element;
            printable += ", ";
            itr.next();
        }
        return printable;
    }

    @Override
    public Iterator<E> iterator() {
        return new TreeIterator();
    }

    @Override
    public boolean remove(Object obj) {
        Entry<E> e = getEntry(obj);
        if (e == null) {
            return false;
        }
        deleteEntry(e);
        return true;
    }

    private Entry<E> getEntry(Object obj) {
        int comp;
        if (obj == null) {
            throw new NullPointerException();
        }
        Entry<E> e = root;
        while (e != null) {
            comp = ((Comparable) obj).compareTo(e.element);
            if (comp == 0) {
                return e;
            } else if (comp < 0) {
                e = e.left;
            } else {
                e = e.right;
            }
        }
        return null;
    }

    /**
     * Deletes the element in a specified Entry object from this
     * BinarySearchTree.
     *
     * @param p � the Entry object whose element is to be deleted from this
     * BinarySearchTree object.
     *
     * @return the Entry object that was actually deleted from this
     * BinarySearchTree object.
     *
     */
    protected Entry<E> deleteEntry(Entry<E> p) {
        size--;

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (p.left != null && p.right != null) {
            Entry<E> s = successor(p);
            p.element = s.element;
            p = s;
        } // p had two children

        // At this point, p has either no children or one child.
        Entry<E> replacement;

        if (p.left != null) {
            replacement = p.left;
        } else {
            replacement = p.right;
        }

        // If p has at least one child, link replacement to p.parent.
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }
        } // p has at least one child  
        else if (p.parent == null) {
            root = null;
        } else if (p == p.parent.left) {
            p.parent.left = null;
        } else {
            p.parent.right = null;
        } // p has a parent but no children
        return p;
    } // method deleteEntry

    /**
     * Finds the successor of a specified Entry object in this BinarySearchTree.
     * The worstTime(n) is O(n) and averageTime(n) is constant.
     *
     * @param e - the Entry object whose successor is to be found.
     *
     * @return the successor of e, if e has a successor; otherwise, return null.
     *
     */
    protected Entry<E> successor(Entry<E> e) {
        if (e == null) {
            return null;
        } else if (e.right != null) {
            // successor is leftmost Entry in right subtree of e
            Entry<E> p = e.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;

        } // e has a right child
        else {

            // go up the tree to the left as far as possible, then go up
            // to the right.
            Entry<E> p = e.parent;
            Entry<E> ch = e;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            } // while
            return p;
        } // e has no right child
    } // method successor

    protected class TreeIterator implements Iterator<E> {

        protected Entry<E> lastReturned = null, next;
        int modCount;
        int expectedModCount;

        /**
         * Positions this TreeIterator to the smallest element, according to the
         * Comparable interface, in the BinarySearchTree object. The
         * worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected TreeIterator() {
            next = root;
            if (next != null) {
                while (next.left != null) {
                    next = next.left;
                }
            }
            int expectedModCount = modCount;
        } // default constructor

        public Boolean checkForModification() {
            if (expectedModCount != modCount) {
                System.out.println("Error:  concurrent modification");
                return true;
            } else {
                return false;
            }

        }

        /**
         * Determines if there are still some elements, in the BinarySearchTree
         * object this TreeIterator object is iterating over, that have not been
         * accessed by this TreeIterator object.
         *
         * @return true - if there are still some elements that have not been
         * accessed by this TreeIterator object; otherwise, return false.
         *
         */
        public boolean hasNext() {
            if (checkForModification()) {
                return false;
            }

            return next != null;
        } // method hasNext

        /**
         * Returns the element in the Entry this TreeIterator object was
         * positioned at before this call, and advances this TreeIterator
         * object. The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         * @return the element this TreeIterator object was positioned at before
         * this call.
         *
         * @throws NoSuchElementException - if this TreeIterator object was not
         * positioned at an Entry before this call.
         *
         */
        public E next() {
            if (checkForModification()) {
                return null;
            }

            if (next == null) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = successor(next);
            return lastReturned.element;

        } // method next

        /**
         * Removes the element returned by the most recent call to this
         * TreeIterator object�s next() method. The worstTime(n) is O(n) and
         * averageTime(n) is constant.
         *
         * @throws IllegalStateException - if this TreeIterator�s next() method
         * was not called before this call, or if this TreeIterator�s remove()
         * method was called between the call to the next() method and this
         * call.
         *
         */
        public void remove() {
            if (checkForModification()) {
                return;
            }

            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            if (lastReturned.left != null && lastReturned.right != null) {
                next = lastReturned;
            }
            deleteEntry(lastReturned);
            lastReturned = null;

            modCount++;
        } // method remove     

    } // class TreeIterator

    //Entry Class used to define an entry in our tree, contains an element and a position...
    private class Entry<E> {

        //Private variables:
        private E element;  //Generic variable used to define the element.
        private Entry<E> left, right, parent;    //Defines the entry's position in the tree.

        //Default constructor...
        public Entry() {
            //...
        }

        //Constructor w. parameters...
        public Entry(E element, Entry<E> parent) {
            this.element = element;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
    } // class Entry

} // class BinarySearchTree

