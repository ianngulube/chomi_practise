
import java.util.*;

public class Countdown {

    public static void main(String[] args) throws InterruptedException {
        int time = 100;
        Queue<Integer> queue = new LinkedList<Integer>();

        for (int i = time; i >= 0; i--) {
            queue.add(i);
        }

        while (!queue.isEmpty()) {
            System.out.println(queue.remove());
            Thread.sleep(1000);
        }
    }
}
