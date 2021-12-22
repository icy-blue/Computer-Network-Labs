package exp501;

public class Main {
    public static void main(String[] args) {
        new Flooding().run();
    }
}

/**
 * test cases
 * 6 14 1 //(k = -1 for no limit)
 * 0 1 1
 * 0 2 2
 * 1 3 1
 * 3 1 1
 * 1 4 1
 * 4 1 1
 * 2 3 2
 * 3 2 2
 * 2 4 2
 * 4 2 2
 * 3 4 2
 * 4 3 2
 * 4 5 1
 * 3 5 2
 */
