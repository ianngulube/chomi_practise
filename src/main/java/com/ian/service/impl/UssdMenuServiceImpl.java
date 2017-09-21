/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.model.UssdMenu;
import com.ian.service.UssdMenuService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class UssdMenuServiceImpl implements UssdMenuService {

    private final String folder = "chomiussd";
    private final String url = "http://105.255.131.33:8080/" + folder;
    private final boolean SHOW_OPTIONS = true;

    @Override
    public List<UssdMenu> getLanguage(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".language.headerText";
        //English Option
        page = "ageverification";
        {
            command = "1";
            order = "1";
            code = language + ".language.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "2";
            order = "2";
            code = language + ".language.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "3";
            order = "3";
            code = language + ".language.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "4";
            order = "4";
            code = language + ".language.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "5";
            order = "5";
            code = language + ".language.option5";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "6";
            order = "6";
            code = language + ".language.option6";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "7";
            order = "7";
            code = language + ".language.option7";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "8";
            order = "8";
            code = language + ".language.option8";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "9";
            order = "9";
            code = language + ".language.option9";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "10";
            order = "10";
            code = language + ".language.option10";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "11";
            order = "11";
            code = language + ".language.option11";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "12";
            order = "12";
            code = language + ".language.option12";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "ageverification";
            command = "0";
            order = "13";
            code = language + ".language.option13";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getAgeVerification(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".ageverification.headerText";
        //English Option
        page = "subscribe";
        {
            command = "1";
            order = "1";
            code = language + ".ageverification.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "subscribe";
            command = "0";
            order = "2";
            code = language + ".ageverification.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSubscribe(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".subscribe.headerText";
        //English Option
        page = "subscribeconfirm";
        {
            command = "1";
            order = "1";
            code = language + ".subscribe.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "termsandconditions";
            command = "2";
            order = "2";
            code = language + ".subscribe.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "ageverification";
            command = "0";
            order = "3";
            code = language + ".subscribe.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSubscribeConfirm(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".subscribeconfirm.headerText";
        //English Option
        page = "successful";
        {
            command = "1";
            order = "1";
            code = language + ".subscribeconfirm.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "successful";
            command = "0";
            order = "2";
            code = language + ".subscribeconfirm.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSuccessful(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".successful.headerText";
        //English Option
        page = "ageprompt";
        {
            command = "0";
            order = "1";
            code = language + ".successful.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getTermsAndConditions(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".termsandconditions.headerText";
        //English Option
        page = "subscribe";
        {
            command = "0";
            order = "1";
            code = language + ".termsandconditions.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getAgePrompt(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".ageprompt.headerText";
        //English Option
        page = "gender";
        {
            command = "0";
            order = "1";
            code = language + ".ageprompt.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getGenderPrompt(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".gender.headerText";
        //English Option
        page = "province";
        {
            command = "1";
            order = "1";
            code = language + ".gender.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "province";
            command = "2";
            order = "2";
            code = language + ".gender.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        /*{
            //Afrikaans Option
            page = "province";
            command = "3";
            order = "3";
            code = language + ".gender.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));*/
        {
            //Afrikaans Option
            page = "province";
            command = "0";
            order = "3";
            code = language + ".gender.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getProvincePrompt(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".province.headerText";
        //English Option
        page = "mainwall";
        {
            command = "1";
            order = "1";
            code = language + ".province.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "2";
            order = "2";
            code = language + ".province.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "3";
            order = "3";
            code = language + ".province.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "4";
            order = "4";
            code = language + ".province.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "mainwall";
        {
            command = "5";
            order = "5";
            code = language + ".province.option5";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "6";
            order = "6";
            code = language + ".province.option6";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "7";
            order = "7";
            code = language + ".province.option7";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "8";
            order = "8";
            code = language + ".province.option8";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "9";
            order = "9";
            code = language + ".province.option9";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "0";
            order = "10";
            code = language + ".province.option10";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getMainWall(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".mainwall.headerText";
        //English Option
        page = "language";
        {
            command = "1";
            order = "1";
            code = language + ".mainwall.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "2";
            order = "2";
            code = language + ".mainwall.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "3";
            order = "3";
            code = language + ".mainwall.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "4";
            order = "4";
            code = language + ".mainwall.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "language";
        {
            command = "5";
            order = "5";
            code = language + ".mainwall.option5";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "6";
            order = "6";
            code = language + ".mainwall.option6";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "7";
            order = "7";
            code = language + ".mainwall.option7";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "8";
            order = "8";
            code = language + ".mainwall.option8";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "language";
            command = "0";
            order = "9";
            code = language + ".mainwall.option9";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getMainWallRegistered(String... language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language[0] + ".mainwallregistered.headerText";
        if (language.length == 2) {
            pageHeader = language[0] + ".mainwall.headerText";;
        }
        //English Option
        page = "mainwallregistered_decision";
        {
            command = "1";
            order = "1";
            code = language[0] + ".mainwallregistered.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "2";
            order = "2";
            code = language[0] + ".mainwallregistered.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "3";
            order = "3";
            code = language[0] + ".mainwallregistered.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "4";
            order = "4";
            code = language[0] + ".mainwallregistered.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "mainwallregistered_decision";
        {
            command = "5";
            order = "5";
            code = language[0] + ".mainwallregistered.option5";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "6";
            order = "6";
            code = language[0] + ".mainwallregistered.option6";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "7";
            order = "7";
            code = language[0] + ".mainwallregistered.option7";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        //Groups Addition
        {
            page = "mainwallregistered_decision";
            command = "8";
            order = "8";
            code = language[0] + ".mainwallregistered.option8";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            page = "mainwallregistered_decision";
            command = "0";
            order = "9";
            code = language[0] + ".mainwallregistered.option9";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSearchBy(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".searchby.headerText";
        //English Option
        page = "nickname_search";
        {
            command = "1";
            order = "1";
            code = language + ".searchby.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "province_search";
            command = "2";
            order = "2";
            code = language + ".searchby.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "gender_search";
            command = "3";
            order = "3";
            code = language + ".searchby.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "searchby";
            command = "0";
            order = "4";
            code = language + ".searchby.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getNicknameSearch(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".nickname_search.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".nickname_search.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSetDOB(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".setdob.headerText";
        //English Option
        page = "gender";
        {
            command = "0";
            order = "1";
            code = language + ".setdob.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getProvinceSearch(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".provincesearch.headerText";
        //English Option
        page = "mainwall";
        {
            command = "1";
            order = "1";
            code = language + ".province.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "2";
            order = "2";
            code = language + ".province.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "3";
            order = "3";
            code = language + ".province.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "4";
            order = "4";
            code = language + ".province.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "mainwall";
        {
            command = "5";
            order = "5";
            code = language + ".province.option5";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "6";
            order = "6";
            code = language + ".province.option6";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "7";
            order = "7";
            code = language + ".province.option7";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "8";
            order = "8";
            code = language + ".province.option8";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "9";
            order = "9";
            code = language + ".province.option9";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            //Afrikaans Option
            page = "mainwall";
            command = "0";
            order = "10";
            code = language + ".province.option10";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getViewWall(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".mywall.headerText";
        //English Option
        page = "gender";
        {
            command = "1";
            order = "1";
            code = language + ".mywall.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "gender";
        {
            command = "2";
            order = "2";
            code = language + ".mywall.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "gender";
        {
            command = "3";
            order = "3";
            code = language + ".mywall.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "gender";
        {
            command = "0";
            order = "4";
            code = language + ".mywall.option4";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPostToTheWall(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".posttothewall.headerText";
        //English Option
        page = "posttothewall";
        {
            command = "0";
            order = "1";
            code = language + ".posttothewall.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPostToTheWallSuccess(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".posttothewallsuccess.headerText";
        //English Option
        page = "posttothewall";
        {
            command = "0";
            order = "1";
            code = language + ".posttothewallsuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPostOrViewComment(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".postorviewcomment.headerText";
        //English Option
        page = "postorviewcomment";
        {
            command = "1";
            order = "1";
            code = language + ".postorviewcomment.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "postorviewcomment";
        {
            command = "2";
            order = "2";
            code = language + ".postorviewcomment.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "postorviewcomment";
        {
            command = "0";
            order = "3";
            code = language + ".postorviewcomment.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPostComment(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".postcomment.headerText";
        //English Option
        page = "postcomment";
        {
            command = "0";
            order = "1";
            code = language + ".postcomment.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPostResults(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".postcommentresults.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".postcommentresults.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getFriendRequestSuccess(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".friendrequestsuccess.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".friendrequestsuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getErrorHandler(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".errorhandler.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".errorhandler.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public UssdMenu buildMenuItem(String code, String command, String order, String url, String page, String pageHeader, boolean display, boolean isShowOption) {
        UssdMenu menu = new UssdMenu();
        menu.setCode(code);
        menu.setCommand(command);
        menu.setOrder(order);
        menu.setUrl(url);
        menu.setDisplay(display);
        menu.setPage(page);
        menu.setPageHeader(pageHeader);
        menu.setShowOptions(isShowOption);
        return menu;
    }

    @Override
    public List<UssdMenu> getTestMenu(String language) {

        Properties props = new Properties();
        InputStream input = null;
        try {
            String baseUrl = "/var/apache-tomcat-7.0.70/";
            input = new FileInputStream(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + language + ".properties");
            // load a properties file
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = MessageFormat.format((String) props.get(language + ".chomis.headerText"), "Ian");
        page = "postcommentresults";
        for (int x = 1; x < 9; x++) {
            {
                command = String.valueOf(x);
                order = x == 8 ? "0" : String.valueOf(x);
                code = MessageFormat.format((String) props.get(language + ".chomis.option" + x), "Ian");
            }
            list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        }

        return list;
    }

    @Override
    public List<UssdMenu> getChomiSendMessage(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".chomisendmessage.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".chomisendmessage.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getChomiSendMessageSuccess(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".chomisendmessagesuccess.headerText";
        //English Option
        page = "gender";
        {
            command = "1";
            order = "1";
            code = language + ".chomisendmessagesuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        page = "gender";
        {
            command = "2";
            order = "2";
            code = language + ".chomisendmessagesuccess.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getMessageReplySuccess(String language) {
        return this.getChomiSendMessageSuccess(language);
    }

    @Override
    public List<UssdMenu> getHashTag(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".hashtag.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".hashtag.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "2";
            order = "2";
            code = language + ".hashtag.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "3";
            code = language + ".hashtag.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getTrendingCreate(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".trendingcreate.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".trendingcreate.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getTrendingCreateSuccess(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".trendingcreatesuccess.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".trendingcreatesuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getTrendingSearch(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".trendingsearch.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".trendingsearch.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getMessageDelete(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".messagedeletesuccess.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".messagedeletesuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getFriendRequestDecline(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".friendrequestdecline.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".friendrequestdecline.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "2";
            code = language + ".friendrequestdecline.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getFriendRequestFailure(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".friendrequestfailure.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".friendrequestfailure.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getFullnameSearch(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".fullname_search.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".fullname_search.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getCellphoneSearch(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".cellphone_search.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".cellphone_search.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getNoUnreadMessages(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".nounreadmessages.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".nounreadmessages.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getNoFriendRequests(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".nofriendrequests.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".nofriendrequests.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getWelcomeNote(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".ageverification.headerText";
        //English Option
        page = "searchresults";
        {
            command = "1";
            order = "1";
            code = language + ".ageverification.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "2";
            code = language + ".ageverification.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getGoodByeNote(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".goodbye.headerText";
        //English Option
        page = "searchresults";
        {
            command = "0";
            order = "1";
            code = language + ".goodbye.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getFriendBlock(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".friendblock.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".friendblock.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "2";
            code = language + ".friendblock.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getMsisdNotSubscribed(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".msisdnotsubscribed.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".msisdnotsubscribed.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "2";
            code = language + ".msisdnotsubscribed.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getNewChomiNotification(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".newchominotification.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".newchominotification.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }
    
    @Override
    public List<UssdMenu> getPageCreate(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".pagecreate.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".pagecreate.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPageCreateDescription(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".pagedescription.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".pagedescription.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPageCreateType(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".pageprivacy.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".pageprivacy.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "2";
            order = "2";
            code = language + ".pageprivacy.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "3";
            code = language + ".pageprivacy.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getPageCreateSuccess(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".pagecreatesuccess.headerText";
        page = "postcommentresults";
        {
            command = "1";
            order = "1";
            code = language + ".pagecreatesuccess.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "2";
            order = "2";
            code = language + ".pagecreatesuccess.option2";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        {
            command = "0";
            order = "3";
            code = language + ".pagecreatesuccess.option3";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

    @Override
    public List<UssdMenu> getSendGroupMessage(String language) {
        List<UssdMenu> list = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        pageHeader = language + ".dynamic.sendgroupmessage.headerText";
        page = "postcommentresults";
        {
            command = "0";
            order = "1";
            code = language + ".dynamic.sendgroupmessage.option1";
        }
        list.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS));
        return list;
    }

}
