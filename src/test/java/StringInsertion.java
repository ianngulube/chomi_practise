
import java.util.Date;


public class StringInsertion {
    
    public static void main(String[] args) {
        int j = 20160201;
        String x = Integer.toString(j);
        x = x.substring(0, 4) + "/" + x.substring(4, x.length());
        x = x.substring(0, 7) + "/" + x.substring(7, x.length());
        System.out.println(new Date(x));
        System.out.println(x);
    }
}
