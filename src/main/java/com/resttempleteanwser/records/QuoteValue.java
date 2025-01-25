package com.resttempleteanwser.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuoteValue(Long id, String quote)  {
}
