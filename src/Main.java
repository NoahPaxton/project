import java.util.Scanner;
import java.io.*;

public class Main {

    public static int option() {
        Scanner input = new Scanner(System.in);
        return Integer.parseInt(input.next());

    }
    public static void main(String[] args) {
        System.out.format(" Package Manager %n 1) Install Package %n 2) Remove Package %n 3) exit %n");

        int selected = option();

        if (selected == 1) {
            System.out.format("temporary");

        } else if (selected == 2) {
            System.out.format("temporary");

        } else {
            System.exit(0);
        }




    }
}