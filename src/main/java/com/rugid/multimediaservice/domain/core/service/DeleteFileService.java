package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteFileService implements DeleteFileUseCase {

    private final FileOutputPort fileOutputPort;
    FileValidator fileValidator;

    public DeleteFileService(FileOutputPort fileOutputPort, FileValidator fileValidator) {
        this.fileOutputPort = fileOutputPort;
        this.fileValidator = fileValidator;
    }

    @Override
    public void delete(DeleteFileCommand deleteFileCommand) {
        fileValidator.validateFileId(deleteFileCommand.fileId());
        fileOutputPort.delete(deleteFileCommand.fileId());
    }
}
