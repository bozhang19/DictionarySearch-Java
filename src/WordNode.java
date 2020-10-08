/**
 * This class represents a word node.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class WordNode implements Comparable<WordNode> {
    private String word;
    private String definition;
    private WordNode left, right;

    /**
     * Creates a new WordNode object.
     */
    public WordNode() {
        // do nothing...
    }

    /**
     * Creates a new WordNode object with the given word and definition.
     *
     * @param word  the new word
     * @param definition  the definition of the new word
     */
    public WordNode(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    /**
     * Returns the word field.
     *
     * @return the word field
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the word field to the given word.
     *
     * @param word  the new word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Returns the definition field.
     *
     * @return the definition field
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Sets the definition field to the given definition.
     *
     * @param definition  the new definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Returns the left node.
     *
     * @return the left node
     */
    public WordNode getLeft() {
        return left;
    }

    /**
     * Sets the left node to the given node.
     *
     * @param left  the new left node
     */
    public void setLeft(WordNode left) {
        this.left = left;
    }

    /**
     * Returns the right node.
     *
     * @return the right node
     */
    public WordNode getRight() {
        return right;
    }

    /**
     * Sets the right node to the given node.
     *
     * @param right  the new right node
     */
    public void setRight(WordNode right) {
        this.right = right;
    }

    /**
     * Returns a integer indicating a comparision result.
     *
     * @param other  the other word node
     * @return one if this node is after other node in alphabet,
     *         negative one if before,
     *         zero if equal
     */
    @Override
    public int compareTo(WordNode other) {
        return this.getWord().compareTo(other.getWord());
    }

    /**
     * Adds a new word node to the binary search tree.
     *
     * @param newWord  the new word node
     */
    public void insert(WordNode newWord) {
        if (newWord.compareTo(this) <= 0) {
            if (this.left == null) {
                this.setLeft(newWord);
            } else {
                this.left.insert(newWord);
            }
        } else {
            if (this.right == null) {
                this.setRight(newWord);
            } else {
                this.right.insert(newWord);
            }
        }
    }

    /**
     * Checks if the binary search tree has the given search word.
     *
     * @param searchWord  the search word
     * @return true if the tree has the search word, false otherwise
     */
    public WordNode contains(String searchWord) {
        if (searchWord.equals(this.word)) {
            return this;
        } else if (searchWord.compareTo(this.getWord()) < 0) {
            if (this.getLeft() == null) {
                return null;
            } else {
                return this.getLeft().contains(searchWord);
            }
        } else {
            if (this.getRight() == null) {
                return null;
            } else {
                return this.getRight().contains(searchWord);
            }
        }
    }

    /**
     * Returns a string representation of a word node.
     *
     * @return a string
     */
    public String toString() {
        String leftWord = (left == null) ? "null" : left.word.toString();
        String rightWord = (right == null) ? "null" : right.word.toString();

        return leftWord + " <-- " + word.toString() + " --> " + rightWord;
    }
}
