package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultVideoIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VideoEndpoint.class)
class VideoEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    @MockBean
    private UploadFileUseCase uploadFileUseCase;
    @MockBean
    private DeleteFileUseCase deleteFileUseCase;
    @MockBean
    private JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator;

    @Test
    void testGetDefaultVideoId_whenValidData() throws Exception {
        String defaultImage = "default.mp4";
        ResponseEntity<RetrieveDefaultVideoIdResponse> expected = ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultVideoIdResponse(defaultImage));
        String jsonResult = objectMapper.writeValueAsString(expected.getBody());
        when(getDefaultFileUrlUseCase.getDefaultVideoId()).thenReturn(defaultImage);

        String result = mockMvc.perform(get("/video/default")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertEquals(jsonResult, result);
        verify(getDefaultFileUrlUseCase, times(1)).getDefaultVideoId();
    }

    @Test
    void testUploadVideo_whenValidData() throws Exception {
        var imageId = "testing";
        var expectedResult = objectMapper.writeValueAsString(new UploadVideoResponse(imageId));
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "This is a test file".getBytes());
        UploadVideoRequest request = new UploadVideoRequest(mockFile);
        String fileExtension = FilenameUtils.getExtension(request.video().getOriginalFilename());
        UploadFileUseCase.UploadFileCommand uploadFileCommand = new UploadFileUseCase
                .UploadFileCommand(request.video().getBytes(), fileExtension);
        when(uploadFileUseCase.uploadImage(uploadFileCommand)).thenReturn(imageId);
        String result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/video")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("request", request))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectedResult, result);
        verify(uploadFileUseCase, times(1)).uploadImage(uploadFileCommand);
    }

    @Test
    void testDeleteVideo_whenValidData() throws Exception {
        DeleteVideoRequest dto = new DeleteVideoRequest("test");
        DeleteFileUseCase.DeleteFileCommand deleteFileCommand = new DeleteFileUseCase.DeleteFileCommand(dto.videoId());

        var result = mockMvc.perform(delete("/video")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        verify(deleteFileUseCase, times(1)).delete(deleteFileCommand);
    }
}