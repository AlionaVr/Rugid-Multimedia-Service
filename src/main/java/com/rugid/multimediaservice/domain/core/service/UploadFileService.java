package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.in.FileType;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.stereotype.Service;

@Service
public class UploadFileService implements UploadFileUseCase {

    private final FileOutputPort fileOutputPort;
    FileValidator fileValidator;

    public UploadFileService(FileOutputPort fileOutputPort, FileValidator FileValidator) {
        this.fileOutputPort = fileOutputPort;
        this.fileValidator = FileValidator;
    }

    @Override
    public String upload(UploadFileCommand uploadFileCommand) {
        if (uploadFileCommand.fileType() == FileType.IMAGE) {
            fileValidator.validateImage(uploadFileCommand.data(), uploadFileCommand.extension());
        } else if (uploadFileCommand.fileType() == FileType.VIDEO) {
            fileValidator.validateVideo(uploadFileCommand.data(), uploadFileCommand.extension());
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }
        return fileOutputPort.upload(uploadFileCommand.data(), uploadFileCommand.extension());
    }
}
