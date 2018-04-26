/**
 * @authors Adam: B00266256, Chloe: B00286864, Paul: B00268411
 */
package binarysearchtreearray;

public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AbstractTree thisTree;
        int option;
        //Introductory Message...
        System.out.println("Welcome To The BinarySearchTreeArray Java Application by B00286864, B00266256 & B00268411.");
        System.out.println("Please decide between using an array-based implementation or a regular binary search tree implementation.");
        //Loop through menu until quit option selected...
        do {
            //Print list of options...
            System.out.println("0: quit");
            System.out.println("1: use an array-based Binary Search Tree");
            System.out.println("2: use a regular Binary Search Tree");
            //Get user input for option...
            option = Input.getInteger("option: ");
            switch (option) {
                case 0:
                    //Quits the program...
                    System.out.println("Quitting Program...");
                    break;
                case 1:
                    //Defines the tree as an array-implemented BST...
                    thisTree = new BinarySearchTreeArray();
                    System.out.println("Using The Array Implementation...");
                    //Calls operations using that definition...
                    run(thisTree);
                    break;
                case 2:
                    //Defines the tree as a regular BST...
                    thisTree = new BinarySearchTree();
                    System.out.println("Using The Regular Implementation...");
                    //Calls operations using that definition...
                    run(thisTree);
                    break;
                default:
                    //Invalid option selected, informs user...
                    System.out.println("Invalid Option...");
                    break;
            }
        } while (option != 0);
    }

    //Calls all relevant operations based on user input:
    private static void run(AbstractTree thisTree) {
        int option;
        //Loop through menu until quit option selected...
        do {
            //Print list of options...
            System.out.println("0: return");
            System.out.println("1: add");
            System.out.println("2: contains");
            System.out.println("3: count entries");
            System.out.println("4: count leaves");
            System.out.println("5: remove");
            System.out.println("6: display");
            System.out.println("7: display in order");
            //Get user input for option...
            option = Input.getInteger("option: ");
            switch (option) {
                case 0:
                    //Quits the program...
                    System.out.println("Returning To Main Menu...");
                    break;
                case 1:
                    //Appends the sequence...
                    System.out.println("Adding To The Tree...");
                    Object element = Input.getInteger("New Value: ");
                    thisTree.add(element);
                    break;
                case 2:
                    System.out.println("Checking For A Specific Value...");
                    Integer searchElement = Input.getInteger("element to be checked: ");
                    System.out.println("Tree Contains Element: " + thisTree.contains(searchElement));
                    break;
                case 3:
                    //Appends the sequence...
                    System.out.println("Printing The Size Of The Tree...");
                    System.out.println("No. Of Entries: " + thisTree.size());
                    break;
                case 4:
                    System.out.println("Printing The Number Of Leaves...");
                    System.out.println("No. Of Leaves: " + thisTree.getLeaves());
                    break;
                case 5:
                    System.out.println("Removing An Element...");
                    Integer removeElement = Input.getInteger("element to be checked: ");
                    System.out.println("Element Removed?: " + thisTree.remove(removeElement));
                    break;
                case 6:
                    System.out.println("Printing The Tree...");
                    System.out.println(thisTree.printTree());
                    break;
                case 7:
                    System.out.println("Printing The Tree In Order Of Insertion...");
                    System.out.println(thisTree.printInOrder());
                    break;
                default:
                    //Invalid option, alerts user...
                    System.out.println("Invalid Option...");
            }
        } while (option != 0);
    }
}
