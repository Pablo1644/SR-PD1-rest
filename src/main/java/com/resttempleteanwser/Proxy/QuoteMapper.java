package com.resttempleteanwser.Proxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resttempleteanwser.records.Quote;
import com.resttempleteanwser.records.QuoteValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QuoteMapper {
    public Quote mapJsonToQuote(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> valueMap = (Map<String, Object>) responseMap.get("value");
        QuoteValue quoteValue = objectMapper.convertValue(valueMap, QuoteValue.class);
        return new Quote(quoteValue.id(), quoteValue);
    }
    public List<Quote> mapJsonToListQuote(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Deserializujemy na List<Map<String, Object>> dla struktury "value"
        List<Map<String, Object>> quotesWithValue = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});

        List<Quote> quotes = new ArrayList<>();
        for (Map<String, Object> quoteData : quotesWithValue) {
            Map<String, Object> valueMap = (Map<String, Object>) quoteData.get("value");
            Long id = ((Integer) valueMap.get("id")).longValue();
            String quote = (String) valueMap.get("quote");
            quotes.add(new Quote(id, new QuoteValue(id, quote)));
        }

        return quotes;
    }

}

