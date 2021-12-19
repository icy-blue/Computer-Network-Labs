package Exp301;

import java.util.Scanner;

public class Alter {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String data = scan.nextLine();
        String polynomial = scan.nextLine();
        StringBuilder sb = new StringBuilder(data);
        sb.setCharAt(0, (char) (sb.charAt(0) ^ 1));
        System.out.println(sb);
        System.out.println(polynomial);
    }
}
