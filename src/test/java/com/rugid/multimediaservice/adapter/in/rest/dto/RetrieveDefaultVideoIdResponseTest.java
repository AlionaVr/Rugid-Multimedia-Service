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
class RetrieveDefaultVideoIdResponseTest {
    @Autowired
    private JacksonTester<RetrieveDefaultVideoIdResponse> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRetrieveDefaultVideoIdResponseDto() throws IOException {

        RetrieveDefaultVideoIdResponse response = new RetrieveDefaultVideoIdResponse("qwe");

        JsonContent<RetrieveDefaultVideoIdResponse> result = json.write(response);

        String actual = json.write(response).getJson();

        assertThat(result)
                .extractingJsonPathStringValue("$.defaultVideoId")
                .isEqualTo("qwe");
        assertEquals(actual, objectMapper.writeValueAsString(response));

    }
}