import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evyatar Assor - 212942486.
 */
public class DiscoverHypernym {
    /**
     * checks if we got a good strings as the regex wants.
     * @param args the values to check
     * @return true if matches the regex
     */
    public static Boolean checkArgs(String[] args) {
        String path = "\\w((:?[^\\\\]*\\\\)*)(.*)";
        String letter = "w*";

//        if (args.length != 2) {
//            System.out.println("wrong num of args");
//            return false;
//        }

        Pattern pattern = Pattern.compile(path, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(args[0]);
        if (!matcher.find()) {
            System.out.println("not correct path");
            return false;
        }

        pattern = Pattern.compile(letter, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(args[1]);
        if (!matcher.find()) {
            System.out.println("not correct hyponym");
            return false;
        }
        return true;
    }

    /**
     * reads from the files.
     * @param directoryPath the path to read from
     * @param wordToCheck the word that we want to know about
     * @return hyper collection
     */
    public static HypernymCollection readPath(String directoryPath, String wordToCheck) {
        HypernymCollection hc = new HypernymCollection();
        int numOfFiles = 84;
        BufferedReader br = null;
        for (int i = 0; i < numOfFiles; i++) {
            String num = String.valueOf(i);
            System.out.println(i + 1 + " out of " + numOfFiles);
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(
                        directoryPath + "/" + "mbta.com_mtu.pages_" + num + ".possf2")));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("<np>" + wordToCheck + "</np>")) {
                        if (line.contains("such") || line.contains("including")
                                || line.contains("especially") || line.contains("which is")) {
                            String txt = line.toLowerCase();
                            new Patterns(txt, hc).check();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong while reading!");
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.out.println("Failed closing the file!");
                    }
                }
            }
        }
        System.out.println();
        hc.delete();
        return hc;
    }

    /**
     * runs the mission.
     * @param args the values from the client
     */
    public static void main(String[] args) {
        if (!checkArgs(args)) {
            return;
        }
        String path = args[0];
        String longLemma = args[1];
        for (int i = 2; i < args.length; i++) {
            longLemma = longLemma + " " + args[i];
        }
        String lemma = longLemma.toLowerCase(Locale.ROOT);
        HypernymCollection hc = readPath(path, lemma);
//        HypernymCollection hc = CreateHypernymDatabase.readPath(path);
        List<Map.Entry<String, Hypernym>> theList = new LinkedList<>(hc.getHypernyms().entrySet());
        List<Map.Entry<String, Hypernym>> after = new LinkedList<>();
        boolean containsLemma = false;
        for (Map.Entry<String, Hypernym> hyper: theList) {
            if (hyper.getValue().getHyponyms().containsKey(lemma)) {
                containsLemma = true;
                after.add(hyper);
            }
        }
        after.sort((o1, o2) -> (int) o2.getValue().getHyponyms().get(lemma)
                - (int) o1.getValue().getHyponyms().get(lemma));
        if (!containsLemma) {
            System.out.println("The lemma doesn't appear in the corpus.");
            return;
        }
        System.out.println(lemma);
        System.out.println();

        for (Map.Entry<String, Hypernym> hyper : after) {
            System.out.println(hyper.getKey() + ": (" + hyper.getValue().getHyponyms().get(lemma).toString() + ")");
        }
    }
}