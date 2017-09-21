
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.springframework.util.DefaultPropertiesPersister;

public class FileWriter {

    public static void main(String args[]) {
        saveParamChanges();
    }

    public static void saveParamChanges() {
        String msisdn="27762750520";
        try {
            // create and set properties into properties object
            Properties props = new Properties();
            props.setProperty(msisdn+".friend.option1", "Seati");
            props.setProperty(msisdn+".friend.option2", "Kenneth");
            props.setProperty(msisdn+".friend.option3", "Kolisa");
            // get or create the file
            File f = new File("27762750520.properties");
            OutputStream out = new FileOutputStream(f);
            // write into it
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(props, out, "Header COmment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
