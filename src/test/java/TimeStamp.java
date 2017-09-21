
import java.sql.Timestamp;

public class TimeStamp {

    public static void main(String args[]) {
        java.util.Date date = new java.util.Date();
        //remove .
        String str1 = new Timestamp(date.getTime()).toString();
        String str2 = str1.replace(".", "");
        //replace :
        String str3 = str2.replace(":", "");
        //replace -
        String str4 = str3.replace("-", "");
        //replace " "
        String str5 = str4.replace(" ", "");
        System.out.println(str5);
    }
}
