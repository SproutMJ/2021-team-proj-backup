package launcher;

import service.Deflate;

import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Deflate deflate = new Deflate();
            deflate.deflate("", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
