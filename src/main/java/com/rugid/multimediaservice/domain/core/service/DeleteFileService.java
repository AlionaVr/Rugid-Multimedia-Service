package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteFileService implements DeleteFileUseCase {

    private final FileOutputPort fileOutputPort;

    public DeleteFileService(FileOutputPort fileOutputPort) {
        this.fileOutputPort = fileOutputPort;
    }

    @Override
    public void delete(DeleteFileCommand deleteFileCommand) {
        String fileId = deleteFileCommand.fileId();
        fileOutputPort.delete(fileId);
    }
}
