/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.UssdSession;
import java.util.List;

public interface UssdSessionService {

    void saveUssdSession(UssdSession ussdSession);

    List<UssdSession> listUssdSessions();

    UssdSession findUssdSession(String id);

    UssdSession findUssdSessionByMsisdn(String msisdn);

    void updateUssdSession(UssdSession ussdSession);

    void deleteUssdSession(String id);

    void deleteUssdSessionByMsisdn(String msisdn);

    void deleteOldUssdSessions();
}
