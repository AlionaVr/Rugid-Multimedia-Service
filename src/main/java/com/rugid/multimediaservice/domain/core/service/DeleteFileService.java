package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.core.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteFileService implements DeleteFileUseCase {

    private final FileOutputPort fileOutputPort;
    private final FileValidator fileValidator;

    @Override
    public void delete(DeleteFileCommand deleteFileCommand) {
        fileValidator.validateFileId(deleteFileCommand.fileId());
        fileOutputPort.delete(deleteFileCommand.fileId(), deleteFileCommand.fileType());
    }
}
