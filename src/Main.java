import java.io.*;
import java.util.*;

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

    public static void main(String[] args) {
        System.out.format(" Package Manager %n 1) Install Package %n 2) Remove Package %n 3) exit %n");

        ArrayList<source> thisList = loadFile("Sources.txt");

        int selected = option();

        if (selected == 1) {
            for(int i=0;i<thisList.size();i++) {
                source thisSource = thisList.get(i);
                displaySource(thisSource);
            }


        } else if (selected == 2) {
            System.out.format("temporary");

        } else {
            System.exit(0);
        }



    }
}