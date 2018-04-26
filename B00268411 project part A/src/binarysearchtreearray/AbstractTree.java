
/*
* @authors Adam: B00266256
 */
package binarysearchtreearray;

import java.util.AbstractSet;

public abstract class AbstractTree<E> extends AbstractSet<E> {

    public abstract boolean add(E element);// adds to the tree

    public abstract boolean contains(Object obj);// check if parameter is in the tree and prints a boolean true/false 

    public abstract int size();// returns size of tree

    public abstract int getLeaves();// returns number of leaves

    public abstract boolean remove(Object obj);// removes an entry with the corresponding element to the parameter

    public abstract String printTree();// prints entrys in the tree

    public abstract String printInOrder();// prints entrys in the tree as an in order traversal
}
