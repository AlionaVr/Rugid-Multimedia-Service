package com.rugid.multimediaservice.service;

import com.rugid.multimediaservice.domain.core.service.DownloadFileService;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadFileServiceTest {

    @Mock
    private FileOutputPort fileOutputPort;

    @InjectMocks
    private DownloadFileService downloadFileService;

    @Test
    void download_whenFileIdCorrect_thenReturnedDownloadFile() {
        var fileId = "123";
        fileOutputPort = mock(FileOutputPort.class);
        InputStreamResource expectedResource = new InputStreamResource(new ByteArrayInputStream("Hello".getBytes()));
        downloadFileService = new DownloadFileService(fileOutputPort);
        when(fileOutputPort.download(fileId)).thenReturn(expectedResource);

        InputStreamResource result = downloadFileService.download(fileId);


        verify(fileOutputPort, times(1)).download(fileId);
        assertEquals(expectedResource, result);
    }

    @Test
    void testDownloadFileNotFound() {
        String fileId = "nonExistentFileId";
        when(fileOutputPort.download(fileId)).thenReturn(null);

        InputStreamResource result = downloadFileService.download(fileId);

        verify(fileOutputPort, times(1)).download(fileId);
        assertNull(result);
    }
}