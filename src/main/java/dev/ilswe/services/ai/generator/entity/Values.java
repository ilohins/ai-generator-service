package dev.ilswe.services.ai.generator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Values(
        @JsonProperty(required = true, value = "values") Value[] values) {

    record Value(
            @JsonProperty(required = true, value = "value1") String value1,
            @JsonProperty(required = true, value = "value2") String value2) {
    }
}
