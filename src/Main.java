import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static int option() {
        int i = 0;
        Scanner input = new Scanner(System.in);
        int valueint=0;
        while (i==0) {
            try {
                String value = input.next();
                valueint = Integer.parseInt(value);
                i++;
            } catch (NumberFormatException e) {
                System.out.println("input was incorrect, please try again.");

            }
        }
        return valueint;
    }

    public static void Download(String url, String fileName) {
        try {
            //URL is deprecated and URI should be used now however this is complex, so I will just leave it as URL for now.
            InputStream in = new URL(url).openStream();
            String username = System.getProperty("user.name");
            // add support for files other than .exe?
            Files.copy(in, Paths.get("/users/"+username+"/Documents/Packages/" + fileName + ".exe"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static ArrayList<source> loadFile(String filename) {
        List<String> SourceDetails;
        ArrayList<source> thisList = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = in.readLine();
            while (line != null) {
                SourceDetails = Arrays.asList(line.split(","));
                source newSource = new source();
                newSource.program = SourceDetails.get(0);
                newSource.URL = SourceDetails.get(1);
                thisList.add(newSource);
                line = in.readLine();

            }
        } catch (IOException i) {
            System.out.println("Error occurred reading file. Please make sure the file structure is correct. " + i.toString());
        }
        return thisList;
    }

    public static void displaySource(source thisSource){
        System.out.format("%s %n",thisSource.program);
    }


    public static String selectURL(source thisURL){
        return thisURL.URL;
    }

    public static String programFileName(source thisFilename){
        //merge this into selectURL?
        return thisFilename.program;
    }


    public static int countItemsInFolder(){
        String username = System.getProperty("user.name");
        return Objects.requireNonNull(new File("/users/" + username + "/Documents/Packages/").list()).length;
    }

    public static void DeleteFiles(){
        String username = System.getProperty("user.name");

        try {
            FileUtils.cleanDirectory(new File("/users/" + username + "/Documents/Packages"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> FileNames() {
        String username = System.getProperty("user.name");
            return Stream.of(Objects.requireNonNull(new File("/users/" + username + "/Documents/Packages/").listFiles()))
                    .map(File::getName)
                    .collect(Collectors.toList());
    }




    public static void main(String[] args) {
        System.out.format(" Package Manager %n 0) Install Package %n 1) Remove Package and/or files %n 2) exit %n");

        ArrayList<source> thisList = loadFile("Sources.txt");

        int selected = option();

        if (selected == 0) {
            for(int i=0;i<thisList.size();i++) {
                source thisSource = thisList.get(i);
                displaySource(thisSource);
            }

            int urlselected = option();
            source thisURL = thisList.get(urlselected);
            String url = selectURL(thisURL);
            source thisFilename = thisList.get(urlselected);
            String fileName = programFileName(thisFilename);
            Download(url, fileName);

        } else if (selected == 1) {

            for(int i=0;i<countItemsInFolder();i++) {
                System.out.format("%n", FileNames().get(i));
                //out of bounds

            }


            System.out.format("%d) Delete everything in Packages folder %n", (countItemsInFolder() + 1));
            int deleteSelected = option();
            if (deleteSelected == (countItemsInFolder() + 1)) {
               DeleteFiles();
            }


        } else {
            System.exit(0);
        }



    }
}