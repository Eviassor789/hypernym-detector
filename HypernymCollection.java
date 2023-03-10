import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Evyatar Assor - 212942486.
 */
public class HypernymCollection {
    private TreeMap hypernyms;

    /**
     * constructor.
     */
    public HypernymCollection() {
        this.hypernyms = new TreeMap<String, Hypernym>();
    }

    /**
     * adds hyper to the map.
     * @param s the name of the hyper
     */
    public void addHypernym(String s) {
        if (!this.hypernyms.containsKey(s)) {
            this.hypernyms.put(s, new Hypernym(s));
        }
    }

    /**
     * gets the map.
     * @return the map
     */
    public TreeMap getHypernyms() {
        return hypernyms;
    }

    /**
     * returns the hyper itself.
     * @param s the name
     * @return the hyper itself
     */
    public Hypernym getHypernym(String s) {
        return (Hypernym) hypernyms.get(s);
    }

    @Override
    public String toString() {
        List<Map.Entry<String, Hypernym>> hyperList = new LinkedList<>(hypernyms.entrySet());
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, Hypernym> hyper : hyperList) {
            string.append(hyper.getValue().toString() + '\n');
        }
        return string.toString();
    }

    /**
     * deletes if there is less than 3 hypo for the hyper.
     */
    public void delete() {
        List<Map.Entry<String, Hypernym>> oldList = new LinkedList<>(hypernyms.entrySet());
        List<Map.Entry<String, Hypernym>> newList = new LinkedList<>();

        for (Map.Entry<String, Hypernym> hyper : oldList) {
            if (hyper.getValue().getHyponyms().size() >= 3) {
                newList.add(hyper);
            }
        }
        this.hypernyms = new TreeMap<String, Hypernym>();

        for (Map.Entry<String, Hypernym> stringHypernymEntry : newList) {
            this.hypernyms.put(stringHypernymEntry.getKey(), stringHypernymEntry.getValue());
        }
    }
}
