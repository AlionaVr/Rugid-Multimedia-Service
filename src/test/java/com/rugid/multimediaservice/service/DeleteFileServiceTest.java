package com.rugid.multimediaservice.service;

import com.rugid.multimediaservice.domain.core.service.DeleteFileService;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteFileServiceTest {

    @Mock
    private FileOutputPort fileOutputPort;

    @InjectMocks
    private DeleteFileService deleteFileService;

    @Test
    void delete_whenFieldIdPassed_thenDeletedFile() {
        fileOutputPort = mock(FileOutputPort.class);
        deleteFileService = new DeleteFileService(fileOutputPort);
        var fileId = "123";

        deleteFileService.delete(new DeleteFileUseCase.DeleteFileCommand(fileId));

        verify(fileOutputPort).delete(fileId);
    }

}