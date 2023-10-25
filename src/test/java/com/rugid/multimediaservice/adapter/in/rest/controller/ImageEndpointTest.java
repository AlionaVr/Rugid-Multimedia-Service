package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultImageIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringJUnitWebConfig(ImageEndpoint.class)
class ImageEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DownloadFileUseCase downloadFileUseCase;
    @MockBean
    private GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    @MockBean
    private UploadFileUseCase uploadFileUseCase;
    @MockBean
    private DeleteFileUseCase deleteFileUseCase;
    @MockBean
    private JsonDtoValidator<UploadImageRequest> uploadImageRequestValidator;
    @MockBean
    private JsonDtoValidator<DeleteImageRequest> deleteImageRequestValidator;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDownloadImage_whenValidData() throws Exception {
        var imageId = "test";
        InputStreamResource image = new InputStreamResource(new ByteArrayInputStream(imageId.getBytes()));
        when(downloadFileUseCase.download(imageId)).thenReturn(image);

        String result = mockMvc.perform(get("/image", imageId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.valueOf("image/png"))
                        .accept(MediaType.valueOf("image/png"))
                        .param("imageId", imageId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(imageId, result);
        verify(downloadFileUseCase, times(1)).download(imageId);
    }

    @Test
    void testGetDefaultImageId_whenValidData() throws Exception {
        String defaultImage = "default.jpg";
        ResponseEntity<RetrieveDefaultImageIdResponse> expected = ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultImageIdResponse(defaultImage));
        String jsonResult = objectMapper.writeValueAsString(expected.getBody());
        when(getDefaultFileUrlUseCase.getDefaultImageId()).thenReturn(defaultImage);

        String result = mockMvc.perform(get("/image/default")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertEquals(jsonResult, result);
        verify(getDefaultFileUrlUseCase, times(1)).getDefaultImageId();
    }

    @Test
    void testUploadImage_whenValidData() throws Exception {
        var imageId = "testing";
        var expectedResult = objectMapper.writeValueAsString(new UploadImageResponse(imageId));
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "This is a test file".getBytes());
        UploadImageRequest request = new UploadImageRequest(mockFile);
        String fileExtension = FilenameUtils.getExtension(request.image().getOriginalFilename());
        UploadFileUseCase.UploadFileCommand uploadFileCommand = new UploadFileUseCase
                .UploadFileCommand(request.image().getBytes(), fileExtension);
        when(uploadFileUseCase.uploadImage(uploadFileCommand)).thenReturn(imageId);
        String result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/image")
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
    void testDeleteImage_whenValidData() throws Exception {
        DeleteImageRequest dto = new DeleteImageRequest("test");
        DeleteFileUseCase.DeleteFileCommand deleteFileCommand = new DeleteFileUseCase.DeleteFileCommand(dto.imageId());

        var result = mockMvc.perform(delete("/image")
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