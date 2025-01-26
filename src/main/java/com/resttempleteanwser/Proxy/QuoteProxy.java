package com.resttempleteanwser.Proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resttempleteanwser.records.Quote;
import com.resttempleteanwser.records.QuotePost;
import com.resttempleteanwser.records.QuoteValue;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Log4j2
@Component
public class QuoteProxy {

    private final RestTemplate restTemplate;

    @Value("${quote.service.url}")
    private String baseUrl;

    @Value("${quote.service.port}")
    private int port;

    public QuoteProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getString(URI uri, Object entity) {
        try {
            ResponseEntity<String> response = (entity != null) ?
                    restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(entity), String.class) :
                    restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            return response.getBody();
        } catch (Exception e) {
            logError(e);  // Call the error handler
        }
        return null;
    }

    private URI buildUriWithInsertedID(long id) {
        return UriComponentsBuilder.fromUriString("/api/quote/{id}")
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .buildAndExpand(id)
                .toUri();
    }

    private URI buildUriWithRequestID(long id) {
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .path("/apiWithRequestParam")
                .queryParam("id", id)
                .build()
                .toUri();
    }

    private URI buildUri(String destinationEndpoint, Object... uriVariables) {
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .path(destinationEndpoint)
                .buildAndExpand(uriVariables)
                .toUri();
    }

    public String postQuote(URI uri, Object entity) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(entity);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            logError(e);  // Call the error handler
        }
        return null;
    }

    public String deleteQuote(URI uri, Object entity) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(entity);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            logError(e);  // Call the error handler
        }
        return null;
    }

    private void logError(Exception e) {
        if (e instanceof RestClientResponseException responseException) {
            log.error("Response error: {}", responseException.getResponseBodyAsString());
        } else if (e instanceof RestClientException) {
            log.error("Request error: {}", e.getMessage());
        } else if (e instanceof JsonProcessingException) {
            log.error("JSON processing error: {}", e.getMessage());
        } else {
            log.error("Unexpected error: {}", e.getMessage(), e);
        }
    }

    public List<Quote> makeAllGetRequest() {
        URI uri = buildUri("/api");
        String responseBody = getString(uri, null);
        if (responseBody != null) {
            QuoteMapper quoteMapper = new QuoteMapper();
            try {
                return quoteMapper.mapJsonToListQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Quote getQuoteByID(long id) {
        URI uri = buildUri("/api/{id}", id);
        return getQuote(uri);
    }

    public Quote getRandomQuote() {
        URI uri = buildUri("/api/random");
        return getQuote(uri);
    }

    private Quote getQuote(URI uri) {
        String responseBody = getString(uri, null);
        if (responseBody != null) {
            QuoteMapper quoteMapper = new QuoteMapper();
            try {
                return quoteMapper.mapJsonToQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Quote getQuoteWithRequestID(long id) {
        URI uri = buildUriWithRequestID(id);
        return getQuote(uri);
    }

    public List<Quote> getQuoteWithHeader(String header, String value) {
        URI uri = buildUri("/apiWithHeader");
        HttpHeaders headers = new HttpHeaders();
        headers.add(header, value);
        QuoteValue val = new QuoteValue(1L, "QUOTE");
        Quote requestBody = new Quote(1L, val);
        HttpEntity<Quote> entity = new HttpEntity<>(requestBody, headers);
        String responseBody = getString(uri, entity);
        if (responseBody != null) {
            QuoteMapper quoteMapper = new QuoteMapper();
            try {
                return quoteMapper.mapJsonToListQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Collections.emptyList();
    }

    public Quote postQuote(QuotePost quotePost) {
        URI uri = buildUri("/api/quote");
        String responseBody = postQuote(uri, quotePost);
        if (responseBody != null) {
            QuoteMapper quoteMapper = new QuoteMapper();
            try {
                return quoteMapper.mapJsonToQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Quote deleteQuoteWithID(long id) {
        URI uri = buildUriWithInsertedID(id);
        String responseBody = deleteQuote(uri, null);
        if (responseBody != null && !responseBody.isEmpty()) {
            QuoteMapper quoteMapper = new QuoteMapper();
            try {
                return quoteMapper.mapJsonToQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public long getQuoteCount() {
        URI uri = buildUri("/api");
        String responseBody = getString(uri, null);
        if (responseBody != null) {
            QuoteMapper quoteMapper = new QuoteMapper();
            List<Quote> quoteList;
            try {
                quoteList = quoteMapper.mapJsonToListQuote(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return quoteList != null ? quoteList.size() : 0;
        }
        return 0;
    }
}
