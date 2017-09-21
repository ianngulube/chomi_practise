
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.web.client.RestTemplate;

public class Advert {

    String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Chat&responseType=ussd&xmlwrapped=false";

    public static void main(String[] args) throws IOException {
        new Advert().tester();
    }

    public void tester() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String o = restTemplate.getForObject(url, String.class);
        //System.out.println(o);

        ObjectMapper mapper = new ObjectMapper();
        za.co.biza.adverts.marbl.Advert ad = mapper.readValue(o, za.co.biza.adverts.marbl.Advert.class);
        System.out.println(ad);
    }
}
