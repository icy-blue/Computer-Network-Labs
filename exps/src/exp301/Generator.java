package exp301;

import java.util.Scanner;

public class Generator {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String data = scan.nextLine();
        String polynomial = scan.nextLine();
        CRC crc = new CRC(polynomial);
        System.out.println(crc.generate(data));
        System.out.println(polynomial);
    }
}
