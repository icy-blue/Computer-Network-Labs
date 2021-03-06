package exp503;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Item> items = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt(), m = scan.nextInt();
        scan.nextLine();
        boolean hasDefault = false;
        for (int i = 0; i < n; i++) {
            String[] strs = scan.nextLine().split(" ");
            assert strs.length == 3;
            if (strs[0].strip().equals("0.0.0.0")) hasDefault = true;
            items.add(new Item(strs[0], strs[1], Integer.parseInt(strs[2])));
        }
        if (!hasDefault) items.add(new Item("0.0.0.0", "0.0.0.0", -1));
        items.sort(Item::compareTo);
        for (int i = 0; i < m; i++) {
            String x = scan.nextLine().strip();
            for (Item item : items) {
                if (item.match(x)) {
                    System.out.println(item.outline);
                    break;
                }
            }
        }
    }
}

/**
 * test cases
 * 4 4
 * 10.0.0.0 255.0.0.0 1
 * 10.12.123.128 255.255.255.128 2
 * 10.12.123.0 255.255.255.0 3
 * 1.2.3.4 255.255.255.255 4
 * 1.3.2.4
 * 10.12.123.129
 * 10.12.123.127
 * 10.1.2.3
 */