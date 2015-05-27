package hello.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.TwitterProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchCachedService {
    private TwitterProperties twitterProperties;

    @Autowired
    public SearchCachedService(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }

    @Cacheable("tweets") public List<Tweet> search(SearchParameters params) {
        System.out.println("SEARCH " + params.getQuery());

        TwitterTemplate twitter = new TwitterTemplate(twitterProperties.getAppId(), twitterProperties.getAppSecret());
        return twitter.searchOperations().search(params).getTweets();
    }
}
