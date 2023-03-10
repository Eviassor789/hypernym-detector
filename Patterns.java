import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evyatar Assor - 212942486.
 */
public class Patterns {
    private String firstP = "<np>[^<]+</np>( |,)*\\s*such\\s*as\\s*(\\s)*<np>"
            + "[^<]+</np>( |,)*\\s*((and|or|,|&|\\s)*<np>[^<]+</np>( |,)*)*";

    private String secondP = "such\\s*( |,)*<np>[^<]+</np>( |,)*\\s*as(\\s)*<np>"
            + "[^<]+</np>( |,)*\\s*((and|or|,|&|\\s)*<np>[^<]+</np>( |,)*)*";

    private String thirdP = "<np>[^<]+</np>( |,)*(\\s|,)*including(\\s)*<np>"
            + "[^<]+</np>(\\s|,)*((and|or|,|&|\\s)*<np>[^<]+</np>( |,)*)*";

    private String fourthP = "<np>[^<]+</np>(\\s|,)*especially(\\s)*<np>"
            + "[^<]+</np>(\\s|,)*((and|or|,|&|\\s)*<np>[^<]+</np>( |,)*)*";

    private String fifthP = "<np>[^<]+</np>(\\s|,)*which is(\\s)*"
            + "((an example |a kind |a class )of (a )?)*<np>[^<]+</np>";

    private String line;
    private List<String> list;
    private HypernymCollection hyperCollection;

    /**
     * constructor.
     * @param line the string
     * @param hypernymCollection the collection
     */
    public Patterns(String line, HypernymCollection hypernymCollection) {
        this.line = line;
        this.list = new ArrayList<>();
        this.hyperCollection = hypernymCollection;
    }

    /**
     * checks the patterns.
     */
    public void check() {
        pattern(this.firstP);
        pattern(this.secondP);
        pattern(this.thirdP);
        pattern(this.fourthP);
        pattern5();
    }

    /**
     * checks the pattern with the regex.
     * @param regex a string format with rules
     */
    public void pattern(String regex) {
        int hypernymCounter = 0;
        int wordEnd = 0;
        String hyperName = null;
        //String copyCurr = null;
        String copyLine = this.line;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(copyLine);
        while (matcher.find()) {
            String curr = copyLine.substring(matcher.start(), matcher.end());
            wordEnd = curr.indexOf("</np>");
            String word = curr.substring(curr.indexOf("<np>") + 4, wordEnd);
            while (word != null) {
                if (hypernymCounter == 0) {
                    hyperName = word;
                    this.hyperCollection.addHypernym(word);
                    hypernymCounter++;
                } else {
                    hyperCollection.getHypernym(hyperName).addToMap(word);
                }
                //System.out.println(matcher.end() + " - " + copyLine.length() + " - " + matcher.start());
                //copyCurr = curr;
                curr = curr.substring(curr.indexOf(">", curr.indexOf("</np>")) + 1);
                if (!curr.contains("</np>")) {
                    word = null;
                } else {
                    word = curr.substring(curr.indexOf("<np>") + 4, curr.indexOf("</np>"));
                }
            }
            hypernymCounter = 0;
        }
    }

    /**
     * check the specific type and sends different based on the regex.
     */
    public void pattern5() {
        int hypernymCounter = 0;
        int wordEnd = 0;
        String hypoName = null;
        String copyLine = this.line;
        Pattern pattern = Pattern.compile(this.fifthP, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(copyLine);
        while (matcher.find()) {
            String curr = copyLine.substring(matcher.start(), matcher.end());
            wordEnd = curr.indexOf("</np>");
            String word = curr.substring(curr.indexOf("<np>") + 4, wordEnd);
            while (word != null) {
                if (hypernymCounter == 0) {
                    hypoName = word;
                    hypernymCounter++;
                } else {
                    this.hyperCollection.addHypernym(word);
                    hyperCollection.getHypernym(word).addToMap(hypoName);
                }
                curr = curr.substring(curr.indexOf(">", curr.indexOf("</np>")) + 1);
                if (!curr.contains("</np>")) {
                    word = null;
                } else {
                    word = curr.substring(curr.indexOf("<np>") + 4, curr.indexOf("</np>"));
                }
            }
            hypernymCounter = 0;
        }
    }
}