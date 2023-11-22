package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
public class DownloadFileService implements DownloadFileUseCase {

    private final FileOutputPort fileOutputPort;

    public DownloadFileService(FileOutputPort fileOutputPort) {
        this.fileOutputPort = fileOutputPort;
    }

    @Override
    public InputStreamResource download(String fileId) {
        return null;
    }
}
