package com.ian.mvc;

import com.ian.chomibridge.Comment;
import com.ian.chomibridge.Friend;
import com.ian.chomibridge.Group;
import com.ian.chomibridge.GroupChat;
import com.ian.chomibridge.GroupRequestCounter;
import com.ian.chomibridge.Mail;
import com.ian.chomibridge.NotificationList;
import com.ian.chomibridge.RequestingFriend;
import com.ian.chomibridge.TopicCount;
import com.ian.chomibridge.User;
import com.ian.chomibridge.UserReg;
import com.ian.entity.Post;
import com.ian.entity.USSDActivity;
import com.ian.entity.UssdSession;
import com.ian.model.ErResponse;
import com.ian.model.GroupUser;
import com.ian.model.SubResponse;
import com.ian.model.UssdMenu;
import com.ian.model.Value;
import com.ian.service.AdvertService;
import com.ian.service.PersonService;
import com.ian.service.PostService;
import com.ian.service.PropertiesFileService;
import com.ian.service.USSDActivityService;
import com.ian.service.UssdMenuService;
import com.ian.service.UssdMenuService2;
import com.ian.service.UssdSessionService;
import static com.ian.service.impl.PropertiesFileServiceImpl.replaceAmpersand;
import static com.ian.service.impl.PropertiesFileServiceImpl.setDynamicValue;
import com.ian.util.PropertiesReader;
import static com.ian.util.PropertiesReader.getDynamicMenuValue;
import static com.ian.util.Util.stringToDom;
import static com.ian.util.Utils.convertLanguage;
import static com.ian.util.Utils.hasSufficientLength;
import static com.ian.util.Utils.isNotUserSessionAbortOrTimeOut;
import static com.ian.util.Utils.startWithCharacter;
import com.ian.util.XmlResponseProcessor;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import za.co.biza.adverts.marbl.Advert;
import za.co.biza.memcache.IanInMemoryCache;

@Controller
public class NewController {

    @Autowired
    private UssdMenuService ussdMenuService;//This is a service object for getting the appropriate menus. The menus are STATIC.
    @Autowired
    UssdSessionService ussdSessionService;//This is a service object for keeping track of a USSD session.
    @Autowired
    PersonService personService;//This is a service object for adding, retrieving, updating and deleting a user/person/subscriber 
    @Autowired
    UssdMenuService2 ussdMenuService2;//This is a service object for getting the appropriate menus. The menus are DYNAMIC
    @Autowired
    PropertiesFileService propertiesFileService;//This is a service object for writing dynamic content to a properties file. The properties is used to build the menus.
    @Autowired
    PostService postService;//This is a service object for adding, retrieving, updating and deleting a post/posts
    @Autowired
    USSDActivityService uSSDActivityService;
    @Autowired
    AdvertService advertService;

    //For a simulator, the value of this 
    //constant must be index. Otherwise
    //it must be index_ussd
    private final String chomiBridge = PropertiesReader.getGeneralPropertyValue("appsettings", "prop.chomibridge");

    private final String INDEX = "index_ussd";
    private final String GOODBYE = "goodbye";
    private final int NEXT_CHOMI_ID_INCREMENT = 5;
    private final int NEXT_TRENDING_ID_INCREMENT = 5;
    private final int NEXT_POST_ID_INCREMENT = 5;
    private final int NEXT_MESSAGE_ID_INCREMENT = 5;
    private final int NEXT_COMMENT_ID_INCREMENT = 5;

    private final String VC_USSD_INBOX = PropertiesReader.getGeneralPropertyValue("appsettings", "prop.vcussdinbox");

    private final String MOUserAbort = "MO User Abort";//MOUserAbort MOSessionTimeout MOUserTimeout
    private final String MOSessionTimeout = " MO Session Timeout";
    private final String MOUserTimeout = "MO User Timeout";
    private final String host = "182.0.0.2";//private final String host = "182.0.0.2";
    private final String serviceBaseUrl = "http://" + host + ":8080/" + chomiBridge;

