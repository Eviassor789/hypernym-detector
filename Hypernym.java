import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Evyatar Assor - 212942486.
 */
public class Hypernym {
    private TreeMap hyponyms;
    private String name;

    /**
     * constructor.
     * @param name of the hyper
     */
    public Hypernym(String name) {
        this.hyponyms = new TreeMap<String, Integer>();
        this.name = name;
    }

    /**
     * gets the map.
     * @return gets the map
     */
    public TreeMap getHyponyms() {
        return hyponyms;
    }

    /**
     * gets the name.
     * @return gets the name.
     */
    public String getName() {
        return name;
    }

    /**
     * adds hypo to the map.
     * @param s the name.
     */
    public void addToMap(String s) {
        if (this.hyponyms.containsKey(s)) {
            this.hyponyms.put(s, (int) hyponyms.get(s) + 1);
        } else {
            this.hyponyms.put(s, 1);
        }
    }

    @Override
    public String toString() {
        List<Map.Entry<String, Integer>> hyponymList = new LinkedList<>(hyponyms.entrySet());
        hyponymList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        StringBuilder string = new StringBuilder(this.getName() + ": ");
        for (Map.Entry<String, Integer> hipo : hyponymList) {
            string.append(hipo.getKey() + " (" + hipo.getValue() + "), ");
        }
        String s = string.toString();
        return s.substring(0, s.length() - 2);
    }
}
