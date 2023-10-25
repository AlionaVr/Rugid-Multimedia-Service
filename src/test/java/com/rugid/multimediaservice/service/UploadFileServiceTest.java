package com.rugid.multimediaservice.service;

import com.rugid.multimediaservice.domain.core.service.UploadFileService;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadFileServiceTest {

    @Mock
    private FileOutputPort fileOutputPort;

    @InjectMocks
    private UploadFileService uploadFileService;

    @Test
    void testUploadImage_whenDataCorrect_thenUploadedImage() {
        fileOutputPort = mock(FileOutputPort.class);
        var uploadFileCommand = new UploadFileUseCase.UploadFileCommand("testData".getBytes(), "jpg");
        uploadFileService = new UploadFileService(fileOutputPort);
        when(fileOutputPort.upload(any(byte[].class), anyString())).thenReturn("uploadedFileUrl");

        String result = uploadFileService.uploadImage(uploadFileCommand);

        assertEquals("uploadedFileUrl", result);
        verify(fileOutputPort, times(1)).upload(any(byte[].class), anyString());
    }


    //Можно добавить негативный сценарий, но придется изменять бизнес логику и добавлять новые проверки.


//    @Test
//    void testUploadImage_whenDataNull_thenReturnedException() {
//        fileOutputPort = mock(FileOutputPort.class);
//        var uploadFileCommand = new UploadFileUseCase.UploadFileCommand(null, "jpg");
//        uploadFileService = new UploadFileService(fileOutputPort);
//
//        assertThrows(NullPointerException.class, () -> uploadFileService.uploadImage(uploadFileCommand));
//        verify(fileOutputPort, never()).upload(any(byte[].class), anyString());
//    }
}