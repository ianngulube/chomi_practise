package com.ian.service;

import com.ian.chomibridge.User;
import za.co.biza.adverts.marbl.Advert;

public interface AdvertService {

    public Advert getAdvert(String url);

    public void getSendFeedback(Advert advert, User user);

    public Advert doCompleteAction(String url, User user);
}
