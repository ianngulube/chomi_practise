/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Trending;
import java.util.Date;
import java.util.List;

public interface TrendingService {

    void saveTrending(Trending trending);

    void updateTrending(Trending newTrending);

    List<Trending> findAllTrendings();

    Trending findTrendingById(String id);

    Trending findTrendingByDateRange(Date id);

    void deleteTrending();

    Trending findTrendingByPostedBy(String postedBy);

    List<Trending> findAllTrendingsByPostedBy(String postedBy);

    List<Trending> findAllTrendingsPostedByChomis(List<String> friendIds);

    List<Trending> findAllTrendingByTitle(String searchText);

    Trending findTrendingByTitle(String searchText);
}
