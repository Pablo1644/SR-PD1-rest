package com.resttempleteanwser;

import com.resttempleteanwser.Proxy.QuoteProxy;
import com.resttempleteanwser.records.QuotePost;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
@Log4j2
@SpringBootApplication
public class RestTempleteAnwserApplication {
    private final QuoteProxy quoteApp;

    public RestTempleteAnwserApplication(QuoteProxy quoteApp) {
        this.quoteApp = quoteApp;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestTempleteAnwserApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void run() {
        log.warn("GET TESTING");
        log.info(quoteApp.makeAllGetRequest());
        log.info("GET WITH ID");
        log.info(quoteApp.getQuoteByID(7L));
        log.info("GET WITH req ID");
        log.info(quoteApp.getQuoteWithRequestID(7L));
        log.info("RANDOM:{}", quoteApp.getRandomQuote());
        log.info(quoteApp.getQuoteWithHeader("DDD","CCC"));
        log.warn("PUT TESTING");
        log.info(quoteApp.postQuote(new QuotePost("My quote")));
        log.info("QUOTE WAS ADDED. ADDED QUOTE IS BELOW!");
        log.info(quoteApp.getQuoteByID((quoteApp.getQuoteCount())));
        log.warn("DELETE TESTING ID = 10");
        log.info(quoteApp.deleteQuoteWithID(10L));
        log.info("ID=9: {}", quoteApp.getQuoteByID(9L));
        log.info("After delete ID = 10 : {}",quoteApp.getQuoteByID(10L));
        log.info("ID=11: {}", quoteApp.getQuoteByID(11L));

    }
}
