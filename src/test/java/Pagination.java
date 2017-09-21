
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pagination {

    public static void main(String args[]) {
        /* List<Integer> numbers = new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
        );
        int size = numbers.size();
        List<Integer> head = numbers.subList(0, 4);
        List<Integer> tail = numbers.subList(4, 8);
        System.out.println(head); // prints "[5, 3, 1, 2]"
        System.out.println(tail); // prints "[9, 5, 0, 7]"
        Collections.sort(head);
        System.out.println(numbers); // prints "[1, 2, 3, 5, 9, 5, 0, 7]"
        tail.add(-1);
        System.out.println(numbers); // prints "[1, 2, 3, 5, 9, 5, 0, 7, -1]"*/

        myAlgorithm();
    }

    public static void test() {
        List<Integer> numbers = new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
        );
        int sizePerPage = 5;
        int page = Math.round(numbers.size() / sizePerPage);
        int from = Math.max(0, page * sizePerPage);
        int to = Math.min(numbers.size(), (page + 1) * sizePerPage);
        numbers.subList(from, to);
        System.out.println(numbers.subList(from, to));
        System.out.println("To: " + to);
        System.out.println("From: " + from);
    }

    public static void myAlgorithm() {
        List<Integer> numbers = new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        );
        int pageSize = 5;
        int listSize = numbers.size();
        for (int index = 0; index < listSize; index += (pageSize)) {
            System.out.println("First: " + index + ", Last: " + (((index + pageSize) < listSize) ? (index + pageSize - 1) : 0));
        }
    }

}
