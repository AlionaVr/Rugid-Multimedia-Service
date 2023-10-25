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
class UploadImageResponseTest {
    @Autowired
    private JacksonTester<UploadImageResponse> json;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUploadImageRequestTestDto() throws IOException {
        UploadImageResponse uploadImageRequest = new UploadImageResponse("qwe");
        JsonContent<UploadImageResponse> result = json.write(uploadImageRequest);

        String actual = json.write(uploadImageRequest).getJson();

        assertThat(result)
                .extractingJsonPathStringValue("$.imageId")
                .isEqualTo("qwe");
        assertEquals(actual, objectMapper.writeValueAsString(uploadImageRequest));
    }
}