package com.arch.mvc.controllers;

import com.arch.mvc.dto.BaseResponseDTO;
import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.exceptions.TweetsNotFoundException;
import com.arch.mvc.models.Tweet;
import com.arch.mvc.services.TweetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to get tweets from Elasticsearch cluster
 *
 * @author jimil
 */
@RestController
@RequestMapping("/api/tweet")
@CrossOrigin(origins = "*")
public class TweetController implements IController {

    @Autowired
    private TweetService tweetService;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public BaseResponseDTO getTweets(@RequestBody Map<String, String> payload, HttpServletRequest request) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            String username = getUsername(request);
            String sentiment = payload.get("sentiment");
            if(StringUtils.isBlank(sentiment)) {
                throw new PayloadEmptyException("'sentiment' cannot be null.");
            }
            List<Tweet> tweets = tweetService.findBySentiment(sentiment);
            if(tweets.isEmpty()) {
                throw new TweetsNotFoundException("No tweets found for the given query.");
            }
            HashMap<String, List<Tweet>> data = new HashMap<>();
            data.put("tweets", tweets);
            baseResponseDTO.setData(data);
        } catch(Exception e) {
            System.out.println(e);
            baseResponseDTO.setError(e.getMessage(), e);
        }
        return baseResponseDTO;
    }

}
