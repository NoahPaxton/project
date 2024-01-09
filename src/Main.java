// Add comments
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

    public static int option(PrintWriter out, String[] Errors) {
        // This will ask the user for an input and validate it before returing it
        boolean i = true;
        Scanner input = new Scanner(System.in);
        int valueint=0;
        while (i==true) {
            try {
                String value = input.next();
                valueint = Integer.parseInt(value);
                i=false;
            } catch (NumberFormatException e) {
                System.out.println(Errors[5] + e.toString());
                out.println("Incorrect Input Error "+ e.toString());
            }
        }
        return valueint;
    }

    public static void Download(String url, String fileName, String UserName, PrintWriter out, String[] Errors) {
        // This will download the file from the url passed through and name the file after the filename passed through.
        // Files must be an exe since it appeneds .exe to the end of the filename so it is executable.
        boolean i = true;
        while (i==true) {
            try {
                //URL is deprecated and URI should be used now however this is complex, so I will just leave it as URL for now.
                InputStream in = new URL(url).openStream();
                // add support for files other than .exe?
                Files.copy(in, Paths.get("/users/"+UserName+"/Documents/Packages/" + fileName + ".exe"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Download of " +fileName+" completed successfully");
                out.println("Download of " +fileName+" completed successfully");
                i = false;
            } catch (IOException e) {
                System.out.println(Errors[4] + e.toString());
                out.println("Error while downloading file "+ e.toString());
            }
        }


    }

    public static ArrayList<source> loadFile(String filename, PrintWriter out, String[] Errors) {
        List<String> SourceDetails;
        ArrayList<source> thisList = new ArrayList<>();
        boolean i = true;
        while (i==true) {
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
                i = false;
            } catch (IOException e) {
                System.out.println(Errors[3] + e.toString());
                out.println("Error while loading sources file. Please Make sure it exists. " + e.toString());
            }
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


    public static int countItemsInFolder(String UserName){
        return Objects.requireNonNull(new File("/users/" + UserName + "/Documents/Packages/").list()).length;
    }

    public static void DeleteFiles(String UserName, PrintWriter out, String[] Errors){
        boolean i = true;
        while (i==true) {
            try {
                FileUtils.cleanDirectory(new File("/users/" + UserName + "/Documents/Packages"));
                i = false;
            } catch (IOException e) {
                System.out.println( Errors[2] + e.toString());
                out.println("Error deleting folder. It might not exist. "+ e.toString());
            }
        }

    }


    public static List<String> FileNames(String UserName) {
            return Stream.of(Objects.requireNonNull(new File("/users/" + UserName + "/Documents/Packages/").listFiles()))
                    .map(File::getName)
                    .collect(Collectors.toList());
    }




    public static void main(String[] args) throws IOException {
        String[] Errors = {"File Wasn't Found. or there was an error parsing the timesRan file. make sure timesRan only contains an integer or delete the timesRan file(s) ",
                "There was an error finding the selected file ","Folder could not be deleted. it may not exist ", "Error occurred reading file. Please make sure the file structure is correct.",
                "There was an error downloading the file, please try again. ", "input was incorrect, please try again.  "};

        PrintWriter out = null;
        boolean pwloop = true;
        while (pwloop==true) {
            try {
                File runcount = new File("timesRan.txt");
                if (runcount.exists()) {
                    BufferedReader in = new BufferedReader(new FileReader(runcount));
                    int numberfromtimesRan = Integer.parseInt(in.readLine());
                    out = new PrintWriter("logs"+numberfromtimesRan+".txt");
                    BufferedWriter writetotimesran = new BufferedWriter(new FileWriter("timesRan.txt"));
                    writetotimesran.write(numberfromtimesRan + 1);
                } else {
                    out = new PrintWriter("logs.txt");
                }
                pwloop = false;
            } catch (IOException e) {
                System.out.println(Errors[0]);
            }
        }
        String UserName = System.getProperty("user.name");
        ArrayList<source> thisList = loadFile("Sources.txt", out, Errors);
        boolean executeprogram = true;
        while (executeprogram==true) {
            System.out.format(" Package Manager %n 0) Install Package %n 1) Remove Package and/or files %n 2) exit %n");
            int selected = option(out, Errors);
            if (selected == 0) {
                for(int i=0;i<thisList.size();i++) {
                    source thisSource = thisList.get(i);
                    displaySource(thisSource);
                }

                int urlselected = option(out, Errors);
                source thisURL = thisList.get(urlselected);
                String url = selectURL(thisURL);
                source thisFilename = thisList.get(urlselected);
                String fileName = programFileName(thisFilename);
                Download(url, fileName, UserName, out, Errors);
                System.out.println("Download completed successfully");
                Runtime.getRuntime().exec("cls");
                out.close();
            } else if (selected == 1) {

                for(int i=0;i<=countItemsInFolder(UserName)-1;) {
                    System.out.format("%d) %s %n", (i + 1), FileNames(UserName).get(i));
                    i++;
                }
                System.out.format("%d) Delete everything in Packages folder %n", (countItemsInFolder(UserName) + 1));

                int deleteSelected = option(out, Errors);

                if (deleteSelected == (countItemsInFolder(UserName) + 1)) {
                    DeleteFiles(UserName, out, Errors);
                    System.out.println("All files deleted");
                    out.println("All files deleted successfully");
                    out.close();
                    Runtime.getRuntime().exec("cls");
                } else {
                    String FileNameToDelete = FileNames(UserName).get(deleteSelected-1);

                        try {
                            FileUtils.delete(new File("/users/"+UserName+"/Documents/Packages/" + FileNameToDelete));
                            System.out.println(FileNameToDelete + " Deleted successfully");
                        } catch (IOException e) {
                            System.out.println(Errors[1] + e.toString());
                            out.println("File wasn't found. " + e.toString());
                        }
                    }


            } else {
                out.close();
                System.exit(0);
            }
        }


    }
}