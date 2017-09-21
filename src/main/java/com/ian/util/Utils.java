package com.ian.util;

public class Utils {

    /*
    This method converts the user input from 1, 2,.. to gender types.
     */
    public static String convertGender(String request) {
        String gender = null;
        switch (request) {
            case "1":
                gender = "Male";
                break;
            case "2":
                gender = "Female";
                break;
            case "3":
                gender = "Undisclosed";
                break;
            default:
                gender = "Undisclosed";
        }
        return gender;
    }

    /*
    This method converts the user input from 1, 2,.. to language names in South Africa.
     */
    public static String convertLanguage(String request) {
        String language = null;
        switch (request) {
            case "1":
                language = "English";
                break;
            case "2":
                language = "IsiZulu";
                break;
            case "3":
                language = "IsiXhosa";
                break;
            case "4":
                language = "Afrikaans";
                break;
            case "5":
                language = "Sesotho";
                break;
            case "6":
                language = "Tswana";
                break;
            case "7":
                language = "Tsonga";
                break;
            case "8":
                language = "Swati";
                break;
            case "9":
                language = "TshiVenda";
                break;
            case "10":
                language = "IsiNdebele";
                break;
            case "11":
                language = "Pedi";
                break;
            case "12":
                language = "German";
                break;
            default:
                language = "";
                break;
        }
        return language;
    }

    /*
    This method converts the user input from 1, 2,.. to province names in South Africa.
     */
    public static String convertProvince(String request) {
        String province = null;
        switch (request) {
            case "1":
                province = "Gauteng";
                break;
            case "2":
                province = "Eastern Cape";
                break;
            case "3":
                province = "Free State";
                break;
            case "4":
                province = "KwaZulu Natal";
                break;
            case "5":
                province = "Limpopo";
                break;
            case "6":
                province = "Mpumalanga";
                break;
            case "7":
                province = "North West";
                break;
            case "8":
                province = "Northen Cape";
                break;
            case "9":
                province = "Western Cape";
                break;
            default:
                province = "Undefined";
        }
        return province;
    }

    public static boolean isNotUserSessionAbortOrTimeOut(String request) {
        final String MOUserAbort = "MO User Abort";
        final String MOSessionTimeout = "MO Session Timeout";
        final String MOSessionTimeout_ = " MO Session Timeout";
        String MOUserTimeout = "MO User Timeout";
        String VC_CODE = "*117*24664#";
        return !(request.contains(MOUserAbort)
                || request.contains(MOSessionTimeout)
                || request.contains(MOUserTimeout)
                || request.contains(MOSessionTimeout_)
                || request.contains(VC_CODE));
    }

    public static boolean hasSufficientLength(String in, int length) {
        return (in.length() >= length);
    }
    
    public static boolean startWithCharacter(String in, char c) {
        return (in.charAt(0) == c);
    }

}
