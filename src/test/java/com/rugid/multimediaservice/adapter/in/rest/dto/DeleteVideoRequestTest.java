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
class DeleteVideoRequestTest {
    @Autowired
    private JacksonTester<DeleteVideoRequest> json;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDeleteVideoRequestDto() throws IOException {
        DeleteVideoRequest deleteVideoRequest = new DeleteVideoRequest("qwe");

        JsonContent<DeleteVideoRequest> result = json.write(deleteVideoRequest);

        String actual = json.write(deleteVideoRequest).getJson();

        assertThat(result)
                .extractingJsonPathStringValue("$.videoId")
                .isEqualTo("qwe");
        assertEquals(actual, objectMapper.writeValueAsString(deleteVideoRequest));
    }
}