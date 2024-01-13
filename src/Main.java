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

    public static int option(PrintWriter out, String[] Errors, String[] LogErrors) {
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
                out.println( LogErrors[1]+ e.toString());
            }
        }
        return valueint;
    }

    public static void Download(String url, String fileName, String UserName, PrintWriter out, String[] Errors, String[] LogErrors) throws RuntimeException {
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
                out.println(LogErrors[3] + e.toString());
            }
        }


    }

    public static ArrayList<source> loadFile(String filename, PrintWriter out, String[] Errors, String[] LogErrors) {
        // This loads the sources file into an arraylist.
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
                out.println( LogErrors[4] + e.toString());
            }
        }

        return thisList;
    }

    public static String selectURL(source thisURL){
        // this returns the url to download from
        return thisURL.URL;
    }

    public static String programFileName(source thisFilename){
        //this returns the program filename
        return thisFilename.program;
    }


    public static int countItemsInFolder(String UserName){
        // This returns how many files are in the packages folder
        return Objects.requireNonNull(new File("/users/" + UserName + "/Documents/Packages/").list()).length;
    }

    public static void DeleteFiles(String UserName, PrintWriter out, String[] Errors, String[] LogErrors){
        // This deletes all files and folders in the Packages folder
        boolean i = true;
        while (i==true) {
            try {
                FileUtils.cleanDirectory(new File("/users/" + UserName + "/Documents/Packages"));
                i = false;
            } catch (IOException e) {
                System.out.println( Errors[2] + e.toString());
                out.println( LogErrors[0] + e.toString());
            }
        }

    }


    public static List<String> FileNames(String UserName) {
        // This gets the filenames and folder names of things inside the packages folder
            return Stream.of(Objects.requireNonNull(new File("/users/" + UserName + "/Documents/Packages/").listFiles()))
                    .map(File::getName)
                    .collect(Collectors.toList());
    }


    public static void CreatePackagesDirectory (String UserName) {
        // This creates the Packages directory in the users documents if it doesnt exist.
        File directory = new File("/users/" + UserName + "/Documents/Packages/");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static void CreateDefaultSources (String[] Errors, PrintWriter out, String[] LogErrors) {
        // This creates the default sources file if it doesnt exist.
        File sources = new File("Sources.txt");
        if (!sources.exists()) {
            try {
                sources.createNewFile();
                BufferedWriter defaultSources = new BufferedWriter(new FileWriter("Sources.txt"));
                defaultSources.write("Rufus,https://github.com/pbatard/rufus/releases/download/v4.3/rufus-4.3.exe\n" +
                        "Discord,https://github.com/portapps/discord-portable/releases/download/1.0.9028-17/discord-portable-win32.exe\n" +
                        "7zipconsole,https://www.7-zip.org/a/7zr.exe\n" +
                        "alacritty,https://github.com/alacritty/alacritty/releases/download/v0.13.1/Alacritty-v0.13.1-portable.exe\n" +
                        "steamrommanager,https://github.com/SteamGridDB/steam-rom-manager/releases/download/v2.4.17/Steam-ROM-Manager-portable-2.4.17.exe");
                defaultSources.close();
            } catch (IOException e) {
                System.out.println( Errors[2] + e.toString());
                out.println( LogErrors[0] + e.toString());
            }

        }

    }



    public static void main(String[] args) {

        // These arrays store error messages for errors printed to screen and errors written to the logs.txt file(s)
        String[] Errors = {"File Wasn't Found. or there was an error parsing the timesRan file. make sure timesRan only contains an integer or delete the timesRan file(s) ",
                "There was an error finding the selected file ","Folder could not be deleted. it may not exist ", "Error occurred reading file. Please make sure the file structure is correct.",
                "There was an error downloading the file, please try again. ", "input was incorrect, please try again.  "};

        String[] LogErrors = {"Error deleting folder. It might not exist. ","Incorrect Input Error ","File wasn't found. ","Error while downloading file ", "Error while loading sources file. Please Make sure it exists. "};

        // String that gets the username of the user
        String UserName = System.getProperty("user.name");

        PrintWriter out = null;
        boolean pwloop = true;
        while (pwloop==true) {
            try {
                // exists so that the logs.txt file isn't overwritten on every run and a new one is created
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
        CreatePackagesDirectory(UserName);
        CreateDefaultSources(Errors, out, LogErrors);

        ArrayList<source> thisList = loadFile("Sources.txt", out, Errors, LogErrors);
        boolean executeprogram = true;
        while (executeprogram==true) {
            System.out.format(" Package Manager %n 0) Install Package %n 1) Remove Package and/or files %n 2) exit %n");
            int selected = option(out, Errors, LogErrors);
            if (selected == 0) {
                // section that handles installing packages
                for(int i=0;i<thisList.size();i++) {
                    source thisSource = thisList.get(i);
                    System.out.format("%d) %s %n", i, programFileName(thisSource));
                }

                int urlselected = option(out, Errors, LogErrors);
                source thisURL = thisList.get(urlselected);
                String url = selectURL(thisURL);
                source thisFilename = thisList.get(urlselected);
                String fileName = programFileName(thisFilename);
                Download(url, fileName, UserName, out, Errors, LogErrors);
                System.out.println("Download completed successfully");
                System.out.println("\n \n \n ");
                out.close();
            } else if (selected == 1) {
                // section that handles removing packages
                for(int i=0;i<=countItemsInFolder(UserName)-1;) {
                    System.out.format("%d) %s %n", (i + 1), FileNames(UserName).get(i));
                    i++;
                }
                System.out.format("%d) Delete everything in Packages folder %n", (countItemsInFolder(UserName) + 1));

                int deleteSelected = option(out, Errors, LogErrors);

                if (deleteSelected == (countItemsInFolder(UserName) + 1)) {
                    // deletes entire contents of packages folder
                    DeleteFiles(UserName, out, Errors, LogErrors);
                    System.out.println("All files deleted");
                    out.println("All files deleted successfully");
                    System.out.println("\n \n \n ");
                    out.close();
                } else {
                    String FileNameToDelete = FileNames(UserName).get(deleteSelected-1);
                        // deletes file selected
                        try {
                            FileUtils.delete(new File("/users/"+UserName+"/Documents/Packages/" + FileNameToDelete));
                            System.out.println(FileNameToDelete + " Deleted successfully");
                        } catch (IOException e) {
                            System.out.println(Errors[1] + e.toString());
                            out.println(LogErrors[2] + e.toString());
                        }
                    }


            } else {
                // section that handles quitting the program
                out.close();
                System.exit(0);
            }
        }


    }
}