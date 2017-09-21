/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Trending;
import com.ian.service.TrendingService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class TrendingServiceImpl implements TrendingService {

    @Autowired
    private MongoTemplate trendingMongoTemplate;

    @Override
    public void saveTrending(Trending trending) {
        if (!trendingMongoTemplate.collectionExists(Trending.class)) {
            trendingMongoTemplate.createCollection(Trending.class);
        }
        trendingMongoTemplate.insert(trending, "Trending");
    }

    @Override
    public void updateTrending(Trending newTrending) {
        Trending trending = trendingMongoTemplate.findOne(Query.query(Criteria.where("id").is(newTrending.getId())), Trending.class, "Trending");
        trending.setPostedBy(newTrending.getPostedBy());
        trending.setPostIds(newTrending.getPostIds());
        trending.setTime(newTrending.getTime());
        trending.setTitle(newTrending.getTitle());
        trendingMongoTemplate.save(trending, "Trending");
    }

    @Override
    public List<Trending> findAllTrendings() {
        return trendingMongoTemplate.findAll(Trending.class, "Trending");
    }

    @Override
    public Trending findTrendingById(String id) {
        return trendingMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Trending.class, "Trending");
    }

    @Override
    public Trending findTrendingByDateRange(Date id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteTrending() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Trending findTrendingByPostedBy(String postedBy) {
        return trendingMongoTemplate.findOne(Query.query(Criteria.where("postedBy").is(postedBy)), Trending.class, "Trending");
    }

    @Override
    public List<Trending> findAllTrendingsByPostedBy(String postedBy) {
        return trendingMongoTemplate.find(Query.query(Criteria.where("postedBy").is(postedBy)), Trending.class, "Trending");
    }

    @Override
    public List<Trending> findAllTrendingsPostedByChomis(List<String> friendIds) {
        List<Trending> trendings = null;
        if (friendIds != null) {
            trendings = new ArrayList<>();
            for (String id : friendIds) {
                Trending p = this.trendingMongoTemplate.findOne(Query.query(Criteria.where("postedBy").is(id)), Trending.class, "Trending");
                if (p != null) {
                    trendings.add(p);
                }
            }
        }
        if (trendings.isEmpty()) {
            System.out.println("%%%%%%%% we return null");
            return null;
        } else {
            return trendings;
        }
    }

    @Override
    public List<Trending> findAllTrendingByTitle(String searchText) {
        return trendingMongoTemplate.find(Query.query(Criteria.where("title").is(searchText)), Trending.class, "Trending");
    }

    @Override
    public Trending findTrendingByTitle(String searchText) {
        return trendingMongoTemplate.findOne(Query.query(Criteria.where("title").is(searchText)), Trending.class, "Trending");
    }

}
