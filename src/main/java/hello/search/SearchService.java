package hello.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.TwitterProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private SearchCachedService searchCachedService;
    private SimpMessagingTemplate webSocket;


    @Autowired
    public SearchService(SearchCachedService searchCachedService, SimpMessagingTemplate simpMessagingTemplate) {
        this.searchCachedService = searchCachedService;
        this.webSocket = simpMessagingTemplate;
    }

    public void twitterSearch(SearchParameters.ResultType searchType, List<String> keywords) {
        List<SearchParameters> searchParameters = keywords.stream()
                .map(taste -> createSearchParam(searchType, taste))
                .collect(Collectors.toList());

        searchParameters.stream().parallel()
                .map(searchCachedService::search)
                .flatMap(Collection::stream)
                .forEach(tweet -> webSocket.convertAndSend("/topic/searchResults", tweet));
    }

    private SearchParameters createSearchParam(SearchParameters.ResultType searchType, String taste) {
        SearchParameters searchParameters = new SearchParameters(taste);
        searchParameters.resultType(searchType);
        searchParameters.count(5);
        return searchParameters;
    }
}
