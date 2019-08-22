import json
import time
import re

import pandas as pd
import nltk
nltk.download('punkt')

from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream
from textblob import TextBlob
from elasticsearch import Elasticsearch

# import twitter keys and tokens
from config import *

# create instance of elasticsearch
es = Elasticsearch([{'host': 'localhost', 'port': 9200}])

class TweetStreamListener(StreamListener):

    # on success
    def on_data(self, data):

        print("New Tweet Fetched!")

        global count
        count = count + 1

        if(count == 100):
            print("20 tweets fetched. Now returning")
            return True

        # decode json
        dict_data = json.loads(data)

        # ------------------------ START DATA CLEANING ------------------------
        tweet = dict_data['text']
        if tweet is None:
            print("Tweet has no relevant text, skipping")
            return True

        # clean up tweet text more
        tweet = tweet.replace("\n", " ")
        tweet = re.sub(r"http\S+", "", tweet)
        tweet = re.sub(r"&.*?;", "", tweet)
        tweet = re.sub(r"<.*?>", "", tweet)
        tweet = tweet.replace("RT", "")
        tweet = tweet.replace(u"…", "")
        tweet = tweet.strip()

        # get date when tweet was created
        created_date = time.strftime(
            '%Y-%m-%dT%H:%M:%S', time.strptime(dict_data['created_at'], '%a %b %d %H:%M:%S +0000 %Y'))

        # store dict_data into vars
        screen_name = str(dict_data.get("user", {}).get("screen_name"))
        location = str(dict_data.get("user", {}).get("location"))
        language = str(dict_data.get("user", {}).get("lang"))
        friends = int(dict_data.get("user", {}).get("friends_count"))
        followers = int(dict_data.get("user", {}).get("followers_count"))
        statuses = int(dict_data.get("user", {}).get("statuses_count"))
        text_filtered = str(tweet)
        tweetid = int(dict_data.get("id"))
        text_raw = str(dict_data.get("text"))
        full_text = str(dict_data.get("full_text"))

        # output twitter data
        print("\n------------------------------")
        print("Tweet Date: " + created_date)
        print("Screen Name: " + screen_name)
        print("Location: " + location)
        print("Language: " + language)
        print("Friends: " + str(friends))
        print("Followers: " + str(followers))
        print("Statuses: " + str(statuses))
        print("Tweet ID: " + str(tweetid))
        print("Tweet Raw Text: " + text_raw)
        print("Tweet Filtered Text: " + text_filtered)
        print("Full Text: " + full_text)

        if friends == 0 or followers == 0 or statuses == 0 or tweet == "":
            print("Tweet doesn't meet min requirements, not adding")
            return True

        # strip out hashtags for language processing
        tweet = re.sub(r"[#|@|\$]\S+", "", tweet)
        tweet.strip()
        tweet = re.sub(r"[@|@|\$]\S+", "", tweet)
        tweet.strip()
        # ------------------------- END DATA CLEANING -------------------------

        # pass tweet into TextBlob
        tweet = TextBlob(dict_data["text"])
        polarities = []

        for sentence in tweet.sentences:
            polarities.append(sentence.sentiment.polarity)

        tweet_polarity = sum(polarities)
        # determine if sentiment is positive, negative, or neutral
        if tweet_polarity < 0:
            sentiment = "negative"
        elif tweet_polarity == 0:
            sentiment = "neutral"
        else:
            sentiment = "positive"

        # output sentiment
        print(tweet + ", " + sentiment)

        # add text and sentiment info to elasticsearch
        es.index(index="sentiment",
                 doc_type="tweets",
                 body={"author": dict_data["user"]["screen_name"],
                       "date": created_date,
                       "message": dict_data["text"],
                       "full_text": full_text,
                       "polarity": tweet.sentiment.polarity,
                       "subjectivity": tweet.sentiment.subjectivity,
                       "sentiment": sentiment})
        return True

    # on failure
    def on_error(self, status):
        print(status)

def create_index(es_object, index_name='sentiment'):
    created = False
    # index settings
    settings = {
        "settings": {
            "number_of_shards": 1,
            "number_of_replicas": 0
        },
        "mappings": {
            "members": {
                "dynamic": "strict",
                "properties": {
                    "author": {
                        "type": "text"
                    },
                    "date": {
                        "type": "text"
                    },
                    "message": {
                        "type": "text"
                    },
                    "full_text": {
                        "type": "text"
                    },
                    "polarity": {
                        "type": "float"
                    },
                    "subjectivity": {
                        "type": "float"
                    },
                    "sentiment": {
                        "type": "float"
                    },
                }
            }
        }
    }
    try:
        if not es_object.indices.exists(index_name):
            # Ignore 400 means to ignore "Index Already Exist" error.
            es_object.indices.create(index=index_name, ignore=400, body=settings)
            print('Created Index')
            created = True
    except Exception as ex:
        print(str(ex))
    finally:
        return created

if __name__ == '__main__':

    # create sentiment index in ES
    create_index(es)

    # create instance of the tweepy tweet stream listener
    listener = TweetStreamListener()
    print("Listener initialized.")

    # set twitter keys/tokens
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    print("Auth done.")

    # create instance of tweepy API
    # api = API(auth, wait_on_rate_limit=True, wait_on_rate_limit_notify=True, retry_count=10, retry_delay=900, retry_errors=420)

    # create instance of the tweepy stream
    stream = Stream(auth, listener)
    print("Stream created.")

    # Read Nifty 50 stocks and fetch tweets -> apply sentiment analysis
    stock_list = read_nifty_50_stocks()
    global count
    count = 0
    # search twitter for the stock symbol
    # print("Searching tweets for: " + symbol)
    stream.filter(track=["#StopIllegalArrestsInKashmir","#Article370Scrapped","#KashmirWithModi"],languages=["en"])
