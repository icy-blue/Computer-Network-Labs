package exp401;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public final static double slot = 51.2;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int N = scan.nextInt(), finished = 0;
        assert N > 0;
        int time;
        ArrayList<PC> pcs = new ArrayList<>(), claimed = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            pcs.add(new PC(i));
        }
        while (finished < N) {
            pcs.sort(PC::compareTo);
            claimed.clear();
            claimed.add(pcs.get(0));
            time = pcs.get(0).waiting;
            for (int i = 1; i < N; i++) {
                if (pcs.get(i).sent) continue;
                if (pcs.get(i).waiting <= time) {
                    claimed.add(pcs.get(i));
                    pcs.get(i).sendable = true;
                }
            }
            System.out.printf("At time %.1f ms, ", time * slot);

            if (claimed.size() > 1) {
                Random random = new Random();
                for (PC i : claimed) {
                    i.sendable = false;
                    i.waiting = time + random.nextInt(9) + 1;
                }
                System.out.println("frame collided.");
            } else {
                finished++;
                System.out.println("frame from PC " + pcs.get(0).id + " sent successfully.");
                pcs.get(0).sent = true;
                for (PC i : pcs) {
                    i.waiting = Math.max(i.waiting, time + 2);
                }
            }
        }
    }
}
