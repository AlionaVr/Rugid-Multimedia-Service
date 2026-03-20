package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
public class DownloadFileService implements DownloadFileUseCase {

    private final FileOutputPort fileOutputPort;
    private final FileValidator fileValidator;

    public DownloadFileService(FileOutputPort fileOutputPort, FileValidator fileValidator) {
        this.fileOutputPort = fileOutputPort;
        this.fileValidator = fileValidator;
    }

    @Override
    public InputStreamResource download(String fileId) {
        fileValidator.validateFileId(fileId);
        return fileOutputPort.download(fileId);
    }
}
