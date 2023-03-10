import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evyatar Assor - 212942486.
 */
public class CreateHypernymDatabase {
    /**
     * checks if we got a good strings as the regex wants.
     * @param args the values to check
     * @return true if matches the regex
     */
    public static Boolean checkArgs(String[] args) {
        String pathRegex = "\\w((:?[^\\\\]*\\\\)*)(.*)";

        if (args.length != 2) {
            System.out.println("wrong num of arguments");
            return false;
        }

        for (int i = 0; i < 2; i++) {
            Pattern pattern = Pattern.compile(pathRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(args[i]);
            if (!matcher.find()) {
                System.out.println("not a path");
                return false;
            }
        }
        return true;
    }

    /**
     * reads from the path and creates database.
     * @param directoryPath the files path
     * @return the database
     */
    public static HypernymCollection readPath(String directoryPath) {
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
                    if (line.contains("such") || line.contains("including")
                            || line.contains("especially") || line.contains("which is")) {
                        String txt = line.toLowerCase();
                        new Patterns(txt, hc).check();
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
     * @param args the values from the user.
     */
    public static void main(String[] args) {
        if (!checkArgs(args)) {
            return;
        }
        String path = args[0];
        HypernymCollection hc = readPath(path);
        try {
            File myObj = new File(args[1] + "\\hypernym_db.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException exception) {
            System.out.println("IO error");
            exception.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(args[1] + "\\hypernym_db.txt");
            myWriter.write(hc.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }
    }
}