/**
 * Evyatar Assor - 212942486.
 */
public class Hyponym {
    private int numOfReferences;
    private String name;

    /**
     * the constructor.
     * @param name the name of the hypernym
     */
    public Hyponym(String name) {
        this.numOfReferences = 0;
        this.name = name;
    }

    /**
     * increases the value.
     */
    public void increase() {
        this.numOfReferences += 1;
    }

    /**
     * gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the num of references.
     * @return the num of references
     */
    public int getNumOfReferences() {
        return numOfReferences;
    }
}
