/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.chomibridge.Comment;
import com.ian.chomibridge.Group;
import com.ian.chomibridge.GroupRequestCounter;
import com.ian.chomibridge.Mail;
import com.ian.chomibridge.RequestingFriend;
import com.ian.chomibridge.TopicCount;
import com.ian.chomibridge.User;
import com.ian.entity.Post;
import com.ian.service.PropertiesFileService;
import static com.ian.util.DateCalculator.getDiffYears;
import com.ian.util.PropertiesReader;
import static com.ian.util.PropertiesReader.getDynamicMenuValue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class PropertiesFileServiceImpl implements PropertiesFileService {

    private final String baseUrl = "/var/apache-tomcat-7.0.70/";
    private final String folder = "chomiussd";
    private final int MAX_PAGE = 5;

    @Override
    public void createPostsByMeFile(String msisdn, List<Post> posts, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (posts.isEmpty()) {
                out.write(language + ".posts.headerText=" + getDynamicMenuValue(language, "dynamic.posts.headerText") + "\n");
                out.write(msisdn + ".posts.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".posts.headerText=My Posts:" + "\n");
                for (Post post : posts) {
                    if (x > MAX_PAGE) {
                        out.write(msisdn + ".posts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                        out.write(msisdn + ".posts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                        out.write(msisdn + ".posts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                        break;
                    }
                    out.write(msisdn + ".posts.option" + x + "=. " + (post.getText().length() >= 25 ? post.getText().substring(0, 25) : post.getText()) + "..." + "\n");
                    x++;
                }
                if (x <= MAX_PAGE) {
                    out.write(msisdn + ".posts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMessagesMenuFile(String msisdn, int friendRequestsCount, int messageCount, int joinRequestsCount, int groupMessageCount, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".messagesmenu.headerText=" + getDynamicMenuValue(language, "dynamic.messagesmenu.headerText") + "\n");
            out.write(msisdn + ".messagesmenu.option1=" + setDynamicValue(messageCount, language, "dynamic.messagesmenu.option1") + "\n");
            out.write(msisdn + ".messagesmenu.option2=" + setDynamicValue(friendRequestsCount, language, "dynamic.messagesmenu.option2") + "\n");
            out.write(msisdn + ".messagesmenu.option3=" + setDynamicValue(joinRequestsCount, language, "dynamic.messagesmenu.option3") + "\n");
            out.write(msisdn + ".messagesmenu.option4=" + setDynamicValue(groupMessageCount, language, "dynamic.messagesmenu.option4") + "\n");
            out.write(msisdn + ".messagesmenu.option5=" + getDynamicMenuValue(language, "dynamic.messagesmenu.option5") + "\n");
            out.write(msisdn + ".messagesmenu.option6=" + getDynamicMenuValue(language, "dynamic.messagesmenu.option6") + "\n");
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getChomiRemove(String msisdn, User user, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".chomiremove.headerText=" + setDynamicValue(user.getFirstName(), language, "dynamic.chomiremove.headerText") + "\n");
            out.write("1_" + msisdn + ".chomiremove.option1=" + getDynamicMenuValue(language, "dynamic.chomiremove.option1") + "\n");
            out.write("2_" + msisdn + ".chomiremove.option2=" + getDynamicMenuValue(language, "dynamic.chomiremove.option2") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createChomiRemoveYes(String msisdn, User user, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".chomiremoveyes.headerText=" + setDynamicValue(user.getFirstName(), language, "dynamic.chomiremoveyes.headerText") + "\n");
            out.write("1_" + msisdn + ".chomiremoveyes.option1=" + getDynamicMenuValue(language, "dynamic.chomiremoveyes.option1") + "\n");
            out.write("2_" + msisdn + ".chomiremoveyes.option2=" + getDynamicMenuValue(language, "dynamic.chomiremoveyes.option2") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createPostsListInTrendingTopicFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (posts == null || posts.isEmpty()) {
                out.write(language + ".trendingposts.headerText=" + getDynamicMenuValue(language, "dynamic.trendingposts.headerText") + "\n");
                out.write(msisdn + ".chomisposts.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".trendingposts.headerText=" + getDynamicMenuValue(language, "dynamic.trendingposts2.headerText") + "\n");
                if (posts.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "= . " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + " =. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.addpost.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.addpost.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() < MAX_PAGE && posts.size() > 0 && !(posts.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.addpost.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readProperties(String code, String language) {
        return new PropertiesReader().getPropertyValue(code, language);
    }

    @Override
    public String readProperties(String code, String msisdn, String language) {
        return new PropertiesReader().getPropertyValue(code, msisdn, language);
    }

    @Override
    public void createMyProfileFile2(String msisdn, User user, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            List list = new ArrayList();
            list.add(0, user.getFirstName());
            list.add(1, getDiffYears(user.getDob(), new Date()));
            list.add(2, user.getMsisdn());
            list.add(3, user.getLangCode());
            out.write(language + ".myprofile.headerText=" + setDynamicValue(list, language, "dynamic.myprofile.headerText") + "\n");
            out.write("1_" + msisdn + ".myprofile.option1=" + getDynamicMenuValue(language, "dynamic.myprofile.option1") + "\n");
            out.write("2_" + msisdn + ".myprofile.option2=" + getDynamicMenuValue(language, "dynamic.myprofile.option2") + "\n");
            //out.write("3_" + msisdn + ".myprofile.option3=" + getDynamicMenuValue(language, "dynamic.myprofile.option3") + "\n");
            out.write("3_" + msisdn + ".myprofile.option3=. " + "[---------]" + "\n");
            out.write("4_" + msisdn + ".myprofile.option4=" + getDynamicMenuValue(language, "dynamic.myprofile.option4") + "\n");
            out.write("5_" + msisdn + ".myprofile.option5=" + getDynamicMenuValue(language, "dynamic.myprofile.option5") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createConversationsList(String msisdn, List<com.ian.chomibridge.Conversation> conversations, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (conversations == null) {
                out.write(language + ".mymessage.headerText=" + getDynamicMenuValue(language, "dynamic.mymessage.headerText") + "\n");
                out.write(msisdn + ".mymessage.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".mymessage.headerText=" + getDynamicMenuValue(language, "dynamic.mymessage2.headerText") + "\n");
                if (conversations.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.Conversation conversation : conversations) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + conversation.getFirstName() + "(" + conversation.getCount() + ")" + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".mymessage.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (conversations.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.Conversation conversation : conversations) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + conversation.getFirstName() + "(" + conversation.getCount() + ")" + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (conversations.size() < MAX_PAGE && conversations.size() > 0 && !(conversations.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.Conversation conversation : conversations) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + conversation.getFirstName() + "(" + conversation.getCount() + ")" + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createConversationRepliesListFile(String msisdn, List<Mail> mails, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (mails.isEmpty()) {
                out.write(language + ".mymessage.headerText=" + getDynamicMenuValue(language, "dynamic.mymessage_.headerText") + "\n");
                out.write(msisdn + ".mymessage.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".mymessage.headerText=" + getDynamicMenuValue(language, "dynamic.mymessage2_.headerText") + "\n");
                if (mails.size() > MAX_PAGE) {
                    for (Mail mail : mails) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + mail.getFirstName() + ": " + (mail.getMessage().length() >= 10 ? mail.getMessage().substring(0, 10) : mail.getMessage()) + "..." + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".mymessage.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (mails.size() == MAX_PAGE) {
                    for (Mail mail : mails) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + mail.getFirstName() + ": " + (mail.getMessage().length() >= 10 ? mail.getMessage().substring(0, 10) : mail.getMessage()) + "..." + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (mails.size() < MAX_PAGE && mails.size() > 0 && !(mails.size() == MAX_PAGE)) {
                    for (Mail mail : mails) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mymessage.option" + x + "=. " + mail.getFirstName() + ": " + (mail.getMessage().length() >= 10 ? mail.getMessage().substring(0, 10) : mail.getMessage()) + "..." + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".mymessage.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMyChomisList2(String msisdn, List<User> users, String language) {
        try {

            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (/*users.isEmpty()*/users == null) {
                out.write(language + ".mychomis.headerText=" + getDynamicMenuValue(language, "dynamic.mychomis.headerText") + "\n");
                out.write(msisdn + ".mychomis.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".mychomis.headerText=" + getDynamicMenuValue(language, "dynamic.mychomis2.headerText") + "\n");
                if (users.size() > MAX_PAGE) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if ((users.size() == MAX_PAGE)) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (users.size() < MAX_PAGE && users.size() > 0 && !(users.size() == MAX_PAGE)) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMyChomisProfile(String msisdn, User user, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            List list = new ArrayList();
            list.add(0, user.getFirstName());
            list.add(1, getDiffYears(user.getDob(), new Date()));
            list.add(2, user.getMsisdn());
            list.add(3, user.getLangCode());
            out.write(language + ".mychomiprofile.headerText=" + setDynamicValue(list, language, "dynamic.mychomiprofile.headerText") + "\n");
            out.write("1_" + msisdn + ".mychomiprofile.option1=" + getDynamicMenuValue(language, "dynamic.mychomiprofile.option1") + "\n");
            out.write("2_" + msisdn + ".mychomiprofile.option2=" + getDynamicMenuValue(language, "dynamic.mychomiprofile.option2") + "\n");
            out.write("3_" + msisdn + ".mychomiprofile.option3=" + getDynamicMenuValue(language, "dynamic.mychomiprofile.option3") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createDetailedMessageFile2(String msisdn, Mail mail, int read, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".message.headerText=" + mail.getFirstName() + ": " + mail.getMessage() + "\n");
            out.write("1_" + msisdn + ".message.option1=" + getDynamicMenuValue(language, "dynamic.reply.option") + "\n");
            if (read == 0) {
                out.write("2_" + msisdn + ".message.option2=" + getDynamicMenuValue(language, "dynamic.delete.option") + "\n");
            }
            out.write("3_" + msisdn + ".message.option3=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createMessageReplyFile2(String msisdn, Mail mail, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".messagereply.headerText=" + mail.getFirstName() + ": " + mail.getMessage() + "\n");
            out.write("1_" + msisdn + ".messagereply.option1=." + getDynamicMenuValue(language, "dynamic.cancel.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createFriendRequestsListMenuFile2(String msisdn, List<RequestingFriend> requestingFriends, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (requestingFriends.isEmpty()) {
                out.write(language + ".friendrequest.headerText=" + getDynamicMenuValue(language, "dynamic.friendrequest.headerText") + "\n");
                out.write(msisdn + ".friendrequest.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".friendrequest.headerText=" + getDynamicMenuValue(language, "dynamic.friendrequest2.headerText") + "\n");
                for (RequestingFriend requestingFriend : requestingFriends) {
                    out.write(msisdn + ".friendrequest.option" + x + "=. " + requestingFriend.getFirstName() + "\n");
                    x++;
                }
                out.write(msisdn + ".friendrequest.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRequestingPersonProfile(String msisdn, RequestingFriend requestingFriend, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            //out.write(language + ".requestingperson.headerText=" + requestingFriend.getFirstName() + "'s profile, Age: " + requestingFriend.getFriendId() + ", " + requestingFriend.getUserId() + ", " + "\n");
            out.write(language + ".requestingperson.headerText=" + setDynamicValue(requestingFriend.getFirstName(), language, "dynamic.requestingperson.headerText") + "\n");
            out.write("1_" + msisdn + ".requestingperson.option1=" + getDynamicMenuValue(language, "dynamic.requestingperson.option1") + "\n");
            out.write("2_" + msisdn + ".requestingperson.option2=" + getDynamicMenuValue(language, "dynamic.requestingperson.option2") + "\n");
            out.write("3_" + msisdn + ".requestingperson.option3=" + getDynamicMenuValue(language, "dynamic.requestingperson.option3") + "\n");
            out.write("4_" + msisdn + ".requestingperson.option4=" + getDynamicMenuValue(language, "dynamic.requestingperson.option4") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createFriendRequestAccept(String msisdn, RequestingFriend requestingFriend, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".friendrequestaccept.headerText=" + setDynamicValue(requestingFriend.getFirstName(), language, "dynamic.friendrequestaccept.headerText") + "\n");
            out.write("1_" + msisdn + ".friendrequestaccept.option1=" + getDynamicMenuValue(language, "dynamic.friendrequestaccept.option1") + "\n");
            out.write("2_" + msisdn + ".friendrequestaccept.option2=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createPostsByChomisFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (posts == null) {
                out.write(language + ".chomisposts.headerText=My Wall: No post was found" + "\n");
                out.write(msisdn + ".chomisposts.option1=" + getDynamicMenuValue(language, "dynamic.posttowall.option") + "\n");
                out.write(msisdn + ".chomisposts.option2=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".chomisposts.headerText=My Wall" + "\n");
                if (posts.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.posttowall.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.posttowall.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() < MAX_PAGE && posts.size() > 0 && !(posts.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.posttowall.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCommentsForPostFile2(String postMessage, String msisdn, List<com.ian.chomibridge.Comment> comments, boolean isNormalPost, String language, boolean showPrevious) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (comments.isEmpty()) {
                if (postMessage.contains("<") | postMessage.contains(">")) {
                    out.write(language + ".comments.headerText=" + removeSpecialCharacters(postMessage) + "\n");
                } else {
                    out.write(language + ".comments.headerText=" + postMessage + "\n");
                }
                if (isNormalPost) {
                    out.write(msisdn + ".comments.option1=" + getDynamicMenuValue(language, "postorviewcomment.option1") + "\n");
                }
                out.write(msisdn + ".comments.option2=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");

            } else {
                if (postMessage.contains("<") | postMessage.contains(">")) {
                    out.write(language + ".comments.headerText=" + removeSpecialCharacters(postMessage) + "\n");
                } else {
                    out.write(language + ".comments.headerText=" + postMessage + "\n");
                }
                if (comments.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.Comment post : comments) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getComment().contains("<") | post.getComment().contains(">")) {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getComment()).length() >= 10 ? removeSpecialCharacters(post.getComment()).substring(0, 10) : removeSpecialCharacters(post.getComment())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (post.getComment().length() >= 10 ? post.getComment().substring(0, 10) : post.getComment()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".comments.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".comments.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    if (isNormalPost) {
                        out.write(msisdn + ".comments.option" + (x + 2) + "=" + getDynamicMenuValue(language, "postorviewcomment.option1") + "\n");
                    }
                    out.write(msisdn + ".comments.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (comments.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.Comment post : comments) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getComment().contains("<") | post.getComment().contains(">")) {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getComment()).length() >= 10 ? removeSpecialCharacters(post.getComment()).substring(0, 10) : removeSpecialCharacters(post.getComment())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (post.getComment().length() >= 10 ? post.getComment().substring(0, 10) : post.getComment()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".comments.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (isNormalPost) {
                        out.write(msisdn + ".comments.option" + (x + 1) + "=" + getDynamicMenuValue(language, "postorviewcomment.option1") + "\n");
                    }
                    out.write(msisdn + ".comments.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (comments.size() < MAX_PAGE && comments.size() > 0 && !(comments.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.Comment post : comments) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getComment().contains("<") | post.getComment().contains(">")) {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getComment()).length() >= 10 ? removeSpecialCharacters(post.getComment()).substring(0, 10) : removeSpecialCharacters(post.getComment())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".comments.option" + x + "=. " + post.getFirstName() + ": " + (post.getComment().length() >= 10 ? post.getComment().substring(0, 10) : post.getComment()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".comments.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (isNormalPost) {
                        out.write(msisdn + ".comments.option" + (x + 1) + "=" + getDynamicMenuValue(language, "postorviewcomment.option1") + "\n");
                    }
                    out.write(msisdn + ".comments.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMyChomisSearchFile3(String msisdn, List<User> users, String language) {
        try {

            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (users.isEmpty()) {
                out.write(language + ".friend.headerText=" + getDynamicMenuValue(language, "dynamic.friend.headerText") + "\n");
                out.write(msisdn + ".friend.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".friend.headerText=" + getDynamicMenuValue(language, "dynamic.friend2.headerText") + "\n");
                for (User person : users) {
                    if (x > MAX_PAGE) {
                        out.write(msisdn + ".friend.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                        out.write(msisdn + ".friend.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                        out.write(msisdn + ".friend.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                        break;
                    }
                    String d = (person.getFirstName().length() > 0) ? person.getFirstName() : person.getUserName();
                    out.write(msisdn + ".friend.option" + x + "=. " + d + "\n");
                    x++;
                }
                if (x <= MAX_PAGE) {
                    out.write(msisdn + ".friend.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTrendingsFile2(String msisdn, List<TopicCount> trendings, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (trendings == null) {
                out.write(language + ".trendings.headerText=" + getDynamicMenuValue(language, "dynamic.trendings2.headerText") + "\n");
                out.write(msisdn + ".trendings.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".trendings.headerText=" + getDynamicMenuValue(language, "dynamic.trendings.headerText") + "\n");
                if (trendings.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.TopicCount trending : trendings) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".trendings.option" + x + "=. " + trending.getTopic() + "(" + trending.getCount() + ")\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".trendings.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".trendings.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".trendings.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (trendings.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.TopicCount trending : trendings) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".trendings.option" + x + "=. " + trending.getTopic() + "(" + trending.getCount() + ")\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".trendings.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".trendings.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (trendings.size() < MAX_PAGE && trendings.size() > 0 && !(trendings.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.TopicCount trending : trendings) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".trendings.option" + x + "=. " + trending.getTopic() + "(" + trending.getCount() + ")\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".trendings.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".trendings.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String removeSpecialCharacters(String value) {
        String strX, strY = null, i = null;
        int x = 0;
        int y = 0;
        int loopCounter = 0;
        do {
            try {
                x = value.indexOf("<");
                y = value.indexOf(">");
                i = value.substring(x, y + 1);
                strX = value.replace(i, "");
                strY = strX.replace("</a>", "");
                value = strY;
            } catch (Exception ex) {
                return value;
            }
            loopCounter++;
        } while (loopCounter < 5);
        return strY.trim();
    }

    public static String replaceAmpersand(String str, boolean reduced) {
        String i = str.replace("&", "&#38;");
        if (!reduced) {
            return i;
        }
        if (i.length() > 13) {
            return i.substring(0, 12) + "...";
        } else {
            return i;
        }
    }

    @Override
    public void createSearchedProfile(String msisdn, User selectedUser, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            //out.write(language + ".myprofile.headerText=User Profile -- Name: " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + ", Cell No: " + selectedUser.getMsisdn() + "\n");
            List list = new ArrayList();
            list.add(selectedUser.getFirstName());
            list.add(selectedUser.getLastName());
            list.add(selectedUser.getMsisdn());
            out.write(language + ".myprofile.headerText=" + setDynamicValue(list, language, "dynamic.myprofile_.headerText") + "\n");
            out.write("1_" + msisdn + ".myprofile.option1=" + getDynamicMenuValue(language, "dynamic.myprofile_.option1") + "\n");
            out.write("2_" + msisdn + ".myprofile.option2=" + getDynamicMenuValue(language, "dynamic.myprofile_.option2") + "\n");
            out.write("3_" + msisdn + ".myprofile.option3=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createCommentOne(String msisdn, Comment comment, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".comment.headerText=" + comment.getFirstName() + ": " + comment.getComment() + "\n");
            out.write("1_" + msisdn + ".comment.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Groups Addition
    @Override
    public void createPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (groups == null) {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages.headerText") + "\n");//No pages are available
                out.write(x + msisdn + ".pages.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages2.headerText") + "\n");
                if (groups.size() > MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(x + msisdn + ".pages.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.createpage.option") + "\n");
                    out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.mypages.option") + "\n");
                    if (x == 6) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.search.option") + "\n");
                        out.write(x + msisdn + ".pages.option" + (x + 5) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 5) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() == MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.createpage.option") + "\n");
                    out.write(x + msisdn + ".pages.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.mypages.option") + "\n");
                    if (x == 7) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.search.option") + "\n");
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() < MAX_PAGE && groups.size() > 0 && !(groups.size() == MAX_PAGE)) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.createpage.option") + "\n");
                    out.write(x + msisdn + ".pages.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.mypages.option") + "\n");
                    if (x == 7) {
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.search.option") + "\n");
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSeachPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (groups == null) {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages.headerText") + "\n");//No pages are available
                out.write(x + msisdn + ".pages.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages2.headerText") + "\n");
                if (groups.size() > MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    if (x == 6) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() == MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (x == 7) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() < MAX_PAGE && groups.size() > 0 && !(groups.size() == MAX_PAGE)) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (x == 7) {
                        out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createViewPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, String language, int numberOfMembers) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            String x = "";
            x = selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText");
            String name = replaceAmpersand(selectedGroup.getName(), false);
            String description = replaceAmpersand(selectedGroup.getDescription(), false);
            out.write(language + ".viewpage.headerText=" + (name.length() >= 25 ? name.substring(0, 25) : name) + "..." + ", " + (description.length() >= 25 ? description.substring(0, 25) : description) + "..." + ", " + (selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText")) + ", Chomis(" + numberOfMembers + ")" + "\n");
            if (isFollowing) {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage.option1") + "\n");
            } else {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage2.option1") + "\n");
            }
            out.write("2_" + msisdn + ".viewpage.option2=" + getDynamicMenuValue(language, "dynamic.viewpage.option2") + "\n");
            if (isFollowing) {
                out.write("3_" + msisdn + ".viewpage.option3=" + getDynamicMenuValue(language, "dynamic.viewpage.option3") + "\n");
            }
            out.write("4_" + msisdn + ".viewpage.option4=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createViewSearchedPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, String language, int numberOfMembers) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            String x = "";
            String name = replaceAmpersand(selectedGroup.getName(), false);
            String description = replaceAmpersand(selectedGroup.getDescription(), false);
            x = selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText");
            out.write(language + ".viewpage.headerText=" + (name.length() >= 25 ? description.substring(0, 25) : name) + "..." + ", " + (description.length() >= 25 ? description.substring(0, 25) : description) + "..." + ", " + (selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText")) + ", Chomis(" + numberOfMembers + ")" + "\n");
            if (isFollowing && selectedGroup.getPublicGroup() == 1) {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage.option1") + "\n");
            } else if (!isFollowing && selectedGroup.getPublicGroup() == 1) {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage2.option1") + "\n");
            } else if (isFollowing && selectedGroup.getPublicGroup() == 0) {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage.option4") + "\n");
            } else if (!isFollowing && selectedGroup.getPublicGroup() == 0) {
                out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage.option5") + "\n");
            }
            out.write("2_" + msisdn + ".viewpage.option2=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createGroupMembers(String msisdn, List<User> users, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (users == null) {
                out.write(language + ".groupusers.headerText=" + getDynamicMenuValue(language, "dynamic.groupusers.headerText") + "\n");//No pages are available
                out.write(msisdn + ".groupusers.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".groupusers.headerText=" + getDynamicMenuValue(language, "dynamic.groupusers2.headerText") + "\n");
                if (users.size() > MAX_PAGE) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".groupusers.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".groupusers.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".groupusers.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".groupusers.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (users.size() == MAX_PAGE) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".groupusers.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".groupusers.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".groupusers.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (users.size() < MAX_PAGE && users.size() > 0 && !(users.size() == MAX_PAGE)) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".groupusers.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".groupusers.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".groupusers.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createGroupPostsFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (posts == null) {
                out.write(language + ".chomisposts.headerText=" + getDynamicMenuValue(language, "dynamic.groupposts.headerText") + "\n");//My Wall: No post was found"
                out.write(msisdn + ".chomisposts.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".chomisposts.headerText=" + getDynamicMenuValue(language, "dynamic.groupposts2.headerText") + "\n");//My Wall
                if (posts.size() > MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".chomisposts.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() == MAX_PAGE) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (posts.size() < MAX_PAGE && posts.size() > 0 && !(posts.size() == MAX_PAGE)) {
                    for (com.ian.chomibridge.Post post : posts) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        if (post.getMessage().contains("<") | post.getMessage().contains(">")) {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (removeSpecialCharacters(post.getMessage()).length() >= 10 ? removeSpecialCharacters(post.getMessage()).substring(0, 10) : removeSpecialCharacters(post.getMessage())) + "..." + "\n");
                        } else {
                            out.write(msisdn + ".chomisposts.option" + x + "=. " + post.getFirstName() + ": " + (post.getMessage().length() >= 10 ? post.getMessage().substring(0, 10) : post.getMessage()) + "..." + "\n");
                        }
                        x++;
                    }
                    if (showPrevious) {
                        out.write(msisdn + ".chomisposts.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(msisdn + ".chomisposts.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMyPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (groups == null) {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages.headerText") + "\n");//No pages are available
                out.write(x + msisdn + ".pages.option1=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".pages.headerText=" + getDynamicMenuValue(language, "dynamic.pages2.headerText") + "\n");
                if (groups.size() > MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + replaceAmpersand(group.getName(), true) + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    out.write(x + msisdn + ".pages.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    if (x == 6) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 4) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() == MAX_PAGE) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + group.getName() + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (x == 7) {
                        x++;
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                } else if (groups.size() < MAX_PAGE && groups.size() > 0 && !(groups.size() == MAX_PAGE)) {
                    for (Group group : groups) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(x + msisdn + ".pages.option" + x + "=. " + group.getName() + "\n");
                        x++;
                    }
                    if (showPrevious) {
                        out.write(x + msisdn + ".pages.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    }
                    if (x == 7) {
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    } else {
                        out.write(x + msisdn + ".pages.option" + (x + 3) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                    }
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMyViewPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, boolean isOwner, int pageRequestsCount, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            String x = "";
            x = selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText");
            String name = replaceAmpersand(selectedGroup.getName(), false);
            String description = replaceAmpersand(selectedGroup.getDescription(), false);
            out.write(language + ".viewpage.headerText=" + (name.length() >= 25 ? name.substring(0, 25) : name) + "..." + ", " + (description.length() >= 25 ? description.substring(0, 25) : description) + "..." + ", " + (selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText")) + "\n");
            out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage_.option1") + "\n");
            out.write("2_" + msisdn + ".viewpage.option2=" + getDynamicMenuValue(language, "dynamic.viewpage_.option2") + "\n");
            out.write("3_" + msisdn + ".viewpage.option3=" + getDynamicMenuValue(language, "dynamic.viewpage_.option3") + "\n");
            out.write("4_" + msisdn + ".viewpage.option4=" + setDynamicValue(pageRequestsCount, language, "dynamic.viewpage_.option4") + "\n");
            out.write("5_" + msisdn + ".viewpage.option5=" + getDynamicMenuValue(language, "dynamic.viewpage_.option5") + "\n");
            if (isOwner) {
                out.write("6_" + msisdn + ".viewpage.option6=" + getDynamicMenuValue(language, "dynamic.viewpage_.option6") + "\n");
                out.write("7_" + msisdn + ".viewpage.option7=" + getDynamicMenuValue(language, "dynamic.viewpage_.option7") + "\n");
                out.write("8_" + msisdn + ".viewpage.option8=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write("6_" + msisdn + ".viewpage.option6=" + getDynamicMenuValue(language, "dynamic.viewpage_2.option6") + "\n");
                out.write("7_" + msisdn + ".viewpage.option7=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            }

            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createMyViewPublicPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, boolean isOwner, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            String x = "";
            String name = replaceAmpersand(selectedGroup.getName(), false);
            String description = replaceAmpersand(selectedGroup.getDescription(), false);
            x = selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText");
            out.write(language + ".viewpage.headerText=" + (name.length() >= 25 ? name.substring(0, 25) : name) + "..." + ", " + (description.length() >= 25 ? description.substring(0, 25) : description) + "..." + ", " + (selectedGroup.getPublicGroup() == 1 ? getDynamicMenuValue(language, "dynamic.public.headerText") : getDynamicMenuValue(language, "dynamic.private.headerText")) + "\n");
            out.write("1_" + msisdn + ".viewpage.option1=" + getDynamicMenuValue(language, "dynamic.viewpage_.option1") + "\n");
            out.write("2_" + msisdn + ".viewpage.option2=" + getDynamicMenuValue(language, "dynamic.viewpage_.option2") + "\n");
            out.write("3_" + msisdn + ".viewpage.option3=" + getDynamicMenuValue(language, "dynamic.viewpage_.option5") + "\n");
            if (isOwner) {
                out.write("4_" + msisdn + ".viewpage.option4=" + getDynamicMenuValue(language, "dynamic.viewpage_.option6") + "\n");
                out.write("5_" + msisdn + ".viewpage.option5=" + getDynamicMenuValue(language, "dynamic.viewpage_2.option7") + "\n");
                out.write("6_" + msisdn + ".viewpage.option6=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write("4_" + msisdn + ".viewpage.option4=" + getDynamicMenuValue(language, "dynamic.viewpage_2.option6") + "\n");
                out.write("5_" + msisdn + ".viewpage.option5=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            }

            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createDeleteMyPublicPageMenu(String msisdn, Group selectedGroup, boolean isOwner, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            if (isOwner) {
                //out.write(language + ".groupdelete.headerText=Are you sure you want to delete " + replaceAmpersand(selectedGroup.getName(), false) + "?\n");
                out.write(language + ".groupdelete.headerText=" + setDynamicValue(replaceAmpersand(selectedGroup.getName(), false), language, "dynamic.groupdelete.headerText") + "\n");
            } else {
                //out.write(language + ".groupdelete.headerText=Are you sure you want to unfollow " + replaceAmpersand(selectedGroup.getName(), false) + "?\n");
                out.write(language + ".groupdelete.headerText=" + setDynamicValue(replaceAmpersand(selectedGroup.getName(), false), language, "dynamic.groupdelete2.headerText") + "\n");
            }
            out.write("1_" + msisdn + ".groupdelete.option1=" + getDynamicMenuValue(language, "dynamic.yes.option"));
            out.write("2_" + msisdn + ".groupdelete.option2=" + getDynamicMenuValue(language, "dynamic.no.option"));
            out.write("3_" + msisdn + ".groupdelete.option3=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createViewGroupMember(String msisdn, User selectedUser, boolean isOwner, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            String x = selectedUser.getFirstName().length() > 0 ? selectedUser.getFirstName() : selectedUser.getFirstName();
            out.write(language + ".viewgroupmember.headerText=" + x + ", " + selectedUser.getMsisdn() + ", " + selectedUser.getLangCode() + "\n");
            if (isOwner) {
                out.write("1_" + msisdn + ".viewgroupmember.option1=" + getDynamicMenuValue(language, "dynamic.viewgroupmember.option1") + "\n");
                out.write("2_" + msisdn + ".viewgroupmember.option2=" + getDynamicMenuValue(language, "dynamic.viewgroupmember.option2") + "\n");
            } else {
                out.write("1_" + msisdn + ".viewgroupmember.option1=" + getDynamicMenuValue(language, "dynamic.viewgroupmember.option5") + "\n");
                out.write("2_" + msisdn + ".viewgroupmember.option2=" + getDynamicMenuValue(language, "dynamic.viewgroupmember.option5") + "\n");
            }
            out.write("3_" + msisdn + ".viewgroupmember.option3=" + getDynamicMenuValue(language, "dynamic.viewgroupmember.option3") + "\n");
            out.write("4_" + msisdn + ".viewgroupmember.option4=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createPageJoinRequests(String msisdn, List<GroupRequestCounter> groupRequestCountersList, String language) {
        try {

            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (/*users.isEmpty()*/groupRequestCountersList == null) {
                out.write(language + ".mychomis.headerText=" + getDynamicMenuValue(language, "dynamic.pagejoinreqs.headerText") + "\n");
                out.write(msisdn + ".mychomis.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".mychomis.headerText=" + getDynamicMenuValue(language, "dynamic.pagejoinreqs2.headerText") + "\n");
                if (groupRequestCountersList.size() > MAX_PAGE) {
                    for (GroupRequestCounter user : groupRequestCountersList) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getGroupName() + "(" + user.getCount() + ")" + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if ((groupRequestCountersList.size() == MAX_PAGE)) {
                    for (GroupRequestCounter user : groupRequestCountersList) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getGroupName() + "(" + user.getCount() + ")" + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (groupRequestCountersList.size() < MAX_PAGE && groupRequestCountersList.size() > 0 && !(groupRequestCountersList.size() == MAX_PAGE)) {
                    for (GroupRequestCounter user : groupRequestCountersList) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getGroupName() + "(" + user.getCount() + ")" + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPageRequestUsers(String msisdn, List<User> users, String pageName, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            int x = 1;
            if (/*users.isEmpty()*/users == null) {
                out.write(language + ".mychomis.headerText=" + getDynamicMenuValue(language, "dynamic.pagejoinreqs.headerText") + "\n");
                out.write(msisdn + ".mychomis.option=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            } else {
                out.write(language + ".mychomis.headerText=" + setDynamicValue(replaceAmpersand(pageName, false), language, "dynamic.pagejoinreqs3.headerText") + "\n");
                if (users.size() > MAX_PAGE) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.previous.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.next.option") + "\n");
                    out.write(msisdn + ".mychomis.option" + (x + 2) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if ((users.size() == MAX_PAGE)) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mymessage.option" + (x) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                } else if (users.size() < MAX_PAGE && users.size() > 0 && !(users.size() == MAX_PAGE)) {
                    for (User user : users) {
                        if (x > MAX_PAGE) {
                            break;
                        }
                        out.write(msisdn + ".mychomis.option" + x + "=. " + user.getFirstName() + "\n");
                        x++;
                    }
                    out.write(msisdn + ".mychomis.option" + (x + 1) + "=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
                }
            }
            out.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createViewGroupRequestUserAcceptOrDecline(String msisdn, User user, String groupName, String language) {
        try {
            FileWriter f = new FileWriter(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + msisdn + ".properties");
            BufferedWriter out = new BufferedWriter(f);
            out.write(language + ".mychomiprofile.headerText=" + groupName + ", " + user.getFirstName() + "\n");
            out.write("1_" + msisdn + ".mychomiprofile.option1=" + getDynamicMenuValue(language, "dynamic.requestingperson.option1") + "\n");
            out.write("2_" + msisdn + ".mychomiprofile.option2=" + getDynamicMenuValue(language, "dynamic.requestingperson.option2") + "\n");
            out.write("3_" + msisdn + ".mychomiprofile.option3=" + getDynamicMenuValue(language, "dynamic.requestingperson.option3") + "\n");
            out.write("4_" + msisdn + ".mychomiprofile.option4=" + getDynamicMenuValue(language, "dynamic.back.option") + "\n");
            out.close();
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String setDynamicValue(int count, String language, String code) {
        //code : ".mainwallregistered.option3"
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = MessageFormat.format((String) props.get(language + "." + code), count);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    public static String setDynamicValue(String count, String language, String code) {
        //code : ".mainwallregistered.option3"
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = MessageFormat.format((String) props.get(language + "." + code), count);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    public static String setDynamicValue(List items, String language, String code) {
        //code : ".mainwallregistered.option3"
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = "";
            if (items.size() == 4) {
                p = MessageFormat.format((String) props.get(language + "." + code), items.get(0), items.get(1), items.get(2), items.get(3));
            }
            if (items.size() == 3) {
                p = MessageFormat.format((String) props.get(language + "." + code), items.get(0), items.get(1), items.get(2));
            }
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

}
