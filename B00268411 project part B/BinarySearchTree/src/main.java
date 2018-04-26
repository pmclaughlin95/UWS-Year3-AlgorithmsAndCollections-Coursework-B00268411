



import java.util.*;
import java.io.*;
/**
 *
 * A class to exercise the methods of the BinarySearchTreeArray class.
 *
 * @author Paul McLaughlin- B00268411
 */
public class main {
    /**
     * @param args the command line arguments
     */
	 public static void main(String[] args) {
  	        Set<Item> rbTree = new TreeSet<>();  // a Set implemented using a red-black tree
	        Set<Item> hTable = new HashSet<>(); // a Set implemented using a hash-table
	         BinarySearchTreeArray<Item> bsTree = new BinarySearchTreeArray<>();
         	List<Item> data = new ArrayList<>();
         	
         	

         	
	        int option;
	     
	        do {
	            //Print list of options...
	            System.out.println("0: Quit");
	            System.out.println("1: Add");
	            System.out.println("2: Height of trees");
	            System.out.println("3: Compare");
	            System.out.println("4: Display");
	            System.out.println("5: Count leaves");
	            System.out.println("6: Clear");
	            System.out.println("7: Serialise");
	            System.out.println("8: Deserialise");
	            System.out.println("9: Size of sets");

	            //Get user input for option...
	            option = Input.getInteger("option: ");
	            switch (option) {
	                case 0:
	                    //Quits the program...
	                    System.out.println("Quitting Program...");
	                    break;
	                case 1:
	                	Random r = new Random();
	                	while (rbTree.size() < 100) {
	                	    Integer current = r.nextInt(1000000)*2 + 1;  // to guarantee that the number is odd
	                	    if (rbTree.add(new Item(current))) { // as current might be a duplicate
	                	        hTable.add(new Item(current));
	                	        bsTree.add(new Item(current));
	                	        data.add(new Item(current));
	                	    }
	                	}
	                    break;
	                case 2:
	                    System.out.println("height of red-black tree is : " + height(rbTree));
 	                    System.out.println("height of binary search tree is : " +height(bsTree));

	                    break;
	                case 3:
	                	Item.resetCompCount();
	                	for (Item target : data) {
	                	    rbTree.contains(target);
	                	}
	                	long rbTreeCompSuccess = Item.getCompCount();
	                	Item.resetCompCount();
	                	for (Item target : data) {
	                	   hTable.contains(target);
	                	}
	                	long hTableCompSuccess = Item.getCompCount();
	                	Item.resetCompCount();
	                	for (Item target : data) {
	                	   bsTree.contains(target);
	                	}
	                	long bsTreeCompSuccess = Item.getCompCount();
	                	
	                	System.out.println(rbTreeCompSuccess);
	                	System.out.println(hTableCompSuccess);
	                	System.out.println(bsTreeCompSuccess);
	                	break;
	                case 4:
	                	System.out.println("Hash table: "  + hTable);
	                	
	                	System.out.println("red-black tree: " + rbTree);
	                	
	                	System.out.println("Binary search tree: " + bsTree);
	                	break;
	                	
	                case 5:
	                	System.out.println("The number of leaves in the binary search tree is: " + bsTree.leaves());
	                	
	                case 6:
	                	hTable.clear();
	                	rbTree.clear();
	                	bsTree.clear();
	                	break;
	                	
	                case 7:
	                	try {
	                     	FileOutputStream fos = new FileOutputStream("bst.txt");
	                     
	            			ObjectOutputStream oos = new ObjectOutputStream(fos);

  	            			oos.writeObject(bsTree);
	            			oos.close();
	            		} catch (Exception e) {
	            			e.printStackTrace();	
	            		}
	                	break;
	                case 8:
	                	try {
	                		FileInputStream fis = new FileInputStream("bst.txt");
	            			ObjectInputStream ois = new ObjectInputStream(fis);
	            			 BinarySearchTreeArray<Item> copy = ( BinarySearchTreeArray<Item>) ois.readObject();
	            			ois.close();
	            			System.out.println(copy);
	            		} catch (Exception e) {
	            			e.printStackTrace();	
	            		}
	                	break;
	                case 9: 
	                	
	                	System.out.println(bsTree.size());
	                	System.out.println(rbTree.size());
	                	System.out.println(hTable.size());
	                	break;
	                	
	                default:
	                    //Invalid option selected, informs user...
	                    System.out.println("Invalid Option...");
	                    break;
	            }
	        } while (option != 0);
	    }
	 private static long height (Set<Item> tree) {
		 long maxComp = 0;
		 for (Item current : tree) {
		 Item.resetCompCount();
		 tree.contains(current);
		 if (maxComp < Item.getCompCount()) {
		 maxComp = Item.getCompCount();
		 }
		 }
		 return maxComp-1;
		 }
	 
	 
	 }
