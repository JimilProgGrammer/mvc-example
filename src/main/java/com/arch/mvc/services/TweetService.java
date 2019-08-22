package com.arch.mvc.services;

import com.arch.mvc.models.Tweet;
import com.arch.mvc.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for TweetController
 *
 * @author jimil
 */
@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    public Iterable<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    public List<Tweet> findBySentiment(String sentiment) {
        return tweetRepository.findBySentiment(sentiment);
    }

}
