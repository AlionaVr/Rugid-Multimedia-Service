package com.rugid.multimediaservice.adapter.in.rest.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class RetrieveDefaultImageIdResponseTest {
    @Autowired
    private JacksonTester<RetrieveDefaultImageIdResponse> json;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDeleteDefaultImageIdResponseDto() throws IOException {
        RetrieveDefaultImageIdResponse retrieveDefaultImageIdResponse = new RetrieveDefaultImageIdResponse("qwe");

        JsonContent<RetrieveDefaultImageIdResponse> result = json.write(retrieveDefaultImageIdResponse);

        String actual = json.write(retrieveDefaultImageIdResponse).getJson();

        assertThat(result)
                .extractingJsonPathStringValue("$.defaultImageId")
                .isEqualTo("qwe");
        assertEquals(actual, objectMapper.writeValueAsString(retrieveDefaultImageIdResponse));
    }
}