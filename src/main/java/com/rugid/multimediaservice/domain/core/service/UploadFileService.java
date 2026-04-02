package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.core.validator.FileValidator;
import com.rugid.multimediaservice.domain.core.model.FileType;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {

    private final FileOutputPort fileOutputPort;
    private final FileValidator fileValidator;

    @Override
    public String upload(UploadFileCommand uploadFileCommand) {
        if (uploadFileCommand.fileType() == FileType.IMAGE) {
            fileValidator.validateImage(uploadFileCommand.size(), uploadFileCommand.extension());
        } else if (uploadFileCommand.fileType() == FileType.VIDEO) {
            fileValidator.validateVideo(uploadFileCommand.size(), uploadFileCommand.extension());
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }
        return fileOutputPort.upload(uploadFileCommand.inputStream(),
                uploadFileCommand.extension(),
                uploadFileCommand.contentType(),
                uploadFileCommand.fileType());
    }
}
