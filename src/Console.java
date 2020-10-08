import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Pattern;

/**
 * This class represents the console.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class Console {
    public static final String UNORDERED_PATH = "files/small dictionary.txt";
    public static final String ORDERED_PATH = "files/ordered small dictionary.txt";

    /**
     * Creates a scanner object for user's all input, a new WordNode root, and
     * calls showMenu method to start the dictionary search app.
     *
     * @param args  the arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        WordNode root = null; // initially the tree is null
        showMenu(scan, root);
    }

    /**
     * Displays options and ask for user input.
     *
     * @param scan  the scanner for user input
     * @param root  the root node of the binary search tree
     */
    private static void showMenu(Scanner scan, WordNode root) {
        System.out.println("1. Load dictionary from unordered pairs");
        System.out.println("2. Load dictionary from serialized tree");
        System.out.println("3. Define");
        System.out.println("4. Save dictionary");
        System.out.println("5. Exit");

        int num = scan.nextInt();
        scan.nextLine();
        System.out.println();

        switch (num) {
            case 1: root = loadUnorderedPairs();
                    showMenu(scan, root);
                    break;

            case 2: root = loadSerializedTree();
                    showMenu(scan, root);
                    break;

            case 3: define(scan, root);
                    showMenu(scan, root);
                    break;

            case 4: saveDictionary(root);
                    showMenu(scan, root);
                    break;

            case 5: exit();
                    break;

            default: showErrorMessage();
                     showMenu(scan, root);
                     break;
        }
    }

    /**
     * Execute the algorithms above that read your input file, sort the words
     * in the file, create a new binary search tree based on the words and adds
     * a definition to the tree for every word.
     */
    public static WordNode loadUnorderedPairs() {
        WordNode root = null;

        try (Scanner scan = new Scanner(new File(UNORDERED_PATH))) {
            // scan the file and put each word-definition pair into a list,
            // this will generate a unsorted list of word-definition pairs
            // e.g. D, J, B, E, F, K, C, A, H, G, I
            ArrayList<WordNode> wordList = new ArrayList<>();
            readFile(scan, wordList);
            scan.close();

            // sort the list
            // e.g. A, B, C, D, E, F, G, H, I, J, K
            Collections.sort(wordList);

            // generate a insertion order list for the binary search tree
            ArrayList<WordNode> insertionOrderList = new ArrayList<>();
            insertionOrderList = generateInsertionOrderList(wordList, insertionOrderList);

            // loop the list insertionOrderList and add each element to
            // generate a binary search tree
            root = insertionOrderList.get(0);
            for(int i = 1; i < insertionOrderList.size(); i++) {
                root.insert(insertionOrderList.get(i));
            }
        } catch (FileNotFoundException message) {
            System.out.println("Error: " + message);
        }

        return root; // by now the binary search tree has been created
    }

    /**
     * Generates a insertion order list recursively.
     *
     * @param wordList  the original arraylist
     * @param insertionOrderList  the new list
     * @return the newly created list
     */
    public static ArrayList<WordNode> generateInsertionOrderList(ArrayList wordList, ArrayList insertionOrderList) {
        int middle = (wordList.size() - 1) / 2;
        WordNode middleNode = (WordNode) wordList.get(middle);
        insertionOrderList.add(middleNode);

        int leftLowIndex = 0;
        int leftHighIndex = middle - 1;

        int rightLowIndex = middle + 1;
        int rightHighIndex = wordList.size() - 1;

        if (leftLowIndex <= leftHighIndex) {
            ArrayList<WordNode> leftList = new ArrayList<>();
            for (int i = 0; i < middle; i++) {
                leftList.add((WordNode) wordList.get(i));
            }
            generateInsertionOrderList(leftList, insertionOrderList);
        }

        if (rightLowIndex <= rightHighIndex) {
            ArrayList<WordNode> rightList = new ArrayList<>();
            for (int i = middle + 1; i < wordList.size(); i++) {
                rightList.add((WordNode) wordList.get(i));
            }
            generateInsertionOrderList(rightList, insertionOrderList);
        }

        return insertionOrderList;
    }

    /**
     * Read your new output file from top to bottom and add each line from
     * the file to the tree, reading from the top of the file to the bottom.
     * If you have written your saveDictionary algorithm correctly this should
     * reassemble your perfectly balanced tree. Then the user can continue
     * to look up words as they like.
     */
    public static WordNode loadSerializedTree() {
        WordNode root = null;

        try {
            Scanner scan = new Scanner(new File(ORDERED_PATH));

            ArrayList<WordNode> wordList = new ArrayList<>();
            readFile(scan, wordList);
            scan.close();

            root = wordList.get(0);

            for (int i = 1; i < wordList.size(); i++) {
                root.insert(wordList.get(i));
            }
        } catch (FileNotFoundException message) {
            System.out.println("Error when reading: " + message);
        }

        return root;
    }

    private static void readFile(Scanner scan, ArrayList<WordNode> wordList) {
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            String[] eachPair = nextLine.split(Pattern.quote(": "));
            WordNode wordNode = new WordNode(eachPair[0], eachPair[1]);
            wordList.add(wordNode);
        }
    }

    /**
     * prompt the user for a word and then provide either a definition of the
     * word or the following message if the word is not in the tree:
     * "Word not found!" This will require you to perform a look-up in the
     * tree given a word provided by the user.
     */
    public static void define(Scanner scan, WordNode root) {
        System.out.println("Enter a word to search: ");
        String searchWord = scan.nextLine();

        WordNode word = root.contains(searchWord);

        if (word != null) {
            System.out.println("Definition: " + word.getDefinition() + "\n");
        } else {
            System.out.println("Sorry, word not found...\n");
        }
    }

    /**
     * Save each word-definition pair in the tree back to a new text file
     * using an insertion order for the tree.
     */
    public static void saveDictionary(WordNode root) {
        try {
            PrintWriter writer = new PrintWriter(ORDERED_PATH);

            Queue<WordNode> nodeQueue = new LinkedBlockingDeque<>();
            nodeQueue.offer(root);

            while (!nodeQueue.isEmpty()) {
                WordNode nextNode = nodeQueue.poll();
                writer.println(nextNode.getWord() + ": " + nextNode.getDefinition());

                if (nextNode.getLeft() != null) {
                    nodeQueue.offer(nextNode.getLeft());
                }

                if (nextNode.getRight() != null) {
                    nodeQueue.offer(nextNode.getRight());
                }
            }

            writer.close();
        } catch (FileNotFoundException message) {
            System.out.println("Error occurs when writing: " + message);
        }
    }

    /**
     * Returns the height of the tree from the root node.
     *
     * @param root  the root of the binary search tree
     * @return the height
     */
    public static int height(WordNode root) {
        if (root == null) return 0;

        return 1 + Math.max(height(root.getLeft()), height(root.getRight()));
    }

    /**
     * Terminates the program.
     */
    public static void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    /**
     * Shows error message when users type the wrong number.
     */
    public static void showErrorMessage() {
        System.out.println("Invalid choice, choose again.\n");
    }
}
