package com.ian.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesReader {

    private final String baseUrl = "/var/apache-tomcat-7.0.70/";
    private final String folder = "chomiussd";
    Properties prop = null;

    public String getPropertyValue(String code, String language) {
        try {
            prop = new PropertiesReader.ReadPropertyFile().readFile(language);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        String propertyValue;
        propertyValue = (String) prop.getProperty(code);
        return propertyValue;
    }

    public String getPropertyValue(String code, String msisdn, String language) {
        try {
            prop = new PropertiesReader.ReadPropertyFile().readFile(msisdn);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        String propertyValue;
        propertyValue = (String) prop.getProperty(code);
        return propertyValue;
    }

    public Properties getProperty(String language) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + language + ".properties");
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

    public Properties getProperty(String msisdn, String language) {
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

    class ReadPropertyFile {

        public Properties readFile(String language) throws IOException {
            Properties prop = new Properties();
            InputStream input = null;
            try {
                input = new FileInputStream(baseUrl + "webapps/" + folder + "/WEB-INF/classes/" + language + ".properties");
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

        public Properties readFile(String msisdn, String language) throws IOException {
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

    public static String getGeneralPropertyValue(String language, String code) {
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = (String) props.get(code);
            return p;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getDynamicMenuValue(String language, String code) {
        //System.out.println("--------------------------------------- getDynamicMenuValue method called ------------------------------------------");
        try {
            Properties props = new PropertiesReader().getProperty(language);
            String p = (String) props.get(language + "." + code);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR_IN_CODE";
        }
    }
}
