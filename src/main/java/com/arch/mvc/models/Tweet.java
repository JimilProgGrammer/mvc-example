package com.arch.mvc.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Models a tweet saved in elasticsearch
 *
 * @author jimil
 */
@Document(indexName = "sentiment", type = "tweets")
public class Tweet {

    @Id
    private String id;
    private String author;
    private String date;
    private String message;
    private String full_text;
    private float polarity;
    private float subjectivity;
    private String sentiment;

    public Tweet() { }

    public Tweet(String author, String date, String message, String full_text, float polarity, float subjectivity, String sentiment) {
        this.author = author;
        this.date = date;
        this.message = message;
        this.full_text = full_text;
        this.polarity = polarity;
        this.subjectivity = subjectivity;
        this.sentiment = sentiment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getPolarity() {
        return polarity;
    }

    public void setPolarity(float polarity) {
        this.polarity = polarity;
    }

    public float getSubjectivity() {
        return subjectivity;
    }

    public void setSubjectivity(float subjectivity) {
        this.subjectivity = subjectivity;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    @Override
    public String toString() {
        return "{author: " + this.author +", date: " + this.date + ", message: " + this.message + ", polarity: " + polarity
            + ", subjectivity: " + this.subjectivity + ", sentiment: " + this.sentiment + "}";
    }

}
