/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.chomibridge.Group;
import com.ian.chomibridge.GroupRequestCounter;
import com.ian.chomibridge.Mail;
import com.ian.chomibridge.RequestingFriend;
import com.ian.chomibridge.TopicCount;
import com.ian.chomibridge.User;
import com.ian.entity.Post;
import com.ian.model.UssdMenu;
import com.ian.service.UssdMenuService2;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class UssdMenuServiceImpl2 implements UssdMenuService2 {

    private final String baseUrl = "/var/apache-tomcat-7.0.70/";
    private final String folder = "chomiussd";
    private final String url = "http://139.162.206.200:8080/" + folder;
    private final boolean SHOW_OPTIONS = true;
    Properties prop = null;

    @Override
    public UssdMenu buildMenuItem(String code, String command, String order, String url, String page, String pageHeader, boolean display, boolean isShowOption, String value) {
        UssdMenu menu = new UssdMenu();
        menu.setCode(code);
        menu.setCommand(command);
        menu.setOrder(order);
        menu.setUrl(url);
        menu.setDisplay(display);
        menu.setPage(page);
        menu.setPageHeader(pageHeader);
        menu.setShowOptions(isShowOption);
        menu.setValue(value);
        return menu;
    }

    @Override
    public List<UssdMenu> getMyPosts(List<Post> posts, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".posts.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = posts.isEmpty() || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = posts == null ? "post_view_comments?postid=" : "post_view_comments?postid=" + posts.get(x - 1).getId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMessagesMenu(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".messagesmenu.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 5 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getRequestingPersonProfile(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".requestingperson.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 4 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getFriendRequestAccept(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".friendrequestaccept.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 2 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getChomiRemove(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".chomiremove.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 2 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getChomiRemoveYes(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".chomiremoveyes.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 3 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getDetailedMessages(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".message.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 3 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMessageReply(String msisdn, String language) {
        for (int x = 0; x < 3; x++) {
            System.out.println("***************************************************************************************************");
        }
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".messagereply.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 1 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, !SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getTrendingOne(String msisdn, List<com.ian.chomibridge.Post> posts, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".trendingposts.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = posts == null ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = posts == null ? "post_view_comments?postid=" : "post_view_comments?postid=" + posts.get(x - 1).getId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMessagesList3(String msisdn, List<com.ian.chomibridge.Conversation> conversations, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mymessage.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = conversations.isEmpty() || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMessagesList4(String msisdn, List<Mail> mails, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mymessage.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = mails.isEmpty() || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMyChomis2(String msisdn, List<User> users, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mychomis.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = users == null || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMyProfile(User user, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".myprofile.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 5 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
                System.out.println(x + " " + pageHeader);
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getMyChomisProfile(User user, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mychomiprofile.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 5 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
                System.out.println(x + " " + pageHeader);
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getFriendRequestList2(List<RequestingFriend> requestingFriends, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".friendrequest.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = requestingFriends.isEmpty() ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = requestingFriends.isEmpty() ? "post_view_comments?postid=" : "post_view_comments?postid=" + requestingFriends.get(x - 1).getUserId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getPostsByChomisFile2(String msisdn, List<com.ian.chomibridge.Post> posts, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".chomisposts.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                //command = (!(propertyName.contains(msisdn + ".friend.option1")) ? "0" : String.valueOf(x));
                command = posts == null || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = posts == null ? "post_view_comments?postid=" : "post_view_comments?postid=" + posts.get(x - 1).getUserId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getCommentsForPost2(List<com.ian.chomibridge.Comment> comments, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".comments.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = comments.isEmpty() ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = comments.isEmpty() ? "post_view_comments?postid=" : "post_view_comments?postid=" + comments.get(x - 1).getUserId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getTestMenu2(List<User> list, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".friend.headerText");
        prop.keys().nextElement();
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = list.isEmpty() ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getTrendingList2(String msisdn, List<TopicCount> trendings, String language) {
        System.out.println("------------------------------------------------ Trending is null " + (trendings == null));
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".trendings.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (trendings == null) || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                //page = trendings.isEmpty() ? "post_view_comments?postid=" : "post_view_comments?postid=" + trendings.get(0).getId();
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getSearchedProfile(User selectedUser, String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".myprofile.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 3 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
                System.out.println(x + " " + pageHeader);
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getCommentOne(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".comment.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 1 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
                System.out.println(x + " " + pageHeader);
            }
        }
        return menuList;
    }

    //Groups Addition
    @Override
    public List<UssdMenu> getPagesMenu(String msisdn, List<Group> groups, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".pages.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (groups == null) || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getViewPageMenu(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".viewpage.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> createGroupMembersMenu(String msisdn, List<User> users, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".groupusers.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getDeletePage(String msisdn, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".groupdelete.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> createViewGroupMember(String msisdn, User selectedUser, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".viewgroupmember.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }

        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getPageJoinRequests(String msisdn, List<GroupRequestCounter> groupRequestCountersList, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mychomis.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = groupRequestCountersList == null || propertyValue.equalsIgnoreCase("Back") ? "0" : String.valueOf(x);
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
            }
        }
        return menuList;
    }

    @Override
    public List<UssdMenu> getViewGroupRequestUserAcceptOrDecline(String msisdn, User selectedUser, String language) {
        try {
            prop = new ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(UssdMenuServiceImpl2.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UssdMenu> menuList = new ArrayList();
        String code, command, order, page, pageHeader;
        boolean display = true;
        page = "ian";
        pageHeader = (String) prop.getProperty(language + ".mychomiprofile.headerText");
        int x = 1;
        SortedMap sortedProperties = new TreeMap(prop);
        Set keySet = sortedProperties.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = prop.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
            if (propertyName.contains(msisdn)) {
                code = propertyName;
                command = (x == 5 ? "0" : String.valueOf(x));
                order = String.valueOf(x);
                x++;
                menuList.add(this.buildMenuItem(code, command, order, url, page, pageHeader, display, SHOW_OPTIONS, propertyValue));
                System.out.println(x + " " + pageHeader);
            }
        }
        return menuList;
    }

    class ReadPropertyFile {

        public Properties readFile(String msisdn) throws IOException {
            Properties prop = new Properties();
            InputStream input = null;
            try {
                input = new FileInputStream(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
                // load a properties file
                prop.load(input);
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
            return prop;
        }
    }

}
