package com.resttempleteanwser;

import com.resttempleteanwser.Proxy.QuoteProxy;
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
        System.out.println(quoteApp.makeAllGetRequest());
    }
}
