package com.ian.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ian.chomibridge.User;
import com.ian.service.AdvertService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.biza.adverts.marbl.Advert;

@Service
public class AdvertServiceImpl implements AdvertService {

    @Override
    public Advert getAdvert(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Advert ad = null;
        try {
            ad = mapper.readValue(json, Advert.class);
        } catch (IOException ex) {
            Logger.getLogger(AdvertServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (ad);
    }

    @Override
    public void getSendFeedback(Advert advert, User user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("clientref", user.getMsisdn());
        headers.set("x-up-calling-line-id", user.getMsisdn());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<?> request = new HttpEntity<>(headers);
        String result = restTemplate.postForObject(advert.getPostbackUrl(), request, String.class);
        System.out.println(result);
    }

    @Override
    public Advert doCompleteAction(String url, User user) {
        Advert ad = getAdvert(url);
        getSendFeedback(ad, user);
        return ad;
    }

}
