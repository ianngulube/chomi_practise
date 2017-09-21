package com.ian.util;

import com.ian.chomibridge.Mail;
import com.ian.chomibridge.User;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Tester {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> obj = restTemplate.getForEntity("http://105.255.131.33:8080/chomi_bridge/person/msisdn/0762750520/", User.class);
        User user = obj.getBody();
        System.out.println(user.getFirstName() + " " + user.getLastName() + ", " + user.getMsisdn());

        //RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<User> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/msisdn/" + msisdn + "/", User.class);
        //User user = obj.getBody();
        ResponseEntity<Long> i = restTemplate.getForEntity("http://105.255.131.33:8080/chomi_bridge/all-my-messages/unread/count/" + 34 + "/", Long.class);
        int messageCount = i.getBody().intValue();
        System.out.println(messageCount);

        Mail mail = new Mail();
        mail.setContentId(0);
        mail.setDate(new Date());
        mail.setFromUserDel(0);
        mail.setFromUserId(48);
        mail.setMessage("Test");
        mail.setOriginalMailId(0);
        mail.setRead(0);
        mail.setSubject("From USSD");
        mail.setToUserDel(0);
        mail.setToUserId(42);
        mail.setTypeId(0);
        //CALL A SERVICE
        restTemplate.postForLocation("http://105.255.131.33:8080/chomi_bridge/messages/sendmessage/", mail);
    }
}
