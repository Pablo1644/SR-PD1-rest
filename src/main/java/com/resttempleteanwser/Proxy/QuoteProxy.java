package com.resttempleteanwser.Proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resttempleteanwser.records.Quote;
import com.resttempleteanwser.records.QuotePost;
import com.resttempleteanwser.records.QuoteValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;

@Component
public class QuoteProxy {

    private final RestTemplate restTemplate;


    @org.springframework.beans.factory.annotation.Value("${quote.service.url}")
    private String baseUrl;

    @Value("${quote.service.port}")
    private int port;

    private String getString(URI uri, Object entity) {
        ResponseEntity<String> response;
        if (entity != null) {
            response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(entity),
                    String.class
            );
        } else {

            response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    String.class
            );
        }

        return response.getBody();
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
                .port(port) // Port
                .path(destinationEndpoint) // Ścieżka
                .buildAndExpand(uriVariables)
                .toUri();
    }

    public String postQoute(URI uri, Object entity) {
        ResponseEntity<String> response;
        HttpEntity<Object> requestEntity = new HttpEntity<>(entity);

        response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody(); // Zwracamy ciało odpowiedzi
    }

    public String deleteQuote(URI uri, Object entity) {
        ResponseEntity<String> response;
        HttpEntity<Object> requestEntity = new HttpEntity<>(entity);

        response = restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        return response.getBody();
    }


    public QuoteProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Quote> makeAllGetRequest() {
        try {

            URI uri = buildUri("/api");
            String responseBody = getString(uri, null);
            System.out.println(responseBody);
            QuoteMapper quoteMapper = new QuoteMapper();
            return quoteMapper.mapJsonToListQuote(responseBody); // Mapujemy JSON na listę Quote
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }

    public Quote getQuoteByID(long ID) {
        try {
            URI uri = buildUri("/api/{id}", ID);
            String responseBody = getString(uri, null);
            QuoteMapper quoteMapper = new QuoteMapper();
            return quoteMapper.mapJsonToQuote(responseBody);
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }

    public Quote getRandomQuote() {
        try {
            URI uri = buildUri("/api/random");
            String responseBody = getString(uri, null);
            QuoteMapper quoteMapper = new QuoteMapper();
            return quoteMapper.mapJsonToQuote(responseBody);
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }

    public Quote getQuoteWithRequestID(long ID) {
        try {
            URI uri = buildUriWithRequestID(ID);
            String responseBody = getString(uri, null);
            QuoteMapper quoteMapper = new QuoteMapper();
            return quoteMapper.mapJsonToQuote(responseBody); // Mapujemy JSON na listę Quote
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }

    public ResponseEntity<List<Quote>> getQuoteWithHeader(String header, String value) {

        try {
            URI uri = buildUri("/apiWithHeader");
            HttpHeaders headers = new HttpHeaders();
            headers.add("requestId", "45417SQUAD");
            QuoteValue val = new QuoteValue(1L, "QUOTE");
            Quote requestBody = new Quote(1L, val);
            HttpEntity<Quote> entity = new HttpEntity<>(requestBody, headers);
            String responseBody = getString(uri, entity);
            QuoteMapper quoteMapper = new QuoteMapper();
            List<Quote> quoteList = quoteMapper.mapJsonToListQuote(responseBody);
            return new ResponseEntity<>(quoteList, headers, HttpStatus.OK);
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public Quote postQuote(QuotePost quotePost) {
        try {
            URI uri = buildUri("/api/quote");

            String responseBody = postQoute(uri, quotePost);
            QuoteMapper quoteMapper = new QuoteMapper();
            return quoteMapper.mapJsonToQuote(responseBody);
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }

    public Quote deleteQuoteWithID(long ID) {
        try {
            URI uri = buildUriWithInsertedID(ID);
            String responseBody = deleteQuote(uri, null);
            if (responseBody != null && !responseBody.isEmpty()) {
                QuoteMapper quoteMapper = new QuoteMapper();
                return quoteMapper.mapJsonToQuote(responseBody);
            }
            return null;
        } catch (RestClientResponseException e) {
            System.err.println("Response error: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.err.println("Request error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("JSON processing error: " + e.getMessage());
        }
        return null;
    }


}