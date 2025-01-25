package com.resttempleteanwser.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote (Long id, QuoteValue quoteValue) {
}