    IanInMemoryCache<String, ErResponse> cache = new IanInMemoryCache<>(200, 500, 50);

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model, @RequestParam("msisdn") String msisdn, @RequestParam("request") String request) {
        System.out.println("msisdn======================: " + msisdn + ", request======================: " + request + ", ChomiBridge======================:" + chomiBridge);
        cache.cleanup();

        if (request.equalsIgnoreCase(MOUserAbort)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            if (ussdSession != null) {
                ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                cache.remove(msisdn);
                cache.cleanup();
                return "";
            }
        }
        if (request.equalsIgnoreCase(MOSessionTimeout)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            if (ussdSession != null) {
                ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                cache.remove(msisdn);
                cache.cleanup();
                return "";
            }
        }
        if (request.equalsIgnoreCase(MOUserTimeout)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            if (ussdSession != null) {
                ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                cache.remove(msisdn);
                cache.cleanup();
                return "";
            }
        }
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> obj = null;
        try {
            obj = restTemplate.getForEntity(serviceBaseUrl + "/person/msisdn/" + msisdn + "/", User.class);
        } catch (Exception e) {
            model.addAttribute("pageHeader", "There seems to be a problem. We're busy fixing it. Sorry for the inconveniences caused.");
            ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
            return GOODBYE;

        }
        User user = obj.getBody();

        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (user != null) {
            if (ussdSession == null) {
                System.out.println("___________________________________________________________________________________________________________ " + user.getLangCode());
                if (user.getLangCode().equalsIgnoreCase("en")) {
                    user.setLangCode("english");
                }
                ussdSession = new UssdSession();
                ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/" + user.getId() + "/", com.ian.chomibridge.Post[].class);
                com.ian.chomibridge.Post[] posts = i.getBody();
                try {
                    List<com.ian.chomibridge.Post> postsList = Arrays.asList(posts);
                    ussdSession.setMyPosts(postsList);
                } catch (Exception ex) {
                    System.err.println("postsList is NULL");
                }
                ussdSession.setMsisdn(msisdn);
                ussdSession.setDateCreated(new Date());
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setUser(user);
                ussdSession.setNavigationLevel(100);
                ussdSessionService.saveUssdSession(ussdSession);

                //***********************************ACM SCRIPT***********************************
                if (!ussdSession.isAcmScriptCalled()) {
                    try {
                        USSDActivity _USSDActivity = new USSDActivity();
                        _USSDActivity.setAccessed(new Date());
                        _USSDActivity.setMsisdn(msisdn);
                        _USSDActivity.setUssdString(request);
                        uSSDActivityService.addUSSDActivity(_USSDActivity);
                        //CALL A SERVICE
                        restTemplate = new RestTemplate();
                        ResponseEntity<Value> x = restTemplate.getForEntity("http://" + host + ":8080/vodacom_billing_rest/subscriber/checkpgstatus/" + msisdn + "/", Value.class);
                        Value value = x.getBody();
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (value != null) {
                            if (value.getParameter2().equalsIgnoreCase("0")) {
                                model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.serviceunsuitableforminors.headerText"));
                                ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                                return GOODBYE;
                            }
                        } else {
                            System.out.println("-----------------------VALUE IS NULL----------------------------");
                        }
                        ussdSession.setAcmScriptCalled(true);
                        ussdSessionService.saveUssdSession(ussdSession);
                    } catch (Exception e) {
                        e.printStackTrace();;
                    }
                }
                //***********************************ACM SCRIPT***********************************

                //Some idea ***************************************
                if (request.equalsIgnoreCase(VC_USSD_INBOX)) {
                    return this.getMessages(model, msisdn, request);
                }
                //End of some idea ********************************
                return this.getWelcomeNote(model, msisdn, request);//Get a splash screen
            }
        } else if (ussdSession == null) {

            //***********************************ACM SCRIPT***********************************
            try {
                USSDActivity _USSDActivity = new USSDActivity();
                _USSDActivity.setAccessed(new Date());
                _USSDActivity.setMsisdn(msisdn);
                _USSDActivity.setUssdString(request);
                uSSDActivityService.addUSSDActivity(_USSDActivity);
                //CALL A SERVICE
                restTemplate = new RestTemplate();
                ResponseEntity<Value> i = restTemplate.getForEntity("http://" + host + ":8080/vodacom_billing_rest/subscriber/checkpgstatus/" + msisdn + "/", Value.class);
                Value value = i.getBody();
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (value != null) {
                    if (value.getParameter2().equalsIgnoreCase("0")) {
                        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.serviceunsuitableforminors.headerText"));
                        ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                        return GOODBYE;
                    }
                }
            } catch (Exception e) {
            }
            //***********************************ACM SCRIPT***********************************

            ussdSession = new UssdSession();
            ussdSession.setMsisdn(msisdn);
            ussdSession.setDateCreated(new Date());
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setNavigationLevel(9);
            ussdSessionService.saveUssdSession(ussdSession);
            return this.getLanguage(model, msisdn, request);
        }
        return this.getMainWallRegistered(model, msisdn, request);
    }

    public String getMainWallRegistered(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int navigationLevel = ussdSession.getNavigationLevel();
        switch (navigationLevel) {
            case -1:
                return this.getMainWallRegistered(model, msisdn, MOUserTimeout);
            case 0:
                switch (request) {
                    case "1":
                        return this.getHashTag(model, msisdn, request);
                    case "2":
                        //return this.getViewWall(model, msisdn, request);
                        return this.getPostsByMyChomis(model, msisdn, request);
                    case "3":
                        return this.getMessages(model, msisdn, request);
                    case "4":
                        return this.getMyProfile(model, msisdn, request);
                    case "5":
                        return this.getMyChomis(model, msisdn, request);
                    case "6":
                        return this.getSearchBy(model, msisdn, request);
                    //Groups Addition
                    case "7":
                        return this.getPages(model, msisdn, request);
                    case "8":
                        String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Menu&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
                        Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
                        return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 30001);
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 1:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                        return this.getTrendingList(model, msisdn, request);
                    case "2":
                        return this.getTrendingSearch(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 2:
                switch (request) {
                    case "1":
                        return this.getPostsByMyChomis(model, msisdn, request);
                    case "2":
                        return this.getPostsByMe(model, msisdn, request);
                    case "3":
                        return this.getPostToTheWall(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 21:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        if (isNotUserSessionAbortOrTimeOut(request)) {
                            int x = ussdSession.getNextPostId();
                            List<com.ian.chomibridge.Post> posts1 = ussdSession.getMyPosts();
                            List<com.ian.chomibridge.Post> posts = posts1.subList(x - NEXT_POST_ID_INCREMENT, posts1.size());
                            String postId = String.valueOf(posts.get(Integer.valueOf(request) - 1).getMessageId());
                            ussdSession.setPostId(postId);
                            ussdSessionService.updateUssdSession(ussdSession);
                            return this.getCommentsForPost(model, msisdn, request);
                        }
                    case "6":
                        int i = ussdSession.getNextPostId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPostId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getPostsByChomisNext(model, msisdn, request);
                            } else {
                                return this.getPostsByMyChomis(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getPostsByChomisNext(model, msisdn, request);
                    case "9":
                        return this.getPostToTheWall(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    //return this.getViewWall(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 22:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getPostOrViewComment(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getPostsByMeNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 227:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getPostOrViewComment(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getPostsByMeNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 211:
                switch (request) {
                    case "1":
                        return this.getPostComment(model, msisdn, request);
                    case "2":
                        return this.getCommentsForPost(model, msisdn, request);
                    case "7":
                        return this.getNextCommentsForPost(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 2112:
                switch (request) {
                    case "0":
                        return this.getPostsByMyChomis(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getCommentOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextCommentId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextCommentId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getNextCommentsForPost(model, msisdn, request);
                            } else {
                                return this.getCommentsForPost(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getNextCommentsForPost(model, msisdn, request);
                    case "9":
                        return this.getPostComment(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 21121:
                switch (request) {
                    case "0":
                        return this.getCommentsForPost(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 211211:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 2113:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getCommentOneTrendingPost(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextCommentId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextCommentId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getNextCommentsForTrendingPost(model, msisdn, request);
                            } else {
                                return this.getTrendingOnePost(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getNextCommentsForTrendingPost(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 2111:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextCommentId();
                        if (i > 5) {
                            i = i - 5;
                            ussdSession.setNextCommentId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getNextCommentsForPost(model, msisdn, request);
                            } else {
                                return this.getCommentsForPost(model, msisdn, request);
                            }
                        } else {
                            return this.getCommentsForPost(model, msisdn, request);
                        }
                    default:
                        return this.getPostCommentResult(model, msisdn, request);
                }
            case 23:
                switch (request) {
                    case "0":
                        return this.getPostsByMyChomis(model, msisdn, request);
                    //return this.getViewWall(model, msisdn, request);
                    default:
                        return this.getPostToTheWallSuccess(model, msisdn, request);
                }
            case 1179:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    default:
                        return this.getPostTopicToTheWallSuccess(model, msisdn, request);
                }
            case 231:
                switch (request) {
                    case "0":
                        return this.getPostsByMyChomis(model, msisdn, request);
                    //return this.getViewWall(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 11719:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 3:
                switch (request) {
                    case "1":
                        ussdSession.setRead(0);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getConversations(model, msisdn, request, 0);
                    case "2":
                        return this.getFriendRequestsList(model, msisdn, request);
                    case "3":
                        return this.getPageJoinRequests(model, msisdn, request);
                    case "4":
                        return this.getGroupConversations(model, msisdn, request, 0);
                    case "9":
                        ussdSession.setRead(1);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getConversations(model, msisdn, request, 1);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 30:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        return this.getMessages(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 301:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        return this.getHashTag(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 302:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        return this.getPostsByMyChomis(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 303:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        return this.getPages(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 30001:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 3001:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        return this.getWelcomeNote(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 300:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 3000:
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    case "1":
                        if (!ussdSession.isAdvertServed4() && !ussdSession.isSubscribed()) {
                            String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Wall&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
                            Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
                            return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 3001);
                        }
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 32:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getFriendRequestsPerson(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 321:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    case "1":
                        return this.getFriendRequestAccept(model, msisdn, request);
                    case "2":
                        return this.getFriendRequestPersonDecline(model, msisdn, request);
                    case "3":
                        return this.getBlockFriendRequest(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 3211:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    case "1":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 33:
                switch (request) {
                    case "0":
                        /*int i = ussdSession.getNextGroupRequestCounterId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextGroupRequestCounterId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getPageJoinRequestsNext(model, msisdn, request);
                            } else {
                                return this.getPageJoinRequests(model, msisdn, request);
                            }
                        } else {
                            return this.getPageJoinRequests(model, msisdn, request);
                        }*/
                        return this.getMessages(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewGroupRequestUsers(model, msisdn, request);
                    case "7":
                        return this.getPageJoinRequestsNext(model, msisdn, request);
                    default:
                        return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
                }
            case 331:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextGroupRequestCounterId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextGroupRequestCounterId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getPageJoinRequestsNext(model, msisdn, request);
                            } else {
                                return this.getPageJoinRequests(model, msisdn, request);
                            }
                        } else {
                            return this.getPageJoinRequests(model, msisdn, request);
                        }
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewGroupRequestUserAcceptOrDecline(model, msisdn, request);
                    case "7":
                        return this.getViewGroupRequestUsersNext(model, msisdn, request);
                    default:
                        return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
                }
            case 3311:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextChomiId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextChomiId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewGroupRequestUsersNext(model, msisdn, request);
                            } else {
                                return this.getViewGroupRequestUsers(model, msisdn, request);
                            }
                        } else {
                            return this.getViewGroupRequestUsers(model, msisdn, request);
                        }
                    case "1":
                        return this.getGroupRequestAccept(model, msisdn, request);
                    case "2":
                        return this.getGroupRequestReject(model, msisdn, request);
                    case "3":
                        return this.getGroupRequestBlock(model, msisdn, request);
                    default:
                }
            case 4:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                        return this.getSetDOB(model, msisdn, request);
                    case "2":
                        return this.getSetGender(model, msisdn, request);
                    case "3":
                        return this.getSetProvince(model, msisdn, request);
                    case "4":
                        return this.getSetLanguage(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 41:
                switch (request) {
                    case "0":
                        return this.getMyProfile(model, msisdn, request);
                    case MOUserAbort:
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getDOBUpdate(model, msisdn, request);
                }
                break;
            case 42:
                switch (request) {
                    case "0":
                        return this.getMyProfile(model, msisdn, request);
                    case MOUserAbort:
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                    case "2":
                        //case "3":
                        return this.getGenderUpdate(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 43:
                return this.getProvinceUpdate(model, msisdn, request);
            case 44:
                switch (request) {
                    case "0":
                        return this.getMyProfile(model, msisdn, request);
                    case MOUserAbort:
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                    case "10":
                    case "11":
                    case "12":
                        return this.getLanguageUpdate(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 5:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getMyChomisOneChomi(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getMyChomisNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 51:
                switch (request) {
                    case "1":
                        return this.getChomiSendMessage(model, msisdn, request);
                    case "2":
                        return this.getChomiRemove(model, msisdn, request);
                    case "0":
                        return this.getMyChomis(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 511:
                return this.getChomiSendMessageSuccess(model, msisdn, request);
            case 5111:
                switch (request) {
                    case "1":
                        this.defaultLevel(ussdSession);
                        return this.getMainWallRegistered(model, msisdn, "");
                    case "2":
                        return this.getMyChomis(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 512:
                switch (request) {
                    case "1":
                        return this.getChomiRemoveYes(model, msisdn, request);
                    case "0":
                        return this.getMyChomisOneChomi(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 5121:
                switch (request) {
                    case "1":
                        this.defaultLevel(ussdSession);
                        return this.getMainWallRegistered(model, msisdn, "");
                    case "2":
                        return this.getMyChomis(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 6:
                switch (request) {
                    case "1":
                        return this.getNickNameSearch(model, msisdn, request);
                    case "2":
                        return this.getFullNameSearch(model, msisdn, request);
                    case "3":
                        return this.getCellphoneSearch(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 611:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        return this.getSearchBy(model, msisdn, request);
                    default:
                        return this.getSearchResults(model, msisdn, request);
                }
            case 6111:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    case "2":
                        return this.getBlockPerson(model, msisdn, request);
                    default:
                        return this.getPersonSendRequest(model, msisdn, request);
                }
            case 61110:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    case "1":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 612:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    default:
                        return this.getSearchResults(model, msisdn, request);
                }
            case 613:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    default:
                        return this.getSearchResults(model, msisdn, request);
                }
            case 6131:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getSearchedProfile(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 61311:
                switch (request) {
                    case "0":
                        return this.getSearchBy(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 9:
                switch (request) {
                    case "0":
                        return this.getLanguage(model, msisdn, request);
                    default:
                        return this.getAgeVerification(model, msisdn, request);
                }
            case 900:
                switch (request) {
                    case "1":
                        return this.getLanguage(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 901:
                switch (request) {
                    case "1":
                        return this.getMainWallAfterNewChomiNotification(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 10:
                return this.getLanguage(model, msisdn, request);
            case 91:
                switch (request) {
                    case "1":
                        return this.getSubscribe(model, msisdn, request);
                    case "0":
                        return this.getLanguage(model, msisdn, request);
                    default:
                }
            case 911:
                switch (request) {
                    case "1":
                        return this.getSubscribeConfirm(model, msisdn, request);
                    case "2":
                        return this.getTermsAndConditons(model, msisdn, request);
                    case "0":
                        return this.getAgeVerification(model, msisdn, request);
                    default:
                }
            case 9111:
                switch (request) {
                    case "1":
                        return this.getSuccessful(model, msisdn, request);
                    case "0":
                        return this.getSubscribe(model, msisdn, request);
                    default:
                }
            case 91111:
                switch (request) {
                    case "0":
                        return this.getSubscribeConfirm(model, msisdn, request);
                    default:
                        return this.getAgePrompt(model, msisdn, request);
                }
            case 911111:
                return this.getGenderPrompt(model, msisdn, request);
            case 9111111:
                this.getProvincePrompt(model, msisdn, request);
            case 91111111:
                return this.getMainWall(model, msisdn, request);
            case 11:
                switch (request) {
                    case "0":
                        return this.getHashTag(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getTrendingOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextTrendingId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextTrendingId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getTrendingListNext(model, msisdn, request);
                            } else {
                                return this.getTrendingList(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getTrendingListNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 117:
                switch (request) {
                    case "0":
                        return this.getHashTag(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getTrendingOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextTrendingId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextTrendingId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getTrendingListNext(model, msisdn, request);
                            } else {
                                return this.getTrendingList(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getTrendingListNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 1170:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 1171:
                switch (request) {
                    case "0":
                        return this.getTrendingList(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getTrendingOnePost(model, msisdn, request);
                    case "6":
                        System.out.println("--------------------------------------------------------- We're here ------------------------------------------------------------");
                        int i = ussdSession.getTrendingOneNextId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setTrendingOneNextId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getTrendingOneNext(model, msisdn, request);
                            } else {
                                return this.getTrendingOne(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getTrendingOneNext(model, msisdn, request);
                    case "9":
                        return this.getPostTopicToTheWall(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 11711:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                    //trending_post_comment_on_one Coming Soon
                    case "2":
                    //trending_post_view_comment Coming soon
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 13:
                switch (request) {
                    case "0":
                        return this.getHashTag(model, msisdn, request);
                    default:
                        return this.getTrendingSearchResults(model, msisdn, request);
                }
            case 31:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getInbox(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextConversationId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextConversationId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getConversationsNext(model, msisdn, request);
                            } else {
                                return this.getConversations(model, msisdn, request, ussdSession.getRead());
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getConversationsNext(model, msisdn, request);

                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 34:
                switch (request) {
                    case "0":
                        return this.getMessages(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getGroupInbox(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextConversationId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextConversationId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getConversationsNext(model, msisdn, request);
                            } else {
                                return this.getGroupConversations(model, msisdn, request, ussdSession.getRead());
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getConversationsNext(model, msisdn, request);

                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 37:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getInbox(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getConversationsNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
            case 3111:
                switch (request) {
                    case "1":
                        return this.getMessageReply(model, msisdn, request);
                    case "2":
                        return this.getMessageDelete(model, msisdn, request);
                    case "0":
                        //return this.getConversations(model, msisdn, request, ussdSession.getRead());
                        return this.getInbox(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 31110:
                switch (request) {
                    case "0":
                        return this.getGroupConversations(model, msisdn, request, ussdSession.getRead());
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 34110:
                switch (request) {
                    case "0":
                        return this.getGroupConversations(model, msisdn, request, ussdSession.getRead());
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 31111:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getMessageReplySuccess(model, msisdn, request);
                }
                break;
            case 31112:
                System.out.println("---------------------------------------------Are we getting here?-------------------------------------------------------------");
                return this.getInboxOne(model, msisdn, request);
            case 311111:
                return this.getMessageReplySuccess(model, msisdn, request);
            case 3111111:
                switch (request) {
                    case "1":
                        this.defaultLevel(ussdSession);
                        break;
                    case "2":
                        return this.getMyChomis(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
            case 311112:
                switch (request) {
                    case "0":
                        return this.getInbox(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 317:
                switch (request) {
                    case "0":
                        //Interesting update
                        ussdSession.setSelectedRequest(null);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getConversations(model, msisdn, request, ussdSession.getRead());
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getInboxOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextMessageId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextMessageId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getInboxNext(model, msisdn, request);
                            } else {
                                return this.getInbox(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getInboxNext(model, msisdn, request);
                    default:
                        break;
                }
            case 347:
                switch (request) {
                    case "0":
                        //Interesting update
                        ussdSession.setSelectedRequest(null);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getConversations(model, msisdn, request, ussdSession.getRead());
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getGroupInboxOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextMessageId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextMessageId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getGroupInboxNext(model, msisdn, request);
                            } else {
                                return this.getGroupInbox(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getGroupInboxNext(model, msisdn, request);
                    default:
                        break;
                }
            case 57:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getMyChomisOneChomi(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getMyChomisNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 311:
                switch (request) {
                    case "0":
                        //Interesting update
                        ussdSession.setSelectedRequest(null);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getConversations(model, msisdn, request, ussdSession.getRead());
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getInboxOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextMessageId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextMessageId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getInboxNext(model, msisdn, request);
                            } else {
                                return this.getInbox(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getInboxNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 341:
                switch (request) {
                    case "0":
                        //Interesting update
                        ussdSession.setSelectedRequest(null);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getGroupConversations(model, msisdn, request, ussdSession.getRead());
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getGroupInboxOne(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextMessageId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextMessageId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getGroupInboxNext(model, msisdn, request);
                            } else {
                                return this.getGroupInbox(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getGroupInboxNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 3411:
                switch (request) {
                    case "0":
                        return this.getGroupInbox(model, msisdn, request);
                    case "1":
                        return this.getGroupMessageReply(model, msisdn, request);
                    case "2":
                        return getGroupMessageDelete(model, msisdn, request);
                    default:
                }
            case 100:
                this.defaultLevel(ussdSession);
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    default:
                }
                break;
            case 101:
                this.defaultLevel(ussdSession);
                switch (request) {
                    case "0":
                        return this.getGoodByeNote(model, msisdn, request);
                    default:
                }
                break;
            case 1800:
            case 1801:
            case 1802:
                return this.getWelcomeNote(model, msisdn, request);
            case 7:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewPage(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextPagesId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPagesId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getPagesNext(model, msisdn, request);
                            } else {
                                return this.getPages(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getPagesNext(model, msisdn, request);
                    case "8":
                        return this.getPageCreate(model, msisdn, request);
                    case "9":
                        return this.getMyPages(model, msisdn, request);
                    case "10":
                        return this.getSearchForPages(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 710:
                switch (request) {
                    case "0":
                        return this.getPages(model, msisdn, request);
                    default:
                        return this.getSearchForPagesResults(model, msisdn, request);
                }
            case 7101:
                switch (request) {
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewSearchedPage(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextPagesId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPagesId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getSearchForPagesNext(model, msisdn, request);
                            } else {
                                return this.getSearchForPages(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getSearchForPagesNext(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 71011:
                switch (request) {
                    case "0":
                        return this.getPages(model, msisdn, request);
                    case "1":
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/", Boolean.class);
                        if (i.getBody()) {
                            return this.getUnFollow(model, msisdn, request);
                        } else {
                            return this.getFollow(model, msisdn, request);
                        }
                }
                break;
            case 701:
                switch (request) {
                    case "0":
                        ussdSession.setNextPagesId(0);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getPages(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        int x = ussdSession.getGroupsSublist().get(Integer.valueOf(request) - 1).getPublicGroup();
                        if (x == 1) {
                            return this.getViewMyPage(model, msisdn, request);
                        } else if (x == 0) {
                            return this.getViewMyPrivatePage(model, msisdn, request);
                        }
                    case "6":
                        int i = ussdSession.getNextPagesId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPagesId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getMyPagesNext(model, msisdn, request);
                            } else {
                                return this.getMyPages(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getMyPagesNext(model, msisdn, request);
                    case "8":
                        return this.getPageCreate(model, msisdn, request);
                    case "9":
                        return this.getMyPages(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 71:
                switch (request) {
                    case "0":
                        return this.getPages(model, msisdn, request);
                    case "1":
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/", Boolean.class);
                        if (i.getBody()) {
                            return this.getUnFollow(model, msisdn, request);
                        } else {
                            return this.getFollow(model, msisdn, request);
                        }
                    case "2":
                        return this.getViewGroupMembers(model, msisdn, request);
                    case "3":
                        return this.getViewGroupPosts(model, msisdn, request);
                    default:
                }
            case 712:
                switch (request) {
                    case "0":
                        return this.getViewPage(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getErrorHandler(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextGroupMemberId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextGroupMemberId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewGroupMembersNext(model, msisdn, request);
                            } else {
                                return this.getViewGroupMembers(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getViewGroupMembersNext(model, msisdn, request);
                    default:
                        break;
                }
                break;
            case 78:
                switch (request) {
                    case "0":
                        return this.getPages(model, msisdn, request);
                    default:
                        return this.getPageCreateDescription(model, msisdn, request);
                }
            case 781:
                switch (request) {
                    case "0":
                        return this.getPageCreateDescription(model, msisdn, request);
                    default:
                        return this.getPageCreateType(model, msisdn, request);
                }
            case 7811:
                switch (request) {
                    case "0":
                        return this.getPageCreateType(model, msisdn, request);
                    default:
                        return this.getCanMembersCanPost(model, msisdn, request);
                }
            case 78111:
                switch (request) {
                    case "":
                        break;
                    default:
                        return this.getPageCreateSuccess(model, msisdn, request);
                }
            case 781111:
                switch (request) {
                    case "0":
                        return this.getPages(model, msisdn, request);
                    case "1":
                        return this.getViewCreatedPage(model, msisdn, request);
                    case "2":
                        return this.getPages(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 713:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        List<com.ian.chomibridge.Post> posts1 = null;
                        try {
                            //CALL A SERVICE
                            RestTemplate restTemplate = new RestTemplate();
                            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
                            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase() + "/", com.ian.chomibridge.Post[].class);
                            com.ian.chomibridge.Post[] groupPosts = i.getBody();
                            try {
                                posts1 = Arrays.asList(groupPosts);
                            } catch (Exception e) {
                            }
                        } catch (Exception e) {
                        }
                        int x = ussdSession.getNextPostId();

                        List<com.ian.chomibridge.Post> posts = posts1.subList(x - NEXT_POST_ID_INCREMENT, posts1.size());
                        String postId = String.valueOf(posts.get(Integer.valueOf(request) - 1).getMessageId());
                        ussdSession.setPostId(postId);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getCommentsForGroupPost(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextPostId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPostId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewGroupPostsNext(model, msisdn, request);
                            } else {
                                return this.getViewGroupPosts(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getViewGroupPostsNext(model, msisdn, request);
                    case "9":
                        return this.getPostToTheWall(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 7131:
                switch (request) {
                    case "0":
                        return this.getViewGroupPosts(model, msisdn, request);
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getCommentOne_(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextCommentId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextCommentId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getCommentsForGroupPostNext(model, msisdn, request);
                            } else {
                                return this.getCommentsForGroupPost(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getCommentsForGroupPostNext(model, msisdn, request);
                    case "9":
                        return this.getPostComment(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 71311:
                switch (request) {
                    case "0":
                        return this.getCommentsForGroupPost(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 7011:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextGroupMemberId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextPagesId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getMyPagesNext(model, msisdn, request);
                            } else {
                                return this.getMyPages(model, msisdn, request);
                            }
                        } else {
                            return this.getMyPages(model, msisdn, request);
                        }
                    case "1":
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/", Boolean.class);
                        if (k.getBody() || ussdSession.getSelectedGroup().getPublicCanPost() == 1) {
                            return this.getAddPublicGroupPost(model, msisdn, request);
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.membersnotallowedtopost.headerText"));
                        }
                    case "2":
                        return this.getMyViewPublicGroupPosts(model, msisdn, request);
                    case "3":
                        return this.getViewMyPublicGroupMembers(model, msisdn, request);
                    case "4":
                        return this.getDeletePage(model, msisdn, request);
                    case "5":
                        return this.getManageFollowers(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 70111:
                switch (request) {
                    case "0":
                        return this.getViewPage(model, msisdn, request);
                    default:
                        return this.getAddPublicGroupPostSuccess(model, msisdn, request);
                }
            case 70113:
                switch (request) {
                    case "0":
                        int x = ussdSession.getSelectedGroup().getPublicGroup();
                        if (x == 1) {
                            return this.getViewMyPage(model, msisdn, request);
                        } else if (x == 0) {
                            return this.getViewMyPrivatePage(model, msisdn, request);
                        }
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewGroupMember(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextGroupMemberId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextGroupMemberId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewMyPublicGroupMembersNext(model, msisdn, request);
                            } else {
                                return this.getViewMyPublicGroupMembers(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getViewMyPublicGroupMembersNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 70114:
                switch (request) {
                    case "0":
                        return this.getViewMyPage(model, msisdn, request);
                    case "1":
                    case "2":
                        return this.getDeletePageSuccess(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 70115:
                int value = 0;
                switch (request) {
                    case "0":
                        return this.getViewPage(model, msisdn, request);
                    case "1":
                        value = 1;
                        break;
                    case "2":
                        value = 0;
                        break;
                    default:
                        return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
                }
                //CALL A SERVICE
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getForEntity(serviceBaseUrl + "/groupmemberscanpost/" + ussdSession.getSelectedGroup().getId() + "/" + value + "/", Void.class);
                    return this.getViewPage(model, msisdn, "0");
                } catch (Exception e) {
                    e.printStackTrace();
                    return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
                }
            case 70126:
                switch (request) {
                    case "0":
                        int x = ussdSession.getSelectedGroup().getPublicGroup();
                        if (x == 1) {
                            return this.getViewMyPage(model, msisdn, request);
                        } else if (x == 0) {
                            return this.getViewMyPrivatePage(model, msisdn, request);
                        }
                    case "1":
                    case "2":
                        return this.getDeletePrivatePageSuccess(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 701261:
                return this.getPages(model, msisdn, request);
            case 701262:
                return this.getPages(model, msisdn, request);
            case 70112:
                switch (request) {
                    case "0":
                        int x = ussdSession.getSelectedGroup().getPublicGroup();
                        if (x == 1) {
                            return this.getViewMyPage(model, msisdn, request);
                        } else if (x == 0) {
                            return this.getViewMyPrivatePage(model, msisdn, request);
                        }
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        List<com.ian.chomibridge.Post> posts1 = null;
                        try {
                            //CALL A SERVICE
                            RestTemplate restTemplate = new RestTemplate();
                            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
                            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase() + "/", com.ian.chomibridge.Post[].class);
                            com.ian.chomibridge.Post[] groupPosts = i.getBody();
                            try {
                                posts1 = Arrays.asList(groupPosts);
                            } catch (Exception e) {
                            }
                        } catch (Exception e) {
                        }
                        int x1 = ussdSession.getNextPostId();

                        List<com.ian.chomibridge.Post> posts = posts1.subList(x1 - NEXT_POST_ID_INCREMENT, posts1.size());
                        String postId = String.valueOf(posts.get(Integer.valueOf(request) - 1).getMessageId());
                        ussdSession.setPostId(postId);
                        ussdSessionService.updateUssdSession(ussdSession);
                        return this.getCommentsForGroupPost(model, msisdn, request);
                    case "6":
                        int i = ussdSession.getNextPostId();
                        if (i > 5) {
                            i = i - 10;
                            ussdSession.setNextPostId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getMyViewPublicGroupPostsNext(model, msisdn, request);
                            } else {
                                return this.getMyViewPublicGroupPosts(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "7":
                        return this.getMyViewPublicGroupPostsNext(model, msisdn, request);
                    case "9":
                        return this.getPostToTheWall(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 701131:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextGroupMemberId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextGroupMemberId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewMyPublicGroupMembersNext(model, msisdn, request);
                            } else {
                                return this.getViewMyPublicGroupMembers(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    case "1":
                        //CALL A SERVICE
                        RestTemplate restTemplate = new RestTemplate();
                        Group selectedGroup = ussdSession.getSelectedGroup();
                        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
                        if (k.getBody()) {
                            return this.getConfirmRemoveFromPage(model, msisdn, request);
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "That option was disabled for you.");
                        }
                    case "2":
                        //CALL A SERVICE
                        restTemplate = new RestTemplate();
                        selectedGroup = ussdSession.getSelectedGroup();
                        k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
                        if (k.getBody()) {
                            return this.getCanUserPostOnPage(model, msisdn, request);
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "That option was disabled for you.");
                        }
                    case "3":
                        return this.sendChomiRequestFromGroup(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 7011313:
                switch (request) {
                    case "0":
                        return this.getViewMyPrivateGroupMembers(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 7011311:
                switch (request) {
                    case "0":
                        //return this.getViewGroupMember(model, msisdn, request);
                        int i = ussdSession.getNextGroupMemberId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextGroupMemberId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getViewMyPublicGroupMembersNext(model, msisdn, request);
                            } else {
                                return this.getViewMyPublicGroupMembers(model, msisdn, request);
                            }
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
                        }
                    default:
                        return this.getConfirmRemoveFromPageComplete(model, msisdn, request);
                }
            case 70113111:
                switch (request) {
                    case "0":
                        return this.getViewMyPage(model, msisdn, request);
                }
                break;
            case 7011312:
                return this.getCanUserPostOnPageComplete(model, msisdn, request);
            case 7012:
                switch (request) {
                    case "0":
                        int i = ussdSession.getNextGroupMemberId();
                        if (i >= 5) {
                            i = i - 5;
                            ussdSession.setNextPagesId(i);
                            ussdSessionService.updateUssdSession(ussdSession);
                            if (i > 0) {
                                return this.getMyPagesNext(model, msisdn, request);
                            } else {
                                return this.getMyPages(model, msisdn, request);
                            }
                        } else {
                            return this.getMyPages(model, msisdn, request);
                        }
                    case "1":
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/", Boolean.class);
                        if (k.getBody() || ussdSession.getSelectedGroup().getPublicCanPost() == 1) {
                            return this.getAddPrivateGroupPost(model, msisdn, request);
                        } else {
                            return this.getSomethingWentWrong(model, ussdSession, true, "Members are not allowed to post on the wall of this page.");
                        }
                    case "2":
                        return this.getMyViewPrivateGroupPosts(model, msisdn, request);
                    case "3":
                        return this.getSendGroupMessage(model, msisdn, request);
                    case "4":
                        return this.getJoinRequests(model, msisdn, request);
                    case "5":
                        return this.getViewMyPrivateGroupMembers(model, msisdn, request);
                    case "6":
                        return this.getDeletePrivatePage(model, msisdn, request);
                    case "7":
                        return this.getManageFollowers(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
            case 70123:
                switch (request) {
                    case "0":
                        break;
                    default:
                        return this.getSendGroupMessageSuccess(model, msisdn, request);
                }
            case 70124:
                switch (request) {
                    case "0":
                        int x = ussdSession.getSelectedGroup().getPublicGroup();
                        if (x == 1) {
                            return this.getViewMyPage(model, msisdn, request);
                        } else if (x == 0) {
                            return this.getViewMyPrivatePage(model, msisdn, request);
                        }
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewGroupRequestUserAcceptOrDecline(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getJoinRequestsNext(model, msisdn, request);
                    default:
                        return this.getErrorHandler(model, msisdn, request);
                }
                break;
            case 701247:
                switch (request) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return this.getViewGroupRequestUserAcceptOrDecline(model, msisdn, request);
                    case "6":
                        break;
                    case "7":
                        return this.getJoinRequestsNext(model, msisdn, request);
                    case "0":
                        this.defaultLevel(ussdSession);
                        break;
                    default:
                        break;
                }
                break;
        }

        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<Long> i = restTemplate.getForEntity(serviceBaseUrl + "/all-my-messages/unread/count/" + user.getId() + "/0/", Long.class);
            int messageCount = i.getBody().intValue();
            ResponseEntity<Long> j = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requested/" + user.getId() + "/", Long.class);
            int friendRequestCount = j.getBody().intValue();
            ResponseEntity<Long> k = restTemplate.getForEntity(serviceBaseUrl + "/group/myjoinrequestscount/" + user.getId() + "/", Long.class);
            int joinRequestsCount = k.getBody().intValue();
            ResponseEntity<Long> l = restTemplate.getForEntity(serviceBaseUrl + "/all-my-group-messages/unread/count/" + user.getId() + "/0/", Long.class);
            int groupMessageCount = l.getBody().intValue();
            int count = messageCount + friendRequestCount + joinRequestsCount + groupMessageCount;
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getMainWallRegistered(user.getLangCode().toLowerCase());
            this.setMenuItems(model, list, count, advertService.getAdvert("http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Menu&responseType=ussd&xmlwrapped=false&clientref=" + user.getMsisdn()).getDescription(), user.getLangCode().toLowerCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return INDEX;
    }

    //Level 9
    public String getLanguage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);

        if (!ussdSession.isNotifiedOfNewChomi()) {
            try {
                model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.chomihasbeenupgraded.headerText"));
            } catch (Exception e) {
                model.addAttribute("pageHeader", getDynamicMenuValue("english", "dynamic.chomihasbeenupgraded.headerText"));
            }
            ussdSession.setNotifiedOfNewChomi(true);
            ussdSession.setNavigationLevel(900);
            ussdSessionService.updateUssdSession(ussdSession);
            return INDEX;
        }

        ussdSession.setMsisdn(msisdn);
        ussdSession.setDateCreated(new Date());
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setLanguage("english");
        ussdSession.setNavigationLevel(9);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getLanguage(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    //Level 91
    public String getAgeVerification(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        String language = null;
        if (ussdSession.getLanguage() == null) {
            language = convertLanguage(request);
        } else {
            language = ussdSession.getLanguage();
        }
        ussdSession.setLanguage(language);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(91);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getAgeVerification(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    //Level 1
    public String getHashTag(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (!ussdSession.isAdvertServed() && !ussdSession.isSubscribed()) {
            String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Hashtag&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
            Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
            return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 301);
        }
        ussdSession.setNavigationLevel(1);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getHashTag(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 2
    public String getViewWall(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(2);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getViewWall(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 3
    public String getMessages(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (!ussdSession.isAdvertServed3() && !ussdSession.isSubscribed()) {
            String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Chat&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
            Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
            return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 30);
        }
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
        ResponseEntity<Long> i = restTemplate.getForEntity(serviceBaseUrl + "/all-my-messages/unread/count/" + user.getId() + "/0/", Long.class);
        int messageCount = i.getBody().intValue();
        ResponseEntity<Long> j = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requested/" + user.getId() + "/", Long.class);
        int friendRequestCount = j.getBody().intValue();
        ResponseEntity<Long> k = restTemplate.getForEntity(serviceBaseUrl + "/group/myjoinrequestscount/" + user.getId() + "/", Long.class);
        int joinRequestsCount = k.getBody().intValue();
        ResponseEntity<Long> l = restTemplate.getForEntity(serviceBaseUrl + "/all-my-group-messages/unread/count/" + user.getId() + "/0/", Long.class);
        int groupMessageCount = l.getBody().intValue();

        propertiesFileService.createMessagesMenuFile(msisdn, friendRequestCount, messageCount, joinRequestsCount, groupMessageCount, ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(3);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMessagesMenu(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 4
    public String getMyProfile(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        User user = ussdSession.getUser();
        propertiesFileService.createMyProfileFile2(msisdn, user, ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(4);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMyProfile(user, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 5
    public String getMyChomis(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(5);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/chomis/" + ussdSession.getUser().getId() + "/", User[].class);
        User[] users = obj.getBody();
        List<User> userList = null;
        try {
            userList = Arrays.asList(users);
        } catch (Exception ex) {
            System.err.println("Users is NULL");
        }
        propertiesFileService.createMyChomisList2(msisdn, userList, ussdSession.getUser().getLangCode().toLowerCase());
        if (userList != null) {
            int numberOfChomis = userList.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(5);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 6
    public String getSearchBy(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(6);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getSearchBy(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public void setMenuItems(ModelMap model, List<UssdMenu> list, String language) {
        String menu = "";
        String x = "";
        for (UssdMenu m : list) {
            String value = propertiesFileService.readProperties(m.getCode(), language);
            x = (m.getCommand().equalsIgnoreCase("8") || isBackMenuItem(value, language)) ? "0" : m.getCommand();
            if (value.equalsIgnoreCase(". Swati") || value.equalsIgnoreCase(". North West") || value.equalsIgnoreCase(". SiSwati") || value.equalsIgnoreCase(". Swazi")) {
                x = m.getCommand();
            }
            menu += x + "" + value + "\n";
        }
        model.addAttribute("menu", menu);
        model.addAttribute("pageHeader", propertiesFileService.readProperties(list.get(0).getPageHeader(), language));
    }

    public void setMenuItems(ModelMap model, List<UssdMenu> list, String msisdn, String language) {
        String menu = "";
        String x = "";
        for (UssdMenu m : list) {
            String value = propertiesFileService.readProperties(m.getCode(), msisdn, language);
            x = (m.getCommand().equalsIgnoreCase("8") || isBackMenuItem(value, language)) ? "0" : m.getCommand();
            if (qualifiesForNine(value, language)) {
                x = "9";
            }
            if (isNextMenuItem(value, language)) {//Next
                x = "7";
            }
            if (isPreviousMenuItem(value, language)) {//Previous
                x = "6";
            }
            if (isCreatePageMenuItem(value, language)) {//Create Page
                x = "8";
            }
            if (isMyPagesMenuItem(value, language)) {//My Pages
                x = "9";
            }
            if (isSearchMenuItem(value, language)) {//Search
                x = "10";
            }
            menu += x + "" + value + "\n";
        }
        model.addAttribute("menu", menu);
        model.addAttribute("pageHeader", list.get(0).getPageHeader());
    }

    public void setMenuItems(ModelMap model, List<UssdMenu> list, int count, String promo, String language) {
        String menu = "";
        String x = "";
        for (UssdMenu m : list) {
            if (m.getCode().equalsIgnoreCase(language + ".mainwallregistered.option3")) {
                x = m.getCommand().equalsIgnoreCase("8") ? "0" : m.getCommand();
                menu += x + "" + this.setMessagesCount(count, language) + "\n";
            } else if (m.getCode().equalsIgnoreCase(language + ".mainwallregistered.option8")) {
                x = m.getCommand();
                menu += x + "" + this.setMessagesCount(promo, language) + "\n";
            } else {
                menu += m.getCommand() + "" + propertiesFileService.readProperties(m.getCode(), language) + "\n";
            }
        }
        model.addAttribute("menu", menu);
        model.addAttribute("pageHeader", propertiesFileService.readProperties(list.get(0).getPageHeader(), language));
    }

    public void setMenuItems_qfix(ModelMap model, List<UssdMenu> list, String firstName, String language) {
        String menu = "";
        for (UssdMenu m : list) {
            menu += m.getCommand() + "" + propertiesFileService.readProperties(m.getCode(), language) + "\n";
        }
        String p = this.setFirstName_qfix(firstName, language);
        model.addAttribute("menu", menu);
        model.addAttribute("pageHeader", p);
    }

    public String getErrorHandler(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(0);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = null;
        try {
            list = ussdMenuService.getErrorHandler(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        } catch (Exception e) {
            list = ussdMenuService.getErrorHandler(ussdSession.getLanguage().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        }
        return INDEX;
    }

    //Level 11
    public String getTrendingList(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            int numberOfTrendings = 0;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TopicCount[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/hashtags/selecttop/20/", TopicCount[].class);
            TopicCount[] topicCount = obj.getBody();
            List<TopicCount> topicCountList = null;
            try {
                topicCountList = Arrays.asList(topicCount);
                numberOfTrendings = topicCountList.size();
                ussdSession.setTopicCount(topicCountList);
            } catch (Exception ex) {
                System.err.println("topicCountList is NULL");
            }
            int numberOfSets = (int) (numberOfTrendings / NEXT_TRENDING_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next trending
                ussdSession.setNextTrendingId(NEXT_TRENDING_ID_INCREMENT);
            } else {
                ussdSession.setNextTrendingId(NEXT_TRENDING_ID_INCREMENT);
                //There is no need to save the position of the next trending
            }
            ussdSession.setNavigationLevel(11);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            propertiesFileService.createTrendingsFile2(msisdn, topicCountList, false, ussdSession.getUser().getLangCode().toLowerCase());
            List<UssdMenu> list = ussdMenuService2.getTrendingList2(msisdn, topicCountList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message = "Sorry! Something went wrong. There are no more trending topics.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 13
    public String getTrendingSearch(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(13);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getTrendingSearch(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 21
    public String getPostsByMyChomis(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (!ussdSession.isAdvertServed2() && !ussdSession.isSubscribed()) {
            String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Wall&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
            Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
            return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 302);
        }
        try {
            List<com.ian.chomibridge.Post> posts = ussdSession.getMyPosts();
            propertiesFileService.createPostsByChomisFile(msisdn, posts, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = 0;
            try {
                numberOfPosts = ussdSession.getMyPosts().size();
            } catch (Exception ex) {
                System.err.println("getMyPosts is NULL");
            }
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            } else {
                //There is no need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(21);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 22
    public String getPostsByMe(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<Post> posts = postService.findAllPostsByPostedBy(personService.findPersonByMsisdn(msisdn).getId());
        propertiesFileService.createPostsByMeFile(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
        int numberOfPosts = posts.size();
        int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next post
            ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
        } else {
            //There is no need to save the position of the next post
        }
        ussdSession.setNavigationLevel(22);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMyPosts(posts, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 23
    public String getPostToTheWall(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(23);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPostToTheWall(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 31
    public String getConversations(ModelMap model, String msisdn, String request, int read) {
        if (this.hasNoUnreadMessages(msisdn, 0) && read == 0) {
            return this.getNoUnreadMessages(model, msisdn, request);
        }
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<com.ian.chomibridge.Conversation[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/myconversations/" + user.getId() + "/" + read + "/", com.ian.chomibridge.Conversation[].class);
            com.ian.chomibridge.Conversation[] convs = null;
            List<com.ian.chomibridge.Conversation> conversationList = null;
            int numberOfConversations = 0;
            try {
                convs = i.getBody();
                conversationList = Arrays.asList(convs);
                numberOfConversations = conversationList.size();
            } catch (Exception e) {
            }
            propertiesFileService.createConversationsList(msisdn, conversationList, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfSets = (int) (numberOfConversations / NEXT_MESSAGE_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextConversationId(NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setNextConversationId(NEXT_MESSAGE_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
            ussdSession.setNavigationLevel(31);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList3(msisdn, Arrays.asList(convs), ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            if (this.hasNoUnreadMessages(msisdn, 1)) {
                return this.getNoUnreadMessages(model, msisdn, request);
            }
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more conversations.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 32
    public String getFriendRequestsList(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(32);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        if (this.hasNoFriendRequests(msisdn)) {
            return this.getNoFriendRequests(model, msisdn, request);
        }
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
        ResponseEntity<RequestingFriend[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requesting/" + user.getId() + "/", RequestingFriend[].class);
        RequestingFriend[] requestingFriends = i.getBody();
        List<RequestingFriend> requestingFriendsList = Arrays.asList(requestingFriends);
        propertiesFileService.createFriendRequestsListMenuFile2(msisdn, requestingFriendsList, ussdSession.getUser().getLangCode().toLowerCase());
        List<UssdMenu> list = ussdMenuService2.getFriendRequestList2(requestingFriendsList, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 41
    public String getSetDOB(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(41);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getSetDOB(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getDOBUpdate(ModelMap model, String msisdn, String request) {
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            User user = ussdSession.getUser();
            try {
                user.setDob(fixdates(request));
            } catch (ParseException ex) {
                Logger.getLogger(NewController.class.getName()).log(Level.SEVERE, null, ex);
                return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong.");
            }
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/updatedob/", user);
            ussdSession.setNavigationLevel(41);
            ussdSession.setUser(user);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
        }
        return this.getMyProfile(model, msisdn, request);
    }

    //Level 42
    public String getSetGender(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(42);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getGenderPrompt(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getGenderUpdate(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            User user = ussdSession.getUser();
            user.setGenderId(Integer.valueOf(request));
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/updategender/", user);
            ussdSession.setUser(user);
            ussdSession.setNavigationLevel(42);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        return this.getMyProfile(model, msisdn, request);
    }

    //Level 43
    public String getSetProvince(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(43);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getProvincePrompt(ussdSession.getUser().getLangCode().toLowerCase());
        try {
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        } catch (Exception ex) {
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        }
        return INDEX;
    }

    public String getProvinceUpdate(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            User user = ussdSession.getUser();
            user.setProvinceId(Integer.valueOf(request));
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/updateprovince/", user);
            ussdSession.setUser(user);
            ussdSession.setNavigationLevel(43);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        return this.getMyProfile(model, msisdn, request);
    }

    //Level 44
    public String getSetLanguage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(44);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getLanguage(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getLanguageUpdate(ModelMap model, String msisdn, String request) {
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            User user = ussdSession.getUser();
            user.setLangCode(convertLanguage(request));
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/updatelanguage/", user);
            ussdSession.setNavigationLevel(4);
            ussdSession.setUser(user);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
        }
        return this.getMyProfile(model, msisdn, request);
    }

    //Level 51
    public String getMyChomisOneChomi(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            this.experiment(msisdn, request);
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            ussdSession.setNavigationLevel(51);
            ussdSession.setLastUpdateTime(new Date());
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<User> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/userid/" + Integer.valueOf(ussdSession.getSelectedChomi()) + "/", User.class);
            User user = obj.getBody();
            ussdSession.setSelectedUser(user);
            ussdSessionService.updateUssdSession(ussdSession);
            propertiesFileService.createMyChomisProfile(msisdn, user, ussdSession.getUser().getLangCode().toLowerCase());
            List<UssdMenu> list = ussdMenuService2.getMyChomisProfile(user, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            return "";
        }
    }

    public void experiment(String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextChomiId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/chomis/" + ussdSession.getUser().getId() + "/", User[].class);
        User[] users = obj.getBody();
        List<User> userList = null;
        try {
            userList = Arrays.asList(users);
        } catch (Exception e) {

        }
        if (Integer.valueOf(request) > 0 && isNotUserSessionAbortOrTimeOut(request)) {
            User selectedChomi = userList.subList(x - NEXT_CHOMI_ID_INCREMENT, userList.size()).get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedChomi(String.valueOf(selectedChomi.getId()));
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
        }
    }

    public void inbox_dec(String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (Integer.valueOf(request) > 0 && isNotUserSessionAbortOrTimeOut(request)) {
            int x = ussdSession.getNextMessageId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Mail[]> i = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-chomi/myuserid/" + ussdSession.getUser().getId() + "/chimiid/" + ussdSession.getSelectedChomi() + "/" + ussdSession.getRead() + "/", Mail[].class);
            Mail[] mails = i.getBody();
            List<Mail> mailsList = Arrays.asList(mails);
            List<Mail> mailsList1 = mailsList.subList(x - NEXT_MESSAGE_ID_INCREMENT, mailsList.size());
            Mail mail = mailsList1.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedConversationReplyId(String.valueOf(mail.getMailId()));
            ussdSession.setSelectedMessage(String.valueOf(mailsList1.get(Integer.valueOf(request) - 1).getMailId()));
            ussdSession.setSelectedChomi(String.valueOf(mail.getFromUserId()));
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
    }

    public void group_inbox_dec(String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (Integer.valueOf(request) > 0 && isNotUserSessionAbortOrTimeOut(request)) {
            int x = ussdSession.getNextMessageId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<Mail[]> i = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-chomi/myuserid/" + ussdSession.getUser().getId() + "/chimiid/" + ussdSession.getSelectedChomi() + "/" + ussdSession.getRead() + "/", Mail[].class);
            ResponseEntity<Mail[]> i = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-group/myuserid/" + ussdSession.getUser().getId() + "/groupid/" + ussdSession.getSelectedChomi() + "/0/", Mail[].class);
            Mail[] mails = i.getBody();
            List<Mail> mailsList = Arrays.asList(mails);
            List<Mail> mailsList1 = mailsList.subList(x - NEXT_MESSAGE_ID_INCREMENT, mailsList.size());
            Mail mail = mailsList1.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedConversationReplyId(String.valueOf(mail.getMailId()));
            ussdSession.setSelectedMessage(String.valueOf(mailsList1.get(Integer.valueOf(request) - 1).getMailId()));
            //ussdSession.setSelectedChomi(String.valueOf(mail.getFromUserId())); Removed intentionally
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
    }

    //Level 57
    public String getMyChomisNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextChomiId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/chomis/" + ussdSession.getUser().getId() + "/", User[].class);
            User[] users = obj.getBody();
            List<User> userList = Arrays.asList(users);
            List<User> userSublist = userList.subList(x, userList.size());
            propertiesFileService.createMyChomisList2(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfChomis = userSublist.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(57);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more chomis.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 37
    public String getConversationsNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextConversationId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<com.ian.chomibridge.Conversation[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/myconversations/" + user.getId() + "/" + ussdSession.getRead() + "/", com.ian.chomibridge.Conversation[].class);
            com.ian.chomibridge.Conversation[] convs = i.getBody();
            List<com.ian.chomibridge.Conversation> conversationList = Arrays.asList(convs);
            List<com.ian.chomibridge.Conversation> conversationSublist = conversationList.subList(x, conversationList.size());
            propertiesFileService.createConversationsList(msisdn, conversationList, true, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfConversations = conversationSublist.size();
            int numberOfSets = (int) (numberOfConversations / NEXT_MESSAGE_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                ussdSession.setNextConversationId(x + NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setNextConversationId(x + NEXT_MESSAGE_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(37);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList3(msisdn, conversationSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more conversations.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 311
    public String getInbox(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (ussdSession.getSelectedRequest() == null) {
            ussdSession.setSelectedRequest(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            int x = ussdSession.getNextConversationId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<com.ian.chomibridge.Conversation[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/myconversations/" + user.getId() + "/" + ussdSession.getRead() + "/", com.ian.chomibridge.Conversation[].class);
            com.ian.chomibridge.Conversation[] convs = i.getBody();
            List<com.ian.chomibridge.Conversation> conversationList = Arrays.asList(convs);
            List<com.ian.chomibridge.Conversation> conversationsSublist = conversationList.subList(x - NEXT_MESSAGE_ID_INCREMENT, conversationList.size());
            com.ian.chomibridge.Conversation conversation = conversationsSublist.get(Integer.valueOf(ussdSession.getSelectedRequest()) - 1);
            int userId = conversation.getUserId();
            ussdSession.setSelectedChomi(String.valueOf(userId));
            //CALL A SERVICE
            ResponseEntity<Mail[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-chomi/myuserid/" + ussdSession.getUser().getId() + "/chimiid/" + ussdSession.getSelectedChomi() + "/" + ussdSession.getRead() + "/", Mail[].class);
            Mail[] mails = obj.getBody();
            List<Mail> mailList = Arrays.asList(mails);
            if (mailList != null) {
                propertiesFileService.createConversationRepliesListFile(msisdn, mailList, false, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = mailList.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextMessageId(NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setNextMessageId(NEXT_MESSAGE_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(311);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList4(msisdn, mailList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more messages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 317
    public String getInboxNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextMessageId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Mail[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-chomi/myuserid/" + ussdSession.getUser().getId() + "/chimiid/" + ussdSession.getSelectedChomi() + "/" + ussdSession.getRead() + "/", Mail[].class);
            Mail[] mails = obj.getBody();
            List<Mail> mailList = Arrays.asList(mails);
            List<Mail> mailSublist = mailList.subList(x, mailList.size());
            if (mailList != null) {
                propertiesFileService.createConversationRepliesListFile(msisdn, mailSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = mailSublist.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextMessageId(x + NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setNextMessageId(x + NEXT_MESSAGE_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(317);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList4(msisdn, mailSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more messages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 3111
    public String getInboxOne(ModelMap model, String msisdn, String request) {
        this.inbox_dec(msisdn, request);
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Mail> obj = restTemplate.getForEntity(serviceBaseUrl + "/message/" + ussdSession.getSelectedMessage() + "/", Mail.class);
            Mail mail = obj.getBody();
            propertiesFileService.createDetailedMessageFile2(msisdn, mail, ussdSession.getRead(), ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setMail(mail);
            ussdSession.setNavigationLevel(3111);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getDetailedMessages(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            restTemplate.getForObject(serviceBaseUrl + "/message/read/" + ussdSession.getMail().getMailId() + "/", Void.class);//Set the message as read
            return INDEX;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    //Level 31111
    public String getMessageReply(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Mail> obj = restTemplate.getForEntity(serviceBaseUrl + "/message/" + ussdSession.getSelectedMessage() + "/", Mail.class);
        Mail mail = obj.getBody();
        //Check Friendship
        ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/checkfriendship/" + mail.getToUserId() + "/" + mail.getFromUserId() + "/", Boolean.class);
        boolean b = i.getBody();
        System.out.println("---------------------------------------------------------" + i.getBody());
        if (!b) {
            model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.canonlychatwithchomi.headerText"));
            ussdSession.setNavigationLevel(31112);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);//To be revised
            return INDEX;
        } else {
            propertiesFileService.createMessageReplyFile2(msisdn, mail, ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setNavigationLevel(31111);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessageReply(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        }
        return INDEX;
    }

    //Level 31110
    public String getMessageDelete(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(31110);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        if (ussdSession.getUser().getId() != ussdSession.getMail().getFromUserId()) {
            model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youcanonlydelete.headerText"));
            return INDEX;
        } else {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject(serviceBaseUrl + "/deletemessage/" + ussdSession.getMail().getMailId() + "/", Void.class);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. The message could not be deleted.");
            }
            List<UssdMenu> list = ussdMenuService.getMessageDelete(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        }
    }

    //Level 311111
    public String getMessageReplySuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            Mail mail = new Mail();
            mail.setContentId(0);
            mail.setDate(new Date());
            mail.setFromUserDel(0);
            mail.setFromUserId(ussdSession.getUser().getId());
            mail.setMessage(fixSingleQuotes(request));
            mail.setOriginalMailId(0);
            mail.setRead(0);
            mail.setSubject("From USSD");
            mail.setToUserDel(0);
            mail.setToUserId(Integer.valueOf(ussdSession.getSelectedChomi()));
            mail.setTypeId(0);
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/messages/sendmessage/", mail);
        }
        ussdSession.setNavigationLevel(3111111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getMessageReplySuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getPostToTheWallSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {//If the user did not cancel in the middle of the posting action
            com.ian.chomibridge.Post post = new com.ian.chomibridge.Post();
            post.setDateCreated(new Date());
            post.setMessage(fixSingleQuotes(request));
            post.setUserId(ussdSession.getUser().getId());
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/posts/postmessage/", post);
        }
        ussdSession.setNavigationLevel(231);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPostToTheWallSuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    //Level 211
    public String getPostsByChomisViewComment(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            int x = ussdSession.getNextPostId();
            List<com.ian.chomibridge.Post> posts1 = ussdSession.getMyPosts();
            List<com.ian.chomibridge.Post> posts = posts1.subList(x - NEXT_POST_ID_INCREMENT, posts1.size());
            String postId = String.valueOf(posts.get(Integer.valueOf(request) - 1).getMessageId());
            ussdSession.setPostId(postId);
            ussdSession.setNavigationLevel(211);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getPostOrViewComment(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            return "";
        }
    }

    public String getPostsByChomisNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextPostId();
            List<com.ian.chomibridge.Post> posts = ussdSession.getMyPosts();
            List<com.ian.chomibridge.Post> postsSublist = posts.subList(x, posts.size());
            propertiesFileService.createPostsByChomisFile(msisdn, postsSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = postsSublist.size();
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                System.out.println("You need to save the position of the next post");
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
            } else {
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
                System.out.println("There is no need to save the position of the next post");
            }
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 2111
    public String getPostComment(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(2111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPostComment(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getCommentsForPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<com.ian.chomibridge.Comment[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/one/comments/" + ussdSession.getPostId() + "/", com.ian.chomibridge.Comment[].class);
        com.ian.chomibridge.Comment[] comms = i.getBody();
        List<com.ian.chomibridge.Comment> comments = Arrays.asList(comms);
        ResponseEntity<com.ian.chomibridge.Post> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/one/" + ussdSession.getPostId() + "/", com.ian.chomibridge.Post.class);
        com.ian.chomibridge.Post post = obj.getBody();
        if (comments != null) {
            int numberOfComments = comments.size();
            int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
            } else {
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(2112);
        ussdSession.setComments(comments);
        ussdSession.setSelectedPost(post);
        ussdSessionService.updateUssdSession(ussdSession);
        propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, comments, true, ussdSession.getUser().getLangCode().toLowerCase(), false);
        List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(comments, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getNextCommentsForPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            ussdSession.setLastUpdateTime(new Date());
            int x = ussdSession.getNextCommentId();
            List<com.ian.chomibridge.Comment> comments = ussdSession.getComments();
            com.ian.chomibridge.Post post = ussdSession.getSelectedPost();
            List<com.ian.chomibridge.Comment> commentsSublist = comments.subList(x, comments.size());
            if (commentsSublist != null) {
                propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, commentsSublist, true, ussdSession.getUser().getLangCode().toLowerCase(), true);
                int numberOfComments = commentsSublist.size();
                int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                } else {
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(2112);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(commentsSublist, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more comments.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    public String getNextCommentsForTrendingPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            ussdSession.setLastUpdateTime(new Date());
            int x = ussdSession.getNextCommentId();
            List<com.ian.chomibridge.Comment> comments = ussdSession.getComments();
            com.ian.chomibridge.Post post = ussdSession.getSelectedPost();
            List<com.ian.chomibridge.Comment> commentsSublist = comments.subList(x, comments.size());
            if (commentsSublist != null) {
                propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, commentsSublist, false, ussdSession.getUser().getLangCode().toLowerCase(), true);
                int numberOfComments = commentsSublist.size();
                int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                } else {
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(2113);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(commentsSublist, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more comments.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    //Level 211
    public String getPostOrViewComment(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            int x = ussdSession.getNextPostId();
            List<Post> posts1 = postService.findAllPostsByPostedBy(personService.findPersonByMsisdn(msisdn).getId());
            List<Post> posts = posts1.subList(x - NEXT_POST_ID_INCREMENT, posts1.size());
            String postId = posts.get(Integer.valueOf(request) - 1).getId();
            ussdSession.setPostId(postId);
            ussdSession.setNavigationLevel(211);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getPostOrViewComment(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            return "";
        }
    }

    public String getPostsByMeNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextPostId();
        List<Post> posts = postService.findAllPostsByPostedBy(personService.findPersonByMsisdn(msisdn).getId());
        if ((x + NEXT_POST_ID_INCREMENT - 1) <= posts.subList(x, posts.size()).size()) {
            //I would use 4 but since I figured out that 4=NEXT_POST_ID_INCREMENT-1 then I should use this constant. It is probably a formula that I have developed. 
            propertiesFileService.createPostsByMeFile(msisdn, posts.subList(x, posts.size()), ussdSession.getUser().getLangCode().toLowerCase());
        } else {
            propertiesFileService.createPostsByMeFile(msisdn, posts.subList(x, posts.size()), ussdSession.getUser().getLangCode().toLowerCase());
        }
        int numberOfPosts = posts.subList(x, posts.size()).size();
        int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
        if (numberOfSets > 0) {
            System.out.println("You need to save the position of the next post");
            ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
        } else {
            ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
            System.out.println("There is no need to save the position of the next post");
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(227);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMyPosts(posts, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getPostCommentResult(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            com.ian.chomibridge.Comment comment = new Comment();
            comment.setComment(fixSingleQuotes(request));
            comment.setCommentDate(new Date());
            comment.setMessageId(Integer.valueOf(ussdSession.getPostId()));
            comment.setProcessed(0);
            comment.setType(0);
            comment.setUserId(ussdSession.getUser().getId());
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/comments/postcomment/", comment);
            List<UssdMenu> list = ussdMenuService.getPostResults(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            List<UssdMenu> list = ussdMenuService.getErrorHandler(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        }
    }

    public String getSubscribe(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(911);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getSubscribe(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    public String getSubscribeConfirm(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(9111);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getSubscribeConfirm(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    public String getTermsAndConditons(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getTermsAndConditions(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    public String getSuccessful(ModelMap model, String msisdn, String request) {
        if (!request.equals("0")) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setNavigationLevel(91111);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getSuccessful(ussdSession.getLanguage().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject("http://" + host + ":8080/vodacom_billing_rest/subscriber/purchase/" + msisdn, ErResponse.class);
        } else {
            this.getSubscribe(model, msisdn, request);
        }
        return INDEX;
    }

    public String getAgePrompt(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setUsername(request);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(911111);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getAgePrompt(ussdSession.getLanguage().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        return INDEX;
    }

    public String getGenderPrompt(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            ussdSession.setAge(Integer.parseInt(request));
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setNavigationLevel(9111111);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getGenderPrompt(ussdSession.getLanguage().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        }
        return INDEX;
    }

    public String getProvincePrompt(ModelMap model, String msisdn, String request) {
        if (!request.equals("0")) {
            String gender = request;
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            ussdSession.setGender(gender);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setNavigationLevel(91111111);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getProvincePrompt(ussdSession.getLanguage().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
            return INDEX;
        } else {
            return this.getGenderPrompt(model, msisdn, request);
        }
    }

    public String getMainWall(ModelMap model, String msisdn, String request) {
        if (!request.equals("0") && isNotUserSessionAbortOrTimeOut(request)) {
            String province = request;
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            ussdSession.setNavigationLevel(0);
            ussdSession.setProvince(province);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            try {
                UserReg user = new UserReg();
                user.setCompleteStatusId(0);
                user.setCountryID(1);
                user.setDateCreated(new Date());
                user.setDateOfBirth(new Date());
                user.setFirstName(ussdSession.getUsername());
                user.setGenderId(Integer.valueOf(ussdSession.getGender()));
                user.setLangCode(ussdSession.getLanguage());
                user.setLastName("");
                user.setMobileNumber(msisdn);
                user.setNotificationRegistered(0);
                user.setNotifySetting1("Immediately");
                user.setNotifySetting2("Immediately");
                user.setNotifySetting3("Immediately");
                user.setNotifySetting5("Immediately");
                user.setUsername(ussdSession.getUsername());
                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForLocation(serviceBaseUrl + "/person/createuserprofile/", user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return this.getNewChomiNotification(model, msisdn, request);
        } else if (request.equals("0")) {
            return this.getGenderPrompt(model, msisdn, request);
        } else {
            return "";
        }
    }

    public String getTrendingOne(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (ussdSession.getSelectedRequest2() == null) {
            ussdSession.setSelectedRequest2(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            int x = ussdSession.getNextTrendingId();
            List<TopicCount> hashTags = ussdSession.getTopicCount();
            List<TopicCount> subHashTagsList = hashTags.subList(x - NEXT_TRENDING_ID_INCREMENT, hashTags.size());
            String topic = subHashTagsList.get(Integer.valueOf(ussdSession.getSelectedRequest2()) - 1).getTopic();
            ussdSession.setTopicSearchTitle(topic);
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/trending/posts/" + topic.substring(1) + "/", com.ian.chomibridge.Post[].class);
            ussdSession.setTopicSearchTitle(topic.substring(1));
            com.ian.chomibridge.Post posts[] = i.getBody();
            List<com.ian.chomibridge.Post> postsList = Arrays.asList(posts);
            if (postsList != null) {
                propertiesFileService.createPostsListInTrendingTopicFile(msisdn, postsList, false, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = postsList.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    ussdSession.setTrendingOneNextId(NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setTrendingOneNextId(NEXT_MESSAGE_ID_INCREMENT);
                }
            }
            ussdSession.setTrendingPosts(postsList);
            ussdSession.setNavigationLevel(1171);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getTrendingOne(msisdn, postsList, ussdSession.getUser().getLangCode().toLowerCase());
            model.addAttribute("pageHeader", list.get(0).getPageHeader());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    public String getTrendingListNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextTrendingId();
            List<TopicCount> trendings = ussdSession.getTopicCount();
            if ((x + NEXT_TRENDING_ID_INCREMENT - 1) <= trendings.subList(x, trendings.size()).size()) {
                //I would use 4 but since I figured out that 4=NEXT_POST_ID_INCREMENT-1 then I should use this constant. It is probably a formula that I have developed. 
                propertiesFileService.createTrendingsFile2(msisdn, trendings.subList(x, trendings.size()), true, ussdSession.getUser().getLangCode().toLowerCase());
            } else {
                propertiesFileService.createTrendingsFile2(msisdn, trendings.subList(x, trendings.size()), true, ussdSession.getUser().getLangCode().toLowerCase());
            }
            int numberOfPosts = trendings.subList(x, trendings.size()).size();
            int numberOfSets = (int) (numberOfPosts / NEXT_TRENDING_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                ussdSession.setNextTrendingId(x + NEXT_TRENDING_ID_INCREMENT);
            } else {
                ussdSession.setNextTrendingId(x + NEXT_TRENDING_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(117);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getTrendingList2(msisdn, trendings, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more topics.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    public String getTrendingOnePost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (ussdSession.getSelectedRequest3() == null) {
            ussdSession.setSelectedRequest3(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        int x = ussdSession.getTrendingOneNextId() - NEXT_MESSAGE_ID_INCREMENT;
        List<com.ian.chomibridge.Post> postsList = ussdSession.getTrendingPosts();
        List<com.ian.chomibridge.Post> postsListSublist = postsList.subList(x, postsList.size());
        com.ian.chomibridge.Post post = postsListSublist.get(Integer.valueOf(ussdSession.getSelectedRequest3()) - 1);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<com.ian.chomibridge.Comment[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/one/comments/" + post.getMessageId() + "/", com.ian.chomibridge.Comment[].class);
        com.ian.chomibridge.Comment[] comms = i.getBody();
        List<com.ian.chomibridge.Comment> comments = Arrays.asList(comms);
        if (comments != null) {
            int numberOfComments = comments.size();
            int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
            } else {
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(2113);
        ussdSession.setComments(comments);
        ussdSession.setSelectedPost(post);
        ussdSessionService.updateUssdSession(ussdSession);
        propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, comments, false, ussdSession.getUser().getLangCode().toLowerCase(), false);
        List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(comments, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getNickNameSearch(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(611);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getNicknameSearch(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getFullNameSearch(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(612);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getFullnameSearch(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getCellphoneSearch(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(613);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getCellphoneSearch(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getSearchResults(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int navigationLevel = ussdSession.getNavigationLevel();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i;
        switch (navigationLevel) {
            case 611:
                i = restTemplate.getForEntity(serviceBaseUrl + "/person/username/" + request + "/", User[].class);
                break;
            case 612:
                i = restTemplate.getForEntity(serviceBaseUrl + "/person/fullname/search/" + request + "/", User[].class);
                break;
            case 613:
                i = restTemplate.getForEntity(serviceBaseUrl + "/person/msisdn/search/" + request + "/", User[].class);
                break;
            default:
                i = null;
        }
        User[] usrs = null;
        List<User> users = null;
        try {
            usrs = i.getBody();
            users = Arrays.asList(usrs);
        } catch (Exception e) {
        }
        propertiesFileService.createMyChomisSearchFile3(msisdn, users, ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(6131);
        ussdSession.setSearchName(request);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setSearchedUsers(users);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getTestMenu2(users, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getPersonSendRequest(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (ussdSession.getNavigationLevel() != 7011313) {
            ussdSession.setNavigationLevel(61311);
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        User selectedUser = ussdSession.getSelectedUser();
        int friendId = selectedUser.getId();
        int userId = ussdSession.getUser().getId();
        Friend friend = new Friend();
        friend.setCreated(new Date());
        friend.setFriendId(friendId);
        friend.setModified(new Date());
        friend.setStatusId(1); //StatusId 1 is for request 
        friend.setUserId(userId);
        if (userId != friendId) {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/chomiids/" + ussdSession.getUser().getId() + "/", String[].class);
            String[] convs = i.getBody();
            List<String> strList = null;
            try {
                strList = Arrays.asList(convs);
            } catch (Exception ex) {
                System.err.println("strList is NULL");
                strList = new ArrayList<>();
            }
            if (strList.contains(String.valueOf(friendId))) {
                model.addAttribute("pageHeader", setDynamicValue(selectedUser.getFirstName(), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.chomisalready.headerText"));
                return INDEX;
            }

            //Check If Friendship Request has already been sent
            ResponseEntity<Boolean> j = restTemplate.getForEntity(serviceBaseUrl + "/checkiffriendshipsent/" + ussdSession.getUser().getId() + "/" + friendId + "/", Boolean.class);
            if (j.getBody()) {
                model.addAttribute("pageHeader", setDynamicValue(selectedUser.getFirstName(), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.requestsentalreadytouser.headerText"));
                return INDEX;
            }

            NotificationList notification = new NotificationList();
            notification.setUserID(friendId);
            notification.setContentID(userId);
            notification.setCreated(new Date());
            notification.setModified(new Date());
            notification.setNotificationText(ussdSession.getUser().getFirstName() + " " + ussdSession.getUser().getLastName() + " sent you a friend request");
            notification.setNotificationType(2);
            notification.setSenderID(userId);
            notification.setiIsNotificationRead(0);
            //CALL A SERVICE
            restTemplate.postForLocation(serviceBaseUrl + "/person/chomis/sendrequest/", friend);//Send Chomi Request
            restTemplate.postForLocation(serviceBaseUrl + "/person/createchomirequestnotification/", notification);//Send Chomi Request Notification
        } else {
            List<UssdMenu> list = ussdMenuService.getFriendRequestFailure(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        }
        List<UssdMenu> list = ussdMenuService.getFriendRequestSuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getChomiSendMessage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(511);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getChomiSendMessage(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getChomiRemove(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(512);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        User user = ussdSession.getSelectedUser();
        propertiesFileService.getChomiRemove(msisdn, user, ussdSession.getUser().getLangCode().toLowerCase());
        List<UssdMenu> list = ussdMenuService2.getChomiRemove(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getChomiRemoveYes(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(5121);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        User user = ussdSession.getSelectedUser();
        propertiesFileService.createChomiRemoveYes(msisdn, user, ussdSession.getUser().getLangCode().toLowerCase());
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serviceBaseUrl + "/person/removechomi/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedUser().getId() + "/", Void.class);
        } catch (Exception ex) {
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong.");
        }
        List<UssdMenu> list = ussdMenuService2.getChomiRemoveYes(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getChomiSendMessageSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(5111);
        ussdSessionService.updateUssdSession(ussdSession);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            Mail mail = new Mail();
            mail.setContentId(0);
            mail.setDate(new Date());
            mail.setFromUserDel(0);
            mail.setFromUserId(ussdSession.getUser().getId());
            mail.setMessage(fixSingleQuotes(request));
            mail.setOriginalMailId(0);
            mail.setRead(0);
            mail.setSubject("From USSD");
            mail.setToUserDel(0);
            mail.setToUserId(Integer.valueOf(ussdSession.getSelectedChomi()));
            mail.setTypeId(0);
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/messages/sendmessage/", mail);
        }
        List<UssdMenu> list = ussdMenuService.getChomiSendMessageSuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getFriendRequestsPerson(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            try {
                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
                ResponseEntity<RequestingFriend[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requesting/" + user.getId() + "/", RequestingFriend[].class);
                RequestingFriend[] requestingFriends = i.getBody();
                List<RequestingFriend> requestingFriendsList = Arrays.asList(requestingFriends);
                RequestingFriend requestingFriend = requestingFriendsList.get(Integer.valueOf(request) - 1);
                propertiesFileService.createRequestingPersonProfile(msisdn, requestingFriend, ussdSession.getUser().getLangCode().toLowerCase());
                ussdSession.setRequestingFriend(requestingFriend);
                ussdSession.setNavigationLevel(321);
                ussdSession.setRequestingPersonId(String.valueOf(requestingFriend.getUserId()));
                ussdSessionService.updateUssdSession(ussdSession);
            } catch (Exception ex) {
                return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong.");
            }
            List<UssdMenu> list = ussdMenuService2.getRequestingPersonProfile(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            return "";
        }
    }

    public String getFriendRequestAccept(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(3211);
        ussdSessionService.updateUssdSession(ussdSession);
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            RequestingFriend requestingFriend = ussdSession.getRequestingFriend();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/friends/requests/accept/", requestingFriend);
            propertiesFileService.createFriendRequestAccept(msisdn, requestingFriend, ussdSession.getUser().getLangCode().toLowerCase());
        }
        List<UssdMenu> list = ussdMenuService2.getFriendRequestAccept(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getFriendRequestPersonDecline(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(3211);
        ussdSessionService.updateUssdSession(ussdSession);
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            RequestingFriend requestingFriend = ussdSession.getRequestingFriend();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serviceBaseUrl + "/person/declinechomi/" + ussdSession.getUser().getId() + "/" + requestingFriend.getUserId() + "/", Void.class);
        }
        List<UssdMenu> list = ussdMenuService.getFriendRequestDecline(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getNoUnreadMessages(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(300);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getNoUnreadMessages(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getNoFriendRequests(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<UssdMenu> list = ussdMenuService.getNoFriendRequests(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getWelcomeNote(ModelMap model, final String msisdn, String request) {

        final RestTemplate restTemplate = new RestTemplate();
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int navigationLevel = ussdSession.getNavigationLevel();

        //ussdSession.setSubscribed(true);//Done to remove free AD version
        ussdSessionService.updateUssdSession(ussdSession);//Done to remove free AD version

        //0. If coming from the purchase page, invoke purchase API
        if (navigationLevel == 1800) {
            try {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        try {
                            ErResponse erResponse = restTemplate.getForObject("http://" + host + ":8080/vodacom_billing_rest/subscriber/purchase/" + msisdn, ErResponse.class);
                            Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                            StringWriter sw = new StringWriter();
                            jaxbMarshaller.marshal(erResponse, sw);
                            String xmlString = sw.toString();
                            Document d = stringToDom(xmlString);
                            String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                            cache.remove(msisdn);
                            cache.put(msisdn, erResponse);
                            if (reasonCode.equalsIgnoreCase("OK")) {
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                        1000
                );
                //Introduce a 5 second delay
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (cache.get(msisdn) == null) {
                    ussdSession.setNavigationLevel(1802);
                    ussdSessionService.updateUssdSession(ussdSession);
                    model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.processingpurchase.headerText"));
                    return INDEX;
                } else {
                    ErResponse erResponse = cache.get(msisdn);
                    Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                    StringWriter sw = new StringWriter();
                    jaxbMarshaller.marshal(erResponse, sw);
                    String xmlString = sw.toString();
                    Document d = stringToDom(xmlString);
                    String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");

                    if (reasonCode.equalsIgnoreCase("NO VALID PACKAGE") || reasonCode.contains("FAILED SUSPENDED")) {
                        System.out.println("---------------------------------AD-Funded USSD Path--------------------------------------------");
                        ussdSession.setSubscribed(false);//ussdSession.setSubscribed(true);
                        ussdSessionService.updateUssdSession(ussdSession);

                        ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                        return this.index(model, msisdn, request);

                        //model.addAttribute("pageHeader", "Chomi subscription request received. Make sure you have sufficient funds to complete the purchase.");//Done to remove free AD version
                        //return GOODBYE;//Done to remove free AD version
                    }
                    if (reasonCode.contains("RENEWAL NEEDED")) {
                        System.out.println("----------------------------------------Renewal needed---------------------------------------------------");
                        final String subId = XmlResponseProcessor.readElementParameter(d, "package-subscription-id", "usage-authorisation");
                        try {
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here
                                    try {
                                        ErResponse erResponse = restTemplate.getForObject("http://" + host + ":8080/vodacom_billing_rest/subscriber/renew/" + msisdn + "/" + subId, ErResponse.class);
                                        Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                                        StringWriter sw = new StringWriter();
                                        jaxbMarshaller.marshal(erResponse, sw);
                                        String xmlString = sw.toString();
                                        Document d = stringToDom(xmlString);
                                        String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                                        cache.remove(msisdn);
                                        cache.put(msisdn, erResponse);
                                        if (reasonCode.equalsIgnoreCase("OK")) {
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            },
                                    1000
                            );
                            //Introduce a 5 second delay
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //****************
                            erResponse = cache.get(msisdn);
                            jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                            sw = new StringWriter();
                            jaxbMarshaller.marshal(erResponse, sw);
                            xmlString = sw.toString();
                            d = stringToDom(xmlString);
                            reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                            //****************
                            if (reasonCode.contains("PAYMENT FAILED")) {
                                //model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.insufficientfunds.headerText"));
                                //ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                                //return GOODBYE;
                                return this.getAdFundedNotification(model, msisdn, request);
                            }
                            //****************
                        } catch (Exception e) {
                        }
                    }
                    //****************
                    if (reasonCode.contains("PAYMENT FAILED")) {
                        //model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.insufficientfunds.headerText"));
                        //ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                        //return GOODBYE;                        
                        return this.getAdFundedNotification(model, msisdn, request);
                    }
                    //****************
                }
            } catch (Exception e) {
            }
        }
        if (navigationLevel == 1802) {
            if (cache.get(msisdn) == null) {
                ussdSession.setNavigationLevel(1802);
                ussdSessionService.updateUssdSession(ussdSession);
                model.addAttribute("pageHeader", "We're still processing your purchase. Please press 1 to continue.\n1. Continue.\n2. Cancel");
                return INDEX;
            }
        }

        //1. [Check if the session-stored response code is null or not null]        
        if (ussdSession.getSubResponse() != null) {
            //	1.1 If the session-stored response code is RENEWAL NEEDED, invoke renewal API
            final String subId = ussdSession.getSubResponse().getSubscriptionId();
            if (ussdSession.getSubResponse().getResponseCode().equalsIgnoreCase("RENEWAL NEEDED")) {
                System.out.println("----------------------------------------Renewal needed---------------------------------------------------");
                try {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                            try {
                                ErResponse erResponse = restTemplate.getForObject("http://" + host + ":8080/vodacom_billing_rest/subscriber/renew/" + msisdn + "/" + subId, ErResponse.class);
                                Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                                StringWriter sw = new StringWriter();
                                jaxbMarshaller.marshal(erResponse, sw);
                                String xmlString = sw.toString();
                                Document d = stringToDom(xmlString);
                                String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                                cache.remove(msisdn);
                                cache.put(msisdn, erResponse);
                                if (reasonCode.equalsIgnoreCase("OK")) {
                                }
                            } catch (Exception e) {
                            }
                        }
                    },
                            1000
                    );
                    model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.thankyouforsubscription.headerText"));
                    ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                    return GOODBYE;
                } catch (Exception e) {
                }
            }
        } else {
            //1.2 If the session-stored response code is NULL, proceed
        }

        //2. Run the credit check once if NL is 100.
        if (ussdSession.getMsisdn().equalsIgnoreCase("277160467570")) {//To avoid my number from being billed.
            ussdSession.setNavigationLevel(101);
            ussdSession.setSubscribed(true);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService.getWelcomeNote(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        }
        if (navigationLevel == 100) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                @Override
                public void run() {
                    // your code here
                    try {
                        ErResponse erResponse = restTemplate.getForObject("http://" + host + ":8080/vodacom_billing_rest/subscriber/creditcheck/" + msisdn, ErResponse.class);
                        Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                        StringWriter sw = new StringWriter();
                        jaxbMarshaller.marshal(erResponse, sw);
                        String xmlString = sw.toString();
                        Document d = stringToDom(xmlString);
                        /*String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                        String subscriptionId = XmlResponseProcessor.readElementParameter(d, "usage-authorisation", "package-subscription-id");
                        SubResponse subResponse = new SubResponse();
                        subResponse.setResponseCode(reasonCode);
                        subResponse.setSubscriptionId(subscriptionId);*/
                        cache.remove(msisdn);
                        cache.put(msisdn, erResponse);
                    } catch (Exception e) {

                    }
                }
            },
                    1000
            );
        }
        //3. If the response object is null, then it means the credit check is still going on.
        //Introduce a 5 second delay
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (cache.get(msisdn) == null) {
            ussdSession.setNavigationLevel(1801);
            ussdSessionService.updateUssdSession(ussdSession);
            model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.checkingsubscription.headerText"));
            return INDEX;
        } else {
            //4. If the response object is not null, it means the credit check is complete. So check the response code
            //   4.2 If response code is NO VALID PACKAGE, send to purchase page with NL 1800 and save the response code and subscriptionId.
            try {
                Marshaller jaxbMarshaller = JAXBContext.newInstance(ErResponse.class).createMarshaller();
                StringWriter sw = new StringWriter();
                jaxbMarshaller.marshal(cache.get(msisdn), sw);
                String xmlString = sw.toString();
                Document d = stringToDom(xmlString);
                String reasonCode = XmlResponseProcessor.readElementParameter(d, "name", "reason-code");
                String subscriptionId = null;//XmlResponseProcessor.readElementParameter(d, "usage-authorisation", "package-subscription-id");
                SubResponse subResponse = new SubResponse();
                subResponse.setResponseCode(reasonCode);
                subResponse.setSubscriptionId(subscriptionId);
                if (reasonCode.equalsIgnoreCase("NO VALID PACKAGE") && navigationLevel != 1800) {
                    subscriptionId = XmlResponseProcessor.readElementParameter(d, "usage-authorisation", "package-subscription-id");
                    List<UssdMenu> list = ussdMenuService.getMsisdNotSubscribed(ussdSession.getUser().getLangCode().toLowerCase());
                    this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());

                    subResponse.setResponseCode(reasonCode);
                    subResponse.setSubscriptionId(subscriptionId);
                    ussdSession.setSubResponse(subResponse);
                    ussdSession.setNavigationLevel(1800);
                    ussdSessionService.updateUssdSession(ussdSession);
                    return INDEX;
                }
                if (reasonCode.contains("FAILED SUSPENDED")) {
                    subscriptionId = XmlResponseProcessor.readElementParameter(d, "package-subscription-id", "usage-authorisation");
                    List<UssdMenu> list = ussdMenuService.getMsisdNotSubscribed(ussdSession.getUser().getLangCode().toLowerCase());
                    this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());

                    subResponse.setResponseCode(reasonCode);
                    subResponse.setSubscriptionId(subscriptionId);
                    ussdSession.setSubResponse(subResponse);
                    ussdSession.setNavigationLevel(1800);
                    ussdSessionService.updateUssdSession(ussdSession);
                    return INDEX;
                }
                if (reasonCode.equalsIgnoreCase("OK")) {
                    ussdSession.setSubscribed(true);
                    ussdSessionService.updateUssdSession(ussdSession);
                }

            } catch (Exception e) {
            }

        }
        //4.1 If response code is NO VALID PACKAGE, send to purchase page with NL 1800 and save the response code and subscriptionId.
        System.out.println("-------------------------------------------------------" + ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(101);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getWelcomeNote(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getGoodByeNote(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<UssdMenu> list = null;
        try {
            list = ussdMenuService.getGoodByeNote(ussdSession.getUser().getLangCode().toLowerCase());
        } catch (Exception e) {
            list = ussdMenuService.getGoodByeNote("english".toLowerCase());
        }
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
        return GOODBYE;
    }

    public String getCommentOne(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            try {
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setNavigationLevel(21121);
                int x = ussdSession.getNextCommentId();
                List<com.ian.chomibridge.Comment> comments = ussdSession.getComments().subList(x - NEXT_MESSAGE_ID_INCREMENT, ussdSession.getComments().size());
                com.ian.chomibridge.Comment comment = comments.get(Integer.valueOf(request) - 1);
                propertiesFileService.createCommentOne(msisdn, comment, ussdSession.getUser().getLangCode().toLowerCase());
                ussdSessionService.updateUssdSession(ussdSession);
                List<UssdMenu> list = ussdMenuService2.getCommentOne(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                return INDEX;
            } catch (Exception ex) {
                String message = "Sorry! Something went wrong. You selected a non-existing option.";
                return this.getSomethingWentWrong(model, ussdSession, true, message);
            }
        } else {
            return "";
        }
    }

    public void defaultLevel(UssdSession ussdSession) {
        ussdSession.setNavigationLevel(0);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
    }

    public String setMessagesCount(int count, String language) {
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = MessageFormat.format((String) props.get(language + ".mainwallregistered.option3"), count);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    public String setMessagesCount(String promo, String language) {
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = MessageFormat.format((String) props.get(language + ".mainwallregistered.option8"), promo);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    public String setFirstName_qfix(String firstName, String language) {
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = MessageFormat.format((String) props.get(language + ".friendblock.headerText"), firstName);
            System.out.println("*******************************************************************-------------------------------------------------------------- " + p);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }

    private String getTrendingOneNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getTrendingOneNextId();
            List<com.ian.chomibridge.Post> postsList = ussdSession.getTrendingPosts();
            List<com.ian.chomibridge.Post> postsListSublist = postsList.subList(x, postsList.size());
            if (postsListSublist != null) {
                propertiesFileService.createPostsListInTrendingTopicFile(msisdn, postsListSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = postsListSublist.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    ussdSession.setTrendingOneNextId(x + NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setTrendingOneNextId(x + NEXT_MESSAGE_ID_INCREMENT);
                }
            }
            ussdSession.setNavigationLevel(1171);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getTrendingOne(msisdn, postsListSublist, ussdSession.getUser().getLangCode().toLowerCase());
            model.addAttribute("pageHeader", list.get(0).getPageHeader());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more trending posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    public String getSearchedProfile(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            try {
                List<User> searchedUsers = ussdSessionService.findUssdSessionByMsisdn(msisdn).getSearchedUsers();
                User selectedUser = searchedUsers.get(Integer.valueOf(request) - 1);
                propertiesFileService.createSearchedProfile(msisdn, selectedUser, ussdSession.getUser().getLangCode().toLowerCase());
                ussdSession.setNavigationLevel(6111);
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setSelectedUser(selectedUser);
                ussdSessionService.updateUssdSession(ussdSession);
                List<UssdMenu> list = ussdMenuService2.getSearchedProfile(selectedUser, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                return INDEX;
            } catch (Exception ex) {
                String message = "Sorry! Something went wrong.";
                if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                    message += " There is no profile found.";
                }
                return this.getSomethingWentWrong(model, ussdSession, true, message);
            }
        } else {
            return "";
        }
    }

    boolean hasNoUnreadMessages(String msisdn, int read) {
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
        ResponseEntity<Long> i = restTemplate.getForEntity(serviceBaseUrl + "/all-my-messages/unread/count/" + user.getId() + "/" + read + "/", Long.class);
        int messageCount = i.getBody().intValue();
        return messageCount <= 0;
    }

    boolean hasNoGroupUnreadMessages(String msisdn, int read) {
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
        ResponseEntity<Long> i = restTemplate.getForEntity(serviceBaseUrl + "/all-my-group-messages/unread/count/" + user.getId() + "/" + read + "/", Long.class);
        int messageCount = i.getBody().intValue();
        return messageCount <= 0;
    }

    boolean hasNoFriendRequests(String msisdn) {
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
        ResponseEntity<Long> j = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requested/" + user.getId() + "/", Long.class);
        int friendRequestCount = j.getBody().intValue();
        return friendRequestCount <= 0;
    }

    public String getSomethingWentWrong(ModelMap model, UssdSession ussdSession, boolean setDefault, String message) {
        ussdSessionService.deleteUssdSessionByMsisdn(ussdSession.getMsisdn());
        model.addAttribute("pageHeader", message);
        return GOODBYE;
    }

    private String getBlockFriendRequest(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(3211);
        ussdSessionService.updateUssdSession(ussdSession);
        RequestingFriend requestingFriend = null;
        List<UssdMenu> list = ussdMenuService.getFriendRequestDecline(ussdSession.getUser().getLangCode().toLowerCase());
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            requestingFriend = ussdSession.getRequestingFriend();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serviceBaseUrl + "/person/blockchomi/" + ussdSession.getUser().getId() + "/" + requestingFriend.getUserId() + "/", Void.class);
            this.setMenuItems_qfix(model, list, requestingFriend.getFirstName(), request);
            return INDEX;
        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. Your session timed out");
        }

    }

    private String getTrendingSearchResults(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/trending/posts/" + request + "/", com.ian.chomibridge.Post[].class);
        List<com.ian.chomibridge.Post> postsList = null;
        try {
            com.ian.chomibridge.Post posts[] = i.getBody();
            postsList = Arrays.asList(posts);
        } catch (Exception e) {
        }
        propertiesFileService.createPostsListInTrendingTopicFile(msisdn, postsList, false, ussdSession.getUser().getLangCode().toLowerCase());//investigate
        if (postsList != null) {
            int numberOfMessages = postsList.size();
            int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
            if (numberOfSets > 0) {
                ussdSession.setTrendingOneNextId(NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setTrendingOneNextId(NEXT_MESSAGE_ID_INCREMENT);
            }
        }
        ussdSession.setTrendingPosts(postsList);
        ussdSession.setNavigationLevel(1171);
        //Fix
        if (postsList == null) {
            ussdSession.setNavigationLevel(1170);
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getTrendingOne(msisdn, postsList, ussdSession.getUser().getLangCode().toLowerCase());
        model.addAttribute("pageHeader", list.get(0).getPageHeader());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public static String fixSingleQuotes(String value) {
        return value.replace("'", "''");
    }

    public static Date fixdates(String strDate) throws ParseException {
        StringBuilder str = new StringBuilder(strDate);
        //insert character value at offset 4 and 7
        str.insert(4, '/');
        str.insert(7, '/');
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(str.toString());
        return date;
    }

    private String getCommentOneTrendingPost(ModelMap model, String msisdn, String request) {
        if (isNotUserSessionAbortOrTimeOut(request)) {
            UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
            try {
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setNavigationLevel(211211);
                int x = ussdSession.getNextCommentId();
                List<com.ian.chomibridge.Comment> comments = ussdSession.getComments().subList(x - NEXT_MESSAGE_ID_INCREMENT, ussdSession.getComments().size());
                com.ian.chomibridge.Comment comment = comments.get(Integer.valueOf(request) - 1);
                propertiesFileService.createCommentOne(msisdn, comment, ussdSession.getUser().getLangCode().toLowerCase());
                ussdSessionService.updateUssdSession(ussdSession);
                List<UssdMenu> list = ussdMenuService2.getCommentOne(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                return INDEX;
            } catch (Exception e) {
                return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. You selected an option that is non-existant.");
            }
        } else {
            return "";
        }
    }

    public String getPostTopicToTheWall(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(1179);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPostToTheWall(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getPostTopicToTheWallSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {//If the user did not cancel in the middle of the posting action
            com.ian.chomibridge.Post post = new com.ian.chomibridge.Post();
            post.setDateCreated(new Date());
            post.setMessage("#" + ussdSession.getTopicSearchTitle() + " " + fixSingleQuotes(request));
            post.setUserId(ussdSession.getUser().getId());
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(serviceBaseUrl + "/person/posts/postmessage/", post);
        }
        ussdSession.setNavigationLevel(11719);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPostToTheWallSuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    private String getBlockPerson(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(61110);
        ussdSessionService.updateUssdSession(ussdSession);
        if (!request.equalsIgnoreCase(MOUserAbort)) {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serviceBaseUrl + "/person/blockchomi/" + ussdSession.getUser().getId() + "/" + ussdSession.getSelectedUser().getId() + "/", Void.class);
        }
        List<UssdMenu> list = ussdMenuService.getFriendBlock(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems_qfix(model, list, ussdSession.getSelectedUser().getFirstName(), ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    public String getAdvert(ModelMap model, String msisdn, String request, String advert, int navigationLevel) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(navigationLevel);
        ussdSession.setLastUpdateTime(new Date());
        switch (navigationLevel) {
            case 301:
                ussdSession.setAdvertServed(true);
                break;
            case 302:
                ussdSession.setAdvertServed2(true);
                break;
            case 30:
                ussdSession.setAdvertServed3(true);
                break;
            case 303:
                ussdSession.setAdvertServed5(true);
                break;
            default:
                ussdSession.setAdvertServed4(true);
        }
        ussdSessionService.updateUssdSession(ussdSession);
        model.addAttribute("pageHeader", advert + "\n1. Continue\n0. Cancel");
        return INDEX;
    }

    public String getNewChomiNotification(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(901);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setAdvertServed(true);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = null;
        try {
            list = ussdMenuService.getNewChomiNotification(ussdSession.getLanguage().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getLanguage().toLowerCase());
        } catch (Exception e) {
            list = ussdMenuService.getNewChomiNotification("english".toLowerCase());
            this.setMenuItems(model, list, "english".toLowerCase());
        }
        return INDEX;
    }

    public String getAdFundedNotification(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(3000);
        ussdSessionService.updateUssdSession(ussdSession);
        model.addAttribute("pageHeader", "You have insufficient funds. Continue to the Advertsing-Funded version?\n1. Continue\n0. Back");
        return INDEX;
    }

    public String getMainWallAfterNewChomiNotification(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //Registration Fix=====================================================================================================================================
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/msisdn/" + msisdn + "/", User.class);
        User user = obj.getBody();
        ussdSession.setUser(user);

        ResponseEntity<Long> i = restTemplate.getForEntity(serviceBaseUrl + "/all-my-messages/unread/count/" + user.getId() + "/0/", Long.class);
        int messageCount = i.getBody().intValue();
        ResponseEntity<Long> j = restTemplate.getForEntity(serviceBaseUrl + "/person/friends/requested/" + user.getId() + "/", Long.class);
        int friendRequestCount = j.getBody().intValue();
        ResponseEntity<Long> k = restTemplate.getForEntity(serviceBaseUrl + "/group/myjoinrequestscount/" + user.getId() + "/", Long.class);
        int joinRequestsCount = k.getBody().intValue();
        int count = messageCount + friendRequestCount + joinRequestsCount;
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setNavigationLevel(0);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getMainWallRegistered(user.getLangCode().toLowerCase(), "");
        this.setMenuItems(model, list, count, advertService.doCompleteAction("http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Menu&responseType=ussd&xmlwrapped=false&clientref=" + user.getMsisdn(), user).getDescription(), user.getLangCode().toLowerCase());
        //Registration Fix=====================================================================================================================================
        return INDEX;
    }

    //Groups Addition
    String getPages(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (!ussdSession.isAdvertServed5() && !ussdSession.isSubscribed()) {
            String url = "http://service.marbiladserver.co.za/services/adrequest.ashx?portalkey=a2429b38-998f-4e30-823f-50476c0b0df1&tag=Chomi_USSD_Menu&responseType=ussd&xmlwrapped=false&clientref=" + ussdSession.getUser().getMsisdn();
            Advert ad = advertService.doCompleteAction(url, ussdSession.getUser());
            return this.getAdvert(model, msisdn, request, ad.getMedia() + "\n", 303);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/allgroups/", Group[].class);
            com.ian.chomibridge.Group[] groups = i.getBody();
            List<Group> groupsList = null;
            int numberOfGroups = 0;
            try {
                groupsList = Arrays.asList(groups);
                numberOfGroups = groupsList.size();
            } catch (Exception e) {
            }
            propertiesFileService.createPagesMenu(msisdn, groupsList, false, ussdSession.getUser().getLangCode().toLowerCase());

            int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
            ussdSession.setNavigationLevel(7);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setGroupsSublist(groupsList);
            ussdSessionService.updateUssdSession(ussdSession);

            List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message = "Sorry! Something went wrong. There are no more pages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }

    }

    String getPagesNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextPagesId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/allgroups/", Group[].class);
        com.ian.chomibridge.Group[] groups = i.getBody();
        List<Group> groupsList = Arrays.asList(groups);
        List<Group> groupsSubList = groupsList.subList(x, groupsList.size());
        propertiesFileService.createPagesMenu(msisdn, groupsSubList, true, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfGroups = groupsSubList.size();
        int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(7);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setGroupsSublist(groupsSubList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<Group> groups = ussdSession.getGroupsSublist();
        Group selectedGroup = null;
        if (request.equalsIgnoreCase("0")) {
            selectedGroup = groups.get(Integer.valueOf(ussdSession.getSelectedRequest4()) - 1);
        } else {
            selectedGroup = groups.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedRequest4(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Integer> j = restTemplate.getForEntity(serviceBaseUrl + "/group/membercount/" + selectedGroup.getId() + "/", Integer.class);
            ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Long> l = restTemplate.getForEntity(serviceBaseUrl + "/group/myjoinrequestscount/" + ussdSession.getUser().getId() + "/", Long.class);
            if (k.getBody() && selectedGroup.getPublicGroup() == 1) {
                propertiesFileService.createMyViewPublicPageMenu(msisdn, selectedGroup, i.getBody(), k.getBody(), ussdSession.getUser().getLangCode().toLowerCase());
                ussdSession.setNavigationLevel(7011);
            } else if (k.getBody() && selectedGroup.getPublicGroup() == 0) {
                propertiesFileService.createMyViewPageMenu(msisdn, selectedGroup, i.getBody(), k.getBody(), l.getBody().intValue(), ussdSession.getUser().getLangCode().toLowerCase());
                ussdSession.setNavigationLevel(7012);
            } else {
                propertiesFileService.createViewPageMenu(msisdn, selectedGroup, i.getBody(), ussdSession.getUser().getLangCode().toLowerCase(), j.getBody());
                ussdSession.setNavigationLevel(71);
            }
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setSelectedGroup(selectedGroup);
            ussdSession.setGroupName(selectedGroup.getName());
            ussdSession.setGroupOwner(k.getBody());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getViewPageMenu(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            ex.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    String getFollow(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        String message = null;
        GroupUser gu = new GroupUser();
        gu.setGroupId(ussdSession.getSelectedGroup().getId());
        gu.setUserId(ussdSession.getUser().getId());
        gu.setCreated(new Date());
        gu.setHasApplied(1);
        if (ussdSession.getSelectedGroup().getPublicGroup() == 1) {
            //message = "You are now a follower of " + replaceAmpersand(ussdSession.getSelectedGroup().getName(), false) + "\n0. Back";
            message = setDynamicValue(replaceAmpersand(ussdSession.getSelectedGroup().getName(), false), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youarenowafollowerof.headerText");
            gu.setFollowing(1);
            gu.setIsApproved(1);
        } else {
            //message = "You have requested to join " + replaceAmpersand(ussdSession.getSelectedGroup().getName(), false) + "\n0. Back";
            message = setDynamicValue(replaceAmpersand(ussdSession.getSelectedGroup().getName(), false), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaverequestedtojoin.headerText");
            gu.setFollowing(0);
            gu.setIsApproved(0);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(serviceBaseUrl + "/group/follow/", gu, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in the service call");
        }
        model.addAttribute("pageHeader", message);
        return INDEX;
    }

    String getUnFollow(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serviceBaseUrl + "/group/unfollow/" + ussdSession.getSelectedGroup().getId() + "/" + ussdSession.getUser().getId() + "/", Void.class);
            model.addAttribute("pageHeader", setDynamicValue(replaceAmpersand(ussdSession.getSelectedGroup().getName(), false), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youarenolongerafollower.headerText"));
        } catch (Exception e) {
            e.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in the service call");
        }
        return INDEX;
    }

    String getViewGroupMembers(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupmembers/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = i.getBody();
        List<User> usersList = Arrays.asList(users);

        propertiesFileService.createGroupMembers(msisdn, usersList, false, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfUsers = usersList.size();
        int numberOfSets = (int) (numberOfUsers / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(712);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(usersList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.createGroupMembersMenu(msisdn, usersList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewGroupMembersNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextGroupMemberId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupmembers/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = i.getBody();
        List<User> usersList = Arrays.asList(users);
        List<User> usersSubList = usersList.subList(x, usersList.size());
        propertiesFileService.createGroupMembers(msisdn, usersSubList, true, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfGroups = usersSubList.size();
        int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextGroupMemberId(x + NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextGroupMemberId(x + NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(712);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(usersSubList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.createGroupMembersMenu(msisdn, usersSubList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getPageCreate(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(78);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPageCreate(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getPageCreateDescription(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            ussdSession.setGroupName(request);
            ussdSessionService.updateUssdSession(ussdSession);

            //Enforce Page Rules
            //1. Ensure the page name does not start with # character
            if (startWithCharacter(ussdSession.getGroupName(), '#')) {
                return this.getSomethingWentWrong(model, ussdSession, true, "Pages may not start with a hashtag character.");
            }
            //2. Ensure the page name has at least 2 characters
            if (!hasSufficientLength(ussdSession.getGroupName(), 2)) {
                return this.getSomethingWentWrong(model, ussdSession, true, "A page must have at least 2 characters.");
            }
            //3. Ensure the page does not exist already
            if (isPageExisting(ussdSession.getGroupName())) {
                return this.getSomethingWentWrong(model, ussdSession, true, "A page with name " + ussdSession.getGroupName() + " already exists");
            }

        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in capturing Page Name");
        }
        ussdSession.setNavigationLevel(781);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPageCreateDescription(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getPageCreateType(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            ussdSession.setGroupDescription(request);
        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in capturing Page Description");
        }
        ussdSession.setNavigationLevel(7811);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPageCreateType(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    private String getCanMembersCanPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request) && (request.equals("1") || request.equals("2"))) {
            ussdSession.setPageType(Integer.valueOf(request));
        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in capturing Page Type");
        }
        ussdSession.setNavigationLevel(78111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        String str = ussdSession.getPageType() == 1
                ? getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.followers.headerText")
                : getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.members.headerText");
        model.addAttribute("pageHeader", setDynamicValue(str, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.canpostonmywall.headerText"));
        return INDEX;
    }

    String getPageCreateSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request) && (request.equals("1") || request.equals("2"))) {
            ussdSession.setCanPost(Integer.valueOf(request));
            ussdSessionService.updateUssdSession(ussdSession);
        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in capturing Page Type");
        }
        Date date = new Date();
        Group g = new Group();
        g.setOwnerUserId(ussdSession.getUser().getId());
        g.setCreated(date);
        g.setModified(date);
        g.setName(ussdSession.getGroupName());
        g.setDescription(ussdSession.getGroupDescription());
        g.setImagePath("images/user_no_group_pic_menu.png");
        g.setIsStarted(1);
        g.setCategoryId(1);
        if (ussdSession.getPageType() == 1) {
            g.setPublicGroup(1);
        }
        if (ussdSession.getPageType() == 2) {
            g.setPublicGroup(0);
        }
        if (ussdSession.getCanPost() == 1) {
            g.setPublicCanPost(1);
        }
        if (ussdSession.getCanPost() == 2) {
            g.setPublicCanPost(0);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(serviceBaseUrl + "/group/creategroup/", g, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "Error in capturing the service call");
        }
        ussdSession.setSelectedGroup(g);
        ussdSession.setNavigationLevel(781111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getPageCreateSuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewCreatedPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        Group selectedGroup = ussdSession.getSelectedGroup();
        String groupName = selectedGroup.getName();
        int IsPublicGroup = selectedGroup.getPublicGroup();
        String typeOfGroup = (IsPublicGroup == 1) ? "Public Page" : "Private Page";
        model.addAttribute("pageHeader", groupName + ", " + selectedGroup.getDescription() + ", " + typeOfGroup + "\n0. " + getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.back.option"));
        return INDEX;
    }

    String getMyPages(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/mygroups/" + ussdSession.getUser().getId() + "/", Group[].class);
            com.ian.chomibridge.Group[] groups = i.getBody();
            List<Group> groupsList = null;
            int numberOfGroups = 0;
            try {
                groupsList = Arrays.asList(groups);
                numberOfGroups = groupsList.size();
            } catch (Exception e) {
            }
            propertiesFileService.createMyPagesMenu(msisdn, groupsList, false, ussdSession.getUser().getLangCode().toLowerCase());

            int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
            ussdSession.setNavigationLevel(701);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setGroupsSublist(groupsList);
            ussdSessionService.updateUssdSession(ussdSession);

            List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message = "Sorry! Something went wrong. There are no more pages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }

    }

    String getMyPagesNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextPagesId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/mygroups/" + ussdSession.getUser().getId() + "/", Group[].class);
        com.ian.chomibridge.Group[] groups = i.getBody();
        List<Group> groupsList = Arrays.asList(groups);
        List<Group> groupsSubList = groupsList.subList(x, groupsList.size());
        propertiesFileService.createMyPagesMenu(msisdn, groupsSubList, true, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfGroups = groupsSubList.size();
        int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(701);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setGroupsSublist(groupsSubList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewGroupPosts(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);

        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase() + "/", com.ian.chomibridge.Post[].class);
            com.ian.chomibridge.Post[] groupPosts = i.getBody();
            List<com.ian.chomibridge.Post> posts = null;
            try {
                posts = Arrays.asList(groupPosts);
            } catch (Exception e) {
            }
            propertiesFileService.createGroupPostsFile(msisdn, posts, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = 0;
            try {
                numberOfPosts = posts.size();
            } catch (Exception ex) {
                System.err.println("getMyPosts is NULL");
            }
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            } else {
                //There is no need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(713);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getViewGroupPostsNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase() + "/", com.ian.chomibridge.Post[].class);
            com.ian.chomibridge.Post[] groupPosts = i.getBody();

            int x = ussdSession.getNextPostId();
            List<com.ian.chomibridge.Post> posts = null;

            try {
                posts = Arrays.asList(groupPosts);
            } catch (Exception e) {
            }

            List<com.ian.chomibridge.Post> postsSublist = posts.subList(x, posts.size());
            propertiesFileService.createGroupPostsFile(msisdn, postsSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = postsSublist.size();
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                System.out.println("You need to save the position of the next post");
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
            } else {
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
                System.out.println("There is no need to save the position of the next post");
            }
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getCommentsForGroupPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setLastUpdateTime(new Date());
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<com.ian.chomibridge.Comment[]> i = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/one/comments/" + ussdSession.getPostId() + "/", com.ian.chomibridge.Comment[].class);
        com.ian.chomibridge.Comment[] comms = i.getBody();
        List<com.ian.chomibridge.Comment> comments = Arrays.asList(comms);
        ResponseEntity<com.ian.chomibridge.Post> obj = restTemplate.getForEntity(serviceBaseUrl + "/person/posts/one/" + ussdSession.getPostId() + "/", com.ian.chomibridge.Post.class);
        com.ian.chomibridge.Post post = obj.getBody();
        if (comments != null) {
            int numberOfComments = comments.size();
            int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
            } else {
                ussdSession.setNextCommentId(NEXT_COMMENT_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(7131);
        ussdSession.setComments(comments);
        ussdSession.setSelectedPost(post);
        ussdSessionService.updateUssdSession(ussdSession);
        propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, comments, true, ussdSession.getUser().getLangCode().toLowerCase(), false);
        List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(comments, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getCommentsForGroupPostNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            ussdSession.setLastUpdateTime(new Date());
            int x = ussdSession.getNextCommentId();
            List<com.ian.chomibridge.Comment> comments = ussdSession.getComments();
            com.ian.chomibridge.Post post = ussdSession.getSelectedPost();
            List<com.ian.chomibridge.Comment> commentsSublist = comments.subList(x, comments.size());
            if (commentsSublist != null) {
                propertiesFileService.createCommentsForPostFile2(post.getMessage(), msisdn, commentsSublist, true, ussdSession.getUser().getLangCode().toLowerCase(), true);
                int numberOfComments = commentsSublist.size();
                int numberOfSets = (int) (numberOfComments / NEXT_COMMENT_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                } else {
                    ussdSession.setNextCommentId(x + NEXT_COMMENT_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(7131);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getCommentsForPost2(commentsSublist, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more comments.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getCommentOne_(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setNavigationLevel(71311);
            int x = ussdSession.getNextCommentId();
            List<com.ian.chomibridge.Comment> comments = ussdSession.getComments().subList(x - NEXT_MESSAGE_ID_INCREMENT, ussdSession.getComments().size());
            com.ian.chomibridge.Comment comment = comments.get(Integer.valueOf(request) - 1);
            propertiesFileService.createCommentOne(msisdn, comment, ussdSession.getUser().getLangCode().toLowerCase());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getCommentOne(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong. You selected a non-existing option.";
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getViewMyPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<Group> groups = ussdSession.getGroupsSublist();
        Group selectedGroup = null;
        if (request.equalsIgnoreCase("0")) {
            selectedGroup = groups.get(Integer.valueOf(ussdSession.getSelectedRequest4()) - 1);
        } else {
            selectedGroup = groups.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedRequest4(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            propertiesFileService.createMyViewPublicPageMenu(msisdn, selectedGroup, i.getBody(), k.getBody(), ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setNavigationLevel(7011);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setSelectedGroup(selectedGroup);
            ussdSession.setGroupName(selectedGroup.getName());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getViewPageMenu(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    String getAddPublicGroupPost(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(70111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.typeyourpost.headerText"));
        return INDEX;
    }

    String getMyViewPublicGroupPosts(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);

        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase() + "/", com.ian.chomibridge.Post[].class);
            com.ian.chomibridge.Post[] groupPosts = i.getBody();
            List<com.ian.chomibridge.Post> posts = null;
            try {
                posts = Arrays.asList(groupPosts);
            } catch (Exception e) {
            }
            propertiesFileService.createGroupPostsFile(msisdn, posts, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = 0;
            try {
                numberOfPosts = posts.size();
            } catch (Exception ex) {
                System.err.println("getMyPosts is NULL");
            }
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            } else {
                //There is no need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(70112);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getMyViewPublicGroupPostsNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase(), com.ian.chomibridge.Post[].class);
            com.ian.chomibridge.Post[] groupPosts = i.getBody();

            int x = ussdSession.getNextPostId();
            List<com.ian.chomibridge.Post> posts = null;

            try {
                posts = Arrays.asList(groupPosts);
            } catch (Exception e) {
            }

            List<com.ian.chomibridge.Post> postsSublist = posts.subList(x, posts.size());
            propertiesFileService.createGroupPostsFile(msisdn, postsSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = postsSublist.size();
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                System.out.println("You need to save the position of the next post");
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
            } else {
                ussdSession.setNextPostId(x + NEXT_POST_ID_INCREMENT);
                System.out.println("There is no need to save the position of the next post");
            }
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getSendGroupMessage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(70123);//To be fixed Has bee fixed
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getSendGroupMessage(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    private String getSendGroupMessageSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            try {
                GroupChat mail = new GroupChat();
                mail.setGroupId(ussdSession.getSelectedGroup().getId());
                mail.setUserId(ussdSession.getUser().getId());
                mail.setSubject("From USSD");
                mail.setMessage(fixSingleQuotes(request));
                mail.setDate(new Date());
                mail.setRead(0);
                mail.setTypeId(0);
                mail.setOriginalMailId(0);
                mail.setContentId(0);
                mail.setToUserDel(0);
                mail.setFromUserDel(0);

                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForLocation(serviceBaseUrl + "/sendgroupmessage/", mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ussdSession.setNavigationLevel(3111111);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService.getMessageReplySuccess(ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getJoinRequests(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //ussdSession.setNavigationLevel(70124);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/joinrequests/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = obj.getBody();
        List<User> userList = null;
        try {
            userList = Arrays.asList(users);
        } catch (Exception ex) {
            System.err.println("Users is NULL");
        }
        propertiesFileService.createPageRequestUsers(msisdn, userList, ussdSession.getSelectedGroup().getName(), ussdSession.getUser().getLangCode().toLowerCase());
        if (userList != null) {
            int numberOfChomis = userList.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(70124);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(userList);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewMyPublicGroupMembers(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupmembers/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = i.getBody();
        List<User> usersList = null;
        int numberOfUsers = 0;
        try {
            usersList = Arrays.asList(users);
            numberOfUsers = usersList.size();
        } catch (Exception e) {
        }

        propertiesFileService.createGroupMembers(msisdn, usersList, false, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfSets = (int) (numberOfUsers / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(70113);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(usersList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.createGroupMembersMenu(msisdn, usersList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewMyPublicGroupMembersNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextGroupMemberId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupmembers/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = i.getBody();
        List<User> usersList = null;
        List<User> usersSubList = null;
        try {
            usersList = Arrays.asList(users);
            usersSubList = usersList.subList(x, usersList.size());
        } catch (Exception e) {
        }
        propertiesFileService.createGroupMembers(msisdn, usersSubList, true, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfGroups = usersSubList.size();
        int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextGroupMemberId(x + NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextGroupMemberId(x + NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(70113);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(usersSubList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.createGroupMembersMenu(msisdn, usersSubList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getDeletePage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        Group selectedGroup = ussdSession.getSelectedGroup();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
        propertiesFileService.createDeleteMyPublicPageMenu(msisdn, selectedGroup, k.getBody(), ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(70114);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getDeletePage(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewGroupMember(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(701131);
        ussdSession.setLastUpdateTime(new Date());
        List<User> usersList = ussdSession.getUsersSublist();
        User selectedUser = null;
        try {
            selectedUser = usersList.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedUser(selectedUser);
        } catch (Exception e) {
            selectedUser = ussdSession.getSelectedUser();
        }
        ussdSessionService.updateUssdSession(ussdSession);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        Group selectedGroup = ussdSession.getSelectedGroup();
        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
        propertiesFileService.createViewGroupMember(msisdn, selectedUser, k.getBody(), ussdSession.getUser().getLangCode().toLowerCase());
        List<UssdMenu> list = ussdMenuService2.createViewGroupMember(msisdn, selectedUser, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getViewMyPrivatePage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<Group> groups = ussdSession.getGroupsSublist();
        Group selectedGroup = null;
        if (request.equalsIgnoreCase("0")) {
            selectedGroup = groups.get(Integer.valueOf(ussdSession.getSelectedRequest4()) - 1);
        } else {
            selectedGroup = groups.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedRequest4(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Long> l = restTemplate.getForEntity(serviceBaseUrl + "/group/myjoinrequestscount/" + ussdSession.getUser().getId() + "/", Long.class);
            propertiesFileService.createMyViewPageMenu(msisdn, selectedGroup, i.getBody(), k.getBody(), l.getBody().intValue(), ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setNavigationLevel(7012);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setSelectedGroup(selectedGroup);
            ussdSession.setGroupName(selectedGroup.getName());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getViewPageMenu(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    String getAddPrivateGroupPost(ModelMap model, String msisdn, String request) {
        return getAddPublicGroupPost(model, msisdn, request);
    }

    String getMyViewPrivateGroupPosts(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);

        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/", com.ian.chomibridge.Post[].class);
            ResponseEntity<com.ian.chomibridge.Post[]> i = restTemplate.getForEntity(serviceBaseUrl + "/group/posts/" + ussdSession.getSelectedGroup().getId() + "/language/" + ussdSession.getUser().getLangCode().toLowerCase(), com.ian.chomibridge.Post[].class);
            com.ian.chomibridge.Post[] groupPosts = i.getBody();
            List<com.ian.chomibridge.Post> posts = null;
            try {
                posts = Arrays.asList(groupPosts);
            } catch (Exception e) {
            }
            propertiesFileService.createGroupPostsFile(msisdn, posts, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfPosts = 0;
            try {
                numberOfPosts = posts.size();
            } catch (Exception ex) {
                System.err.println("getMyPosts is NULL");
            }
            int numberOfSets = (int) (numberOfPosts / NEXT_POST_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            } else {
                //There is no need to save the position of the next post
                ussdSession.setNextPostId(NEXT_POST_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(70112);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPostsByChomisFile2(msisdn, posts, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more posts.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    String getViewMyPrivateGroupMembers(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupmembers/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
        User[] users = i.getBody();
        List<User> usersList = null;
        int numberOfUsers = 0;
        try {
            usersList = Arrays.asList(users);
            numberOfUsers = usersList.size();
        } catch (Exception e) {
        }
        propertiesFileService.createGroupMembers(msisdn, usersList, false, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfSets = (int) (numberOfUsers / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextGroupMemberId(NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(70113);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setUsersSublist(usersList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.createGroupMembersMenu(msisdn, usersList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getDeletePrivatePage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        Group selectedGroup = ussdSession.getSelectedGroup();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
        propertiesFileService.createDeleteMyPublicPageMenu(msisdn, selectedGroup, k.getBody(), ussdSession.getUser().getLangCode().toLowerCase());
        ussdSession.setNavigationLevel(70126);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getDeletePage(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getJoinRequestsNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextChomiId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/joinrequests/" + ussdSession.getSelectedGroup().getId() + "/", User[].class);
            User[] users = obj.getBody();
            List<User> userList = Arrays.asList(users);
            List<User> userSublist = userList.subList(x, userList.size());
            propertiesFileService.createPageRequestUsers(msisdn, userList, ussdSession.getSelectedGroup().getName(), ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfChomis = userSublist.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);//I would divide it by 5, but since I noticed that it is the same as NEXT_POST_ID_INCREMENT, so I use this constant.
            if (numberOfSets > 0) {
                ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(701247);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setUsersSublist(userSublist);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more chomis.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }

    }

    public static void main(String[] args) {
        System.out.println(isNotUserSessionAbortOrTimeOut("*117*24664#"));
        //System.out.println(false && false);
    }

    private String getAddPublicGroupPostSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            if (isNotUserSessionAbortOrTimeOut(request)) {//If the user did not cancel in the middle of the posting action
                com.ian.chomibridge.Post post = new com.ian.chomibridge.Post();
                post.setDateCreated(new Date());
                post.setMessage(fixSingleQuotes(request));
                post.setUserId(ussdSession.getUser().getId());
                post.setGroupID(ussdSession.getSelectedGroup().getId());
                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForLocation(serviceBaseUrl + "/person/posts/postmessage/", post);
            }
            model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.successfullyposted.headerText"));
            return INDEX;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
        }
    }

    private String getSearchForPages(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(710);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.enterpagesearch.headerText"));
        return INDEX;
    }

    private String getSearchForPagesResults(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            try {
                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupsearch/" + request + "/", Group[].class);
                com.ian.chomibridge.Group[] groups = i.getBody();
                List<Group> groupsList = null;
                int numberOfGroups = 0;
                try {
                    groupsList = Arrays.asList(groups);
                    numberOfGroups = groupsList.size();
                } catch (Exception e) {
                }
                propertiesFileService.createSeachPagesMenu(msisdn, groupsList, false, ussdSession.getUser().getLangCode().toLowerCase());

                int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next conversation
                    ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setNextPagesId(NEXT_MESSAGE_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
                ussdSession.setNavigationLevel(7101);
                ussdSession.setGroupSearchName(request);
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setGroupsSublist(groupsList);
                ussdSessionService.updateUssdSession(ussdSession);

                List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
                this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                return INDEX;
            } catch (Exception ex) {
                String message = "Sorry! Something went wrong.";
                if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                    message = "Sorry! Something went wrong. There are no more pages.";
                }
                return this.getSomethingWentWrong(model, ussdSession, true, message);
            }
        } else {
            return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
        }
    }

    String getSearchForPagesNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int x = ussdSession.getNextPagesId();
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupsearch/" + ussdSession.getGroupSearchName() + "/", Group[].class);
        com.ian.chomibridge.Group[] groups = i.getBody();
        List<Group> groupsList = Arrays.asList(groups);
        List<Group> groupsSubList = groupsList.subList(x, groupsList.size());
        propertiesFileService.createSeachPagesMenu(msisdn, groupsSubList, true, ussdSession.getUser().getLangCode().toLowerCase());

        int numberOfGroups = groupsSubList.size();
        int numberOfSets = (int) (numberOfGroups / NEXT_MESSAGE_ID_INCREMENT);
        if (numberOfSets > 0) {
            //You need to save the position of the next conversation
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
        } else {
            ussdSession.setNextPagesId(x + NEXT_MESSAGE_ID_INCREMENT);
            //There is no need to save the position of the next message
        }
        ussdSession.setNavigationLevel(7101);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setGroupsSublist(groupsSubList);
        ussdSessionService.updateUssdSession(ussdSession);

        List<UssdMenu> list = ussdMenuService2.getPagesMenu(msisdn, groupsList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    private String getViewSearchedPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        List<Group> groups = ussdSession.getGroupsSublist();
        Group selectedGroup = null;
        if (request.equalsIgnoreCase("0")) {
            selectedGroup = groups.get(Integer.valueOf(ussdSession.getSelectedRequest4()) - 1);
        } else {
            selectedGroup = groups.get(Integer.valueOf(request) - 1);
            ussdSession.setSelectedRequest4(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> i = restTemplate.getForEntity(serviceBaseUrl + "/usergroupfollowing/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            ResponseEntity<Integer> j = restTemplate.getForEntity(serviceBaseUrl + "/group/membercount/" + selectedGroup.getId() + "/", Integer.class);
            propertiesFileService.createViewSearchedPageMenu(msisdn, selectedGroup, i.getBody(), ussdSession.getUser().getLangCode().toLowerCase(), j.getBody());
            ussdSession.setNavigationLevel(71011);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setSelectedGroup(selectedGroup);
            ussdSession.setGroupName(selectedGroup.getName());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getViewPageMenu(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    private String getPageJoinRequests(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GroupRequestCounter[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/allmyjoinrequestscount/" + ussdSession.getUser().getId() + "/", GroupRequestCounter[].class);
        GroupRequestCounter[] groupRequestCounters = obj.getBody();
        List<GroupRequestCounter> groupRequestCountersList = null;
        try {
            groupRequestCountersList = Arrays.asList(groupRequestCounters);
        } catch (Exception ex) {
        }
        propertiesFileService.createPageJoinRequests(msisdn, groupRequestCountersList, ussdSession.getUser().getLangCode().toLowerCase());
        if (groupRequestCountersList != null) {
            int numberOfChomis = groupRequestCountersList.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
        }
        ussdSession.setNavigationLevel(33);
        ussdSession.setLastUpdateTime(new Date());
        ussdSession.setGroupRequestCounters(groupRequestCountersList);
        ussdSessionService.updateUssdSession(ussdSession);
        List<UssdMenu> list = ussdMenuService2.getPageJoinRequests(msisdn, groupRequestCountersList, ussdSession.getUser().getLangCode().toLowerCase());
        this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
        return INDEX;
    }

    String getPageJoinRequestsNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextGroupRequestCounterId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GroupRequestCounter[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/allmyjoinrequestscount/" + ussdSession.getUser().getId() + "/", GroupRequestCounter[].class);
            GroupRequestCounter[] groupRequestCounters = obj.getBody();
            List<GroupRequestCounter> groupRequestCountersList = null;
            List<GroupRequestCounter> userSublist = null;
            try {
                groupRequestCountersList = Arrays.asList(groupRequestCounters);
                userSublist = groupRequestCountersList.subList(x, groupRequestCountersList.size());
            } catch (Exception ex) {
            }
            propertiesFileService.createPageJoinRequests(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfChomis = userSublist.size();
            int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
            if (numberOfSets > 0) {
                ussdSession.setNextGroupRequestCounterId(x + NEXT_CHOMI_ID_INCREMENT);
            } else {
                ussdSession.setNextGroupRequestCounterId(x + NEXT_CHOMI_ID_INCREMENT);
            }
            ussdSession.setNavigationLevel(33);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setGroupRequestCounters(userSublist);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getPageJoinRequests(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more chomis.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }

    }

    private String getViewGroupRequestUsers(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            int groupId = 0;
            String groupName = null;
            try {
                groupId = ussdSession.getGroupRequestCounters().get(Integer.valueOf(request) - 1).getGroupId();
                groupName = ussdSession.getGroupRequestCounters().get(Integer.valueOf(request) - 1).getGroupName();
                ussdSession.setSelectedGroupId(groupId);
                ussdSession.setGroupName(groupName);
                ussdSessionService.updateUssdSession(ussdSession);
            } catch (Exception e) {
                groupId = ussdSession.getSelectedGroupId();
                groupName = ussdSession.getGroupName();
            }
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/joinrequests/" + groupId + "/", User[].class);
            User[] users = obj.getBody();
            List<User> userList = null;
            try {
                userList = Arrays.asList(users);
            } catch (Exception ex) {
                System.err.println("Users is NULL");
            }
            propertiesFileService.createPageRequestUsers(msisdn, userList, groupName, ussdSession.getUser().getLangCode().toLowerCase());
            if (userList != null) {
                int numberOfChomis = userList.size();
                int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next conversation
                    ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
                } else {
                    ussdSession.setNextChomiId(NEXT_CHOMI_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            try {
                ResponseEntity<Group> i = restTemplate.getForEntity(serviceBaseUrl + "/group/" + groupId + "/", Group.class);
                Group selectedGroup = i.getBody();
                ussdSession.setSelectedGroup(selectedGroup);
            } catch (Exception e) {
                e.printStackTrace();
                ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
            }
            ussdSession.setNavigationLevel(331);
            ussdSession.setLastUpdateTime(new Date());
            ussdSession.setUsersSublist(userList);
            ussdSession.setGroupName(groupName);
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Error was here");
            ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
            return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
        }
    }

    private String getViewGroupRequestUsersNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            int groupId = 0;
            String groupName = null;
            try {
                groupId = ussdSession.getGroupRequestCounters().get(Integer.valueOf(request) - 1).getGroupId();
                groupName = ussdSession.getGroupRequestCounters().get(Integer.valueOf(request) - 1).getGroupName();
                ussdSession.setSelectedGroupId(groupId);
                ussdSession.setGroupName(groupName);
                ussdSessionService.updateUssdSession(ussdSession);
            } catch (Exception e) {
                groupId = ussdSession.getSelectedGroupId();
                groupName = ussdSession.getGroupName();
            }
            try {
                int x = ussdSession.getNextChomiId();
                //CALL A SERVICE
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<User[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/group/joinrequests/" + groupId + "/", User[].class);
                User[] users = obj.getBody();
                List<User> userList = Arrays.asList(users);
                List<User> userSublist = userList.subList(x, userList.size());
                propertiesFileService.createPageRequestUsers(msisdn, userSublist, groupName, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfChomis = userSublist.size();
                int numberOfSets = (int) (numberOfChomis / NEXT_CHOMI_ID_INCREMENT);
                if (numberOfSets > 0) {
                    ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
                } else {
                    ussdSession.setNextChomiId(x + NEXT_CHOMI_ID_INCREMENT);
                }
                ussdSession.setNavigationLevel(331);
                ussdSession.setLastUpdateTime(new Date());
                ussdSession.setUsersSublist(userSublist);
                ussdSessionService.updateUssdSession(ussdSession);
                List<UssdMenu> list = ussdMenuService2.getMyChomis2(msisdn, userSublist, ussdSession.getUser().getLangCode().toLowerCase());
                this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
                return INDEX;
            } catch (Exception ex) {
                String message = "Sorry! Something went wrong.";
                if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                    message += " There are no more chomis.";
                }
                return this.getSomethingWentWrong(model, ussdSession, true, message);
            }
        } else {
            ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
            return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
        }

    }

    private String getViewGroupRequestUserAcceptOrDecline(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isNotUserSessionAbortOrTimeOut(request)) {
            List<User> users = ussdSession.getUsersSublist();
            User selectedUser = users.get(Integer.valueOf(request) - 1);
            propertiesFileService.createViewGroupRequestUserAcceptOrDecline(msisdn, selectedUser, ussdSession.getGroupName(), ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setNavigationLevel(3311);
            ussdSession.setSelectedUser(selectedUser);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getViewGroupRequestUserAcceptOrDecline(msisdn, selectedUser, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } else {
            ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
            return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
        }
    }

    private String getGroupRequestAccept(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> i = restTemplate.getForEntity(serviceBaseUrl + "/approvegroupjoin/" + ussdSession.getSelectedUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/1/", Void.class);
        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaveacceptedarequest.headerText"));
        return INDEX;
    }

    private String getGroupRequestReject(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> i = restTemplate.getForEntity(serviceBaseUrl + "/approvegroupjoin/" + ussdSession.getSelectedUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/2/", Void.class);
        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaverejectedarequest.headerText"));
        return INDEX;
    }

    private String getGroupRequestBlock(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity(serviceBaseUrl + "/approvegroupjoin/" + ussdSession.getSelectedUser().getId() + "/" + ussdSession.getSelectedGroup().getId() + "/2/", Void.class);
        model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaveblockedarequest.headerText"));
        return INDEX;
    }

    private String getDeletePrivatePageSuccess(ModelMap model, String msisdn, String request) {
        this.genericPageDeleteSuccess(model, msisdn, request, true);
        return INDEX;
    }

    private String getDeletePageSuccess(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        int typeOfGroup = ussdSession.getSelectedGroup().getPublicGroup();
        if (typeOfGroup == 1) {
            this.genericPageDeleteSuccess(model, msisdn, request, false);
        } else {
            this.genericPageDeleteSuccess(model, msisdn, request, true);
        }
        return INDEX;
    }

    private String genericPageDeleteSuccess(ModelMap model, String msisdn, String request, boolean isPrivate) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (isPrivate) {
            ussdSession.setNavigationLevel(701261);
        } else {
            ussdSession.setNavigationLevel(701262);
        }
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        Group selectedGroup = ussdSession.getSelectedGroup();
        String groupName = selectedGroup.getName();
        int groupId = selectedGroup.getId();
        int userId = ussdSession.getUser().getId();

        if (isNotUserSessionAbortOrTimeOut(request)) {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> k = restTemplate.getForEntity(serviceBaseUrl + "/groupownership/" + ussdSession.getUser().getId() + "/" + selectedGroup.getId() + "/", Boolean.class);
            switch (request) {
                case "1":
                    //Yes. Delete
                    try {
                        //CALL A SERVICE
                        restTemplate = new RestTemplate();
                        restTemplate.getForEntity(serviceBaseUrl + "/deletegroup/" + userId + "/" + groupId + "/", Void.class);
                        if (k.getBody()) {
                            model.addAttribute("pageHeader", setDynamicValue(replaceAmpersand(groupName, false), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaveunfoolowed.headerText"));
                        } else {
                            model.addAttribute("pageHeader", setDynamicValue(replaceAmpersand(groupName, false), ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youhaveunfoolowed.headerText"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ussdSessionService.deleteUssdSessionByMsisdn(msisdn);
                        return this.getSomethingWentWrong(model, ussdSession, true, "An error occured");
                    }
                    break;
                case "2":
                    //No. Do NOT delete
                    if (k.getBody()) {
                        model.addAttribute("pageHeader", setDynamicValue(groupName, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youchosenottodelete.headerText"));
                    } else {
                        model.addAttribute("pageHeader", setDynamicValue(groupName, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youchosenottounfllow.headerText"));
                    }
                    break;
                default:
                    return this.getSomethingWentWrong(model, ussdSession, true, "Invalid selection");
            }
        }
        return "";
    }

    private String getConfirmRemoveFromPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(7011311);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        User selectedUser = ussdSession.getSelectedUser();
        String name = selectedUser.getFirstName();
        model.addAttribute("pageHeader", setDynamicValue(name, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.areyousureyouwantdelete.headerText"));
        return INDEX;
    }

    private String getConfirmRemoveFromPageComplete(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(70113111);
        ussdSession.setLastUpdateTime(new Date());
        if (isNotUserSessionAbortOrTimeOut(request)) {
            switch (request) {
                case "1":
                    User selectedUser = ussdSession.getSelectedUser();
                    int userId = selectedUser.getId();
                    Group selectedGroup = ussdSession.getSelectedGroup();
                    int groupId = selectedGroup.getId();
                    String name = selectedUser.getFirstName();
                    try {
                        //CALL A SERVICE
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.getForObject(serviceBaseUrl + "/group/unfollow/" + groupId + "/" + userId + "/", Void.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return this.getSomethingWentWrong(model, ussdSession, true, "An error happened!");
                    }
                    if (selectedGroup.getPublicGroup() == 1) {
                        model.addAttribute("pageHeader", setDynamicValue(name, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.nolongerafollower.headerText"));
                    } else if (selectedGroup.getPublicGroup() == 0) {
                        model.addAttribute("pageHeader", setDynamicValue(name, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.nolongeramember.headerText"));
                    }
                    break;
                case "2":
                    model.addAttribute("pageHeader", getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.youchosenotto.headerText"));
                    break;
                default:
                    model.addAttribute("pageHeader", "An error happened!");
            }
        }
        return INDEX;
    }

    private String getCanUserPostOnPage(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(7011312);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        User selectedUser = ussdSession.getSelectedUser();
        String name = selectedUser.getFirstName();
        model.addAttribute("pageHeader", setDynamicValue(name, selectedUser.getLangCode().toLowerCase(), "dynamic.areyousureyouwant.headerText"));
        return INDEX;
    }

    private String getCanUserPostOnPageComplete(ModelMap model, String msisdn, String request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String sendChomiRequestFromGroup(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(7011313);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        return this.getPersonSendRequest(model, msisdn, request);
    }

    private String getGroupConversations(ModelMap model, String msisdn, String request, int read) {
        if (this.hasNoGroupUnreadMessages(msisdn, 0) && read == 0) {
            return this.getNoUnreadMessages(model, msisdn, request);
        }
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<com.ian.chomibridge.Conversation[]> i = restTemplate.getForEntity(serviceBaseUrl + "/mygroupconversations/" + user.getId() + "/" + read + "/", com.ian.chomibridge.Conversation[].class);
            com.ian.chomibridge.Conversation[] convs = null;
            List<com.ian.chomibridge.Conversation> conversationList = null;
            int numberOfConversations = 0;
            try {
                convs = i.getBody();
                conversationList = Arrays.asList(convs);
                numberOfConversations = conversationList.size();
            } catch (Exception e) {
            }
            propertiesFileService.createConversationsList(msisdn, conversationList, false, ussdSession.getUser().getLangCode().toLowerCase());
            int numberOfSets = (int) (numberOfConversations / NEXT_MESSAGE_ID_INCREMENT);
            if (numberOfSets > 0) {
                //You need to save the position of the next conversation
                ussdSession.setNextConversationId(NEXT_MESSAGE_ID_INCREMENT);
            } else {
                ussdSession.setNextConversationId(NEXT_MESSAGE_ID_INCREMENT);
                //There is no need to save the position of the next message
            }
            ussdSession.setNavigationLevel(34);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList3(msisdn, Arrays.asList(convs), ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            if (this.hasNoUnreadMessages(msisdn, 1)) {
                return this.getNoUnreadMessages(model, msisdn, request);
            }
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more conversations.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    public String getGroupInbox(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        if (ussdSession.getSelectedRequest() == null) {
            ussdSession.setSelectedRequest(request);
            ussdSessionService.updateUssdSession(ussdSession);
        }
        try {
            int x = ussdSession.getNextConversationId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            User user = ussdSessionService.findUssdSessionByMsisdn(msisdn).getUser();
            ResponseEntity<com.ian.chomibridge.Conversation[]> i = restTemplate.getForEntity(serviceBaseUrl + "/mygroupconversations/" + user.getId() + "/" + ussdSession.getRead() + "/", com.ian.chomibridge.Conversation[].class);
            com.ian.chomibridge.Conversation[] convs = i.getBody();
            List<com.ian.chomibridge.Conversation> conversationList = Arrays.asList(convs);
            List<com.ian.chomibridge.Conversation> conversationsSublist = conversationList.subList(x - NEXT_MESSAGE_ID_INCREMENT, conversationList.size());
            com.ian.chomibridge.Conversation conversation = conversationsSublist.get(Integer.valueOf(ussdSession.getSelectedRequest()) - 1);
            int userId = conversation.getUserId();
            ussdSession.setSelectedChomi(String.valueOf(userId));
            //CALL A SERVICE
            ResponseEntity<Mail[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-group/myuserid/" + ussdSession.getUser().getId() + "/groupid/" + userId + "/0/", Mail[].class);
            Mail[] mails = obj.getBody();
            List<Mail> mailList = Arrays.asList(mails);
            if (mailList != null) {
                propertiesFileService.createConversationRepliesListFile(msisdn, mailList, false, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = mailList.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextMessageId(NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setNextMessageId(NEXT_MESSAGE_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(341);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList4(msisdn, mailList, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more messages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    private String getGroupInboxOne(ModelMap model, String msisdn, String request) {
        this.group_inbox_dec(msisdn, request);
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Mail> obj = restTemplate.getForEntity(serviceBaseUrl + "/message/myuserid/" + ussdSession.getUser().getId() + "/groupchatid/" + ussdSession.getSelectedMessage() + "/", Mail.class);
            Mail mail = obj.getBody();
            propertiesFileService.createDetailedMessageFile2(msisdn, mail, ussdSession.getRead(), ussdSession.getUser().getLangCode().toLowerCase());
            ussdSession.setMail(mail);
            ussdSession.setNavigationLevel(3411);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getDetailedMessages(msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            restTemplate.getForObject(serviceBaseUrl + "/groupmessage/read/messageid/" + ussdSession.getMail().getMailId() + "/userid/" + ussdSession.getUser().getId() + "/", Void.class);//Set the message as read
            return INDEX;
        } catch (Exception ex) {
            ex.printStackTrace();
            return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. There are no messages.");
        }
    }

    private String getManageFollowers(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(70115);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        String str = ussdSession.getSelectedGroup().getPublicGroup() == 1
                ? getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.followers.headerText")
                : getDynamicMenuValue(ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.members.headerText");
        model.addAttribute("pageHeader", setDynamicValue(str, ussdSession.getUser().getLangCode().toLowerCase(), "dynamic.canpostonmywall.headerText"));
        return INDEX;
    }

    private String getGroupMessageReply(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        Group selectedGroup = new Group();
        selectedGroup.setId(Integer.valueOf(ussdSession.getSelectedChomi()));
        ussdSession.setSelectedGroup(selectedGroup);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Selected Chomi: " + ussdSession.getSelectedChomi());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Selected Group ID: " + selectedGroup.getId());
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        return this.getSendGroupMessage(model, msisdn, request);
    }

    private String getGroupInboxNext(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        try {
            int x = ussdSession.getNextMessageId();
            //CALL A SERVICE
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Mail[]> obj = restTemplate.getForEntity(serviceBaseUrl + "/my-messages-from-a-group/myuserid/" + ussdSession.getUser().getId() + "/groupid/" + ussdSession.getSelectedChomi() + "/0/", Mail[].class);
            Mail[] mails = obj.getBody();
            List<Mail> mailList = Arrays.asList(mails);
            List<Mail> mailSublist = mailList.subList(x, mailList.size());
            if (mailList != null) {
                propertiesFileService.createConversationRepliesListFile(msisdn, mailSublist, true, ussdSession.getUser().getLangCode().toLowerCase());
                int numberOfMessages = mailSublist.size();
                int numberOfSets = (int) (numberOfMessages / NEXT_MESSAGE_ID_INCREMENT);
                if (numberOfSets > 0) {
                    //You need to save the position of the next message
                    ussdSession.setNextMessageId(x + NEXT_MESSAGE_ID_INCREMENT);
                } else {
                    ussdSession.setNextMessageId(x + NEXT_MESSAGE_ID_INCREMENT);
                    //There is no need to save the position of the next message
                }
            }
            ussdSession.setNavigationLevel(347);
            ussdSession.setLastUpdateTime(new Date());
            ussdSessionService.updateUssdSession(ussdSession);
            List<UssdMenu> list = ussdMenuService2.getMessagesList4(msisdn, mailSublist, ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, msisdn, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        } catch (Exception ex) {
            String message = "Sorry! Something went wrong.";
            if (request.equalsIgnoreCase("7") | !request.equalsIgnoreCase("7")) {
                message += " There are no more messages.";
            }
            return this.getSomethingWentWrong(model, ussdSession, true, message);
        }
    }

    private String getGroupMessageDelete(ModelMap model, String msisdn, String request) {
        UssdSession ussdSession = ussdSessionService.findUssdSessionByMsisdn(msisdn);
        ussdSession.setNavigationLevel(34110);
        ussdSession.setLastUpdateTime(new Date());
        ussdSessionService.updateUssdSession(ussdSession);
        if (ussdSession.getUser().getId() != ussdSession.getMail().getFromUserId()) {
            model.addAttribute("pageHeader", getDynamicMenuValue(request, "dynamic.youcanonlydelete.headerText"));
            return INDEX;
        } else {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject(serviceBaseUrl + "/deletemessage/" + ussdSession.getMail().getMailId() + "/", Void.class);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return this.getSomethingWentWrong(model, ussdSession, true, "Sorry! Something went wrong. The message could not be deleted.");
            }
            List<UssdMenu> list = ussdMenuService.getMessageDelete(ussdSession.getUser().getLangCode().toLowerCase());
            this.setMenuItems(model, list, ussdSession.getUser().getLangCode().toLowerCase());
            return INDEX;
        }
    }

    private static boolean isNextMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.next.option"));
    }

    private static boolean isPreviousMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.previous.option"));
    }

    private static boolean isCreatePageMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.createpage.option"));
    }

    private static boolean isMyPagesMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.mypages.option"));
    }

    private static boolean isSearchMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.search.option"));
    }

    private static boolean isBackMenuItem(String value, String language) {
        return value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.back.option"));
    }

    private boolean qualifiesForNine(String value, String language) {
        return (value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.addpost.option"))
                || value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.posttowall.option"))
                || value.equalsIgnoreCase(getDynamicMenuValue(language, "dynamic.messagesmenu.option5"))
                || value.equalsIgnoreCase(getDynamicMenuValue(language, "postorviewcomment.option1")));
    }

    private boolean isPageExisting(String pageName) {
        //CALL A SERVICE
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Group[]> i = restTemplate.getForEntity(serviceBaseUrl + "/groupsearch/" + pageName + "/", Group[].class);
        com.ian.chomibridge.Group[] groups = i.getBody();
        List<Group> groupsList = null;
        int numberOfGroups = 0;
        try {
            groupsList = Arrays.asList(groups);
            numberOfGroups = groupsList.size();
        } catch (Exception e) {
        }
        return numberOfGroups > 0;
    }

}
