/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.model.UssdMenu;
import java.util.List;

public interface UssdMenuService {

    List<UssdMenu> getLanguage(String language);

    List<UssdMenu> getAgeVerification(String language);

    List<UssdMenu> getSubscribe(String language);

    List<UssdMenu> getSubscribeConfirm(String language);

    List<UssdMenu> getSuccessful(String language);

    List<UssdMenu> getTermsAndConditions(String language);

    List<UssdMenu> getAgePrompt(String language);

    List<UssdMenu> getGenderPrompt(String language);

    List<UssdMenu> getProvincePrompt(String language);

    List<UssdMenu> getMainWall(String language);

    List<UssdMenu> getMainWallRegistered(String... language);

    List<UssdMenu> getSearchBy(String language);

    List<UssdMenu> getNicknameSearch(String language);

    List<UssdMenu> getFullnameSearch(String language);

    List<UssdMenu> getCellphoneSearch(String language);

    List<UssdMenu> getSetDOB(String language);

    List<UssdMenu> getProvinceSearch(String language);

    List<UssdMenu> getViewWall(String language);

    List<UssdMenu> getPostToTheWall(String language);

    List<UssdMenu> getPostToTheWallSuccess(String language);

    List<UssdMenu> getPostOrViewComment(String language);

    List<UssdMenu> getPostComment(String language);

    List<UssdMenu> getPostResults(String language);

    List<UssdMenu> getFriendRequestSuccess(String language);

    List<UssdMenu> getErrorHandler(String language);

    List<UssdMenu> getTestMenu(String language);

    List<UssdMenu> getChomiSendMessage(String language);

    List<UssdMenu> getChomiSendMessageSuccess(String language);

    List<UssdMenu> getMessageReplySuccess(String language);

    List<UssdMenu> getHashTag(String language);

    List<UssdMenu> getTrendingCreate(String language);

    List<UssdMenu> getTrendingCreateSuccess(String language);

    List<UssdMenu> getTrendingSearch(String language);

    List<UssdMenu> getMessageDelete(String language);

    List<UssdMenu> getFriendRequestDecline(String language);

    List<UssdMenu> getFriendRequestFailure(String language);

    List<UssdMenu> getNoUnreadMessages(String language);

    List<UssdMenu> getNoFriendRequests(String language);

    List<UssdMenu> getWelcomeNote(String language);

    List<UssdMenu> getGoodByeNote(String language);

    List<UssdMenu> getMsisdNotSubscribed(String language);

    public List<UssdMenu> getNewChomiNotification(String language);
    
    public List<UssdMenu> getFriendBlock(String language);

    public List<UssdMenu> getPageCreate(String language);

    public List<UssdMenu> getPageCreateDescription(String language);

    public List<UssdMenu> getPageCreateType(String language);

    public List<UssdMenu> getPageCreateSuccess(String language);

    public List<UssdMenu> getSendGroupMessage(String language);

    UssdMenu buildMenuItem(String code,
            String command,
            String order,
            String url,
            String page,
            String pageHeader,
            boolean display,
            boolean isShowOption);

    //public List<UssdMenu> getFriendBlock(String language);

}
