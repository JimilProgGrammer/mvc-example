package com.arch.mvc.repositories;

import com.arch.mvc.models.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Repository interface to handle tweets inside elasticsearch
 *
 * @author jimil
 */
public interface TweetRepository extends ElasticsearchRepository<Tweet, String> {

    Page<Tweet> findAll();

    List<Tweet> findBySentiment(String sentiment);

}
