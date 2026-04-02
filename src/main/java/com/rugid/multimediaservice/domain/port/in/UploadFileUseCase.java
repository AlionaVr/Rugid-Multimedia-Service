package com.rugid.multimediaservice.domain.port.in;

import com.rugid.multimediaservice.domain.core.model.FileType;

import java.io.InputStream;

public interface UploadFileUseCase {

    String upload(UploadFileCommand uploadFileCommand);

    record UploadFileCommand(

            InputStream inputStream,

            long size,

            String extension,

            String contentType,

            FileType fileType

    ) {
    }
}
