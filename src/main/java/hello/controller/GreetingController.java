package hello.controller;

import hello.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class GreetingController {

    @Autowired
    private SearchService searchService;

    @MessageMapping("/search")
    @Async
    public void greeting(@RequestParam String search) throws Exception {
        searchService.twitterSearch(SearchParameters.ResultType.POPULAR, Arrays.asList(search.split(" ")));
    }

}
