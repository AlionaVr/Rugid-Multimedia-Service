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
class UploadVideoResponseTest {
    @Autowired
    private JacksonTester<UploadVideoResponse> json;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUploadVideoResponseDto() throws IOException {
        UploadVideoResponse uploadVideoResponse = new UploadVideoResponse("qwe");
        JsonContent<UploadVideoResponse> result = json.write(uploadVideoResponse);

        String actual = json.write(uploadVideoResponse).getJson();

        assertThat(result)
                .extractingJsonPathStringValue("$.videoId")
                .isEqualTo("qwe");
        assertEquals(actual, objectMapper.writeValueAsString(uploadVideoResponse));
    }
}