package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.core.model.FileType;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadFileService implements DownloadFileUseCase {

    private final FileOutputPort fileOutputPort;
    private final FileValidator fileValidator;

    @Override
    public FileResource download(String fileId, FileType fileType) {
        fileValidator.validateFileId(fileId);
        return fileOutputPort.download(fileId, fileType);
    }
}
