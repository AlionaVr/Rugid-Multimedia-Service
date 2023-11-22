package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class UploadFileService implements UploadFileUseCase {

    private final FileOutputPort fileOutputPort;

    public UploadFileService(FileOutputPort fileOutputPort) {
        this.fileOutputPort = fileOutputPort;
    }

    @Override
    public String uploadImage(UploadFileCommand uploadFileCommand) {
        return null;
    }
}
