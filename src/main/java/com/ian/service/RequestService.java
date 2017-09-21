/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Request;
import java.util.List;

public interface RequestService {

    void saveRequest(Request request);

    void updateRequest(Request newRequest);

    List<Request> findAllRequestsByDestination(String destination);

    Request findRequestById(String id);

    Request findRequestByOrigin(String id);

    Request findNewRequestByOrigin(String id);

    void deleteRequest();
}
