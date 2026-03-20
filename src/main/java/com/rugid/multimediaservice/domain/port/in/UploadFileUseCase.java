package com.rugid.multimediaservice.domain.port.in;

public interface UploadFileUseCase {

    String upload(UploadFileCommand uploadFileCommand);

    record UploadFileCommand(

            byte[] data,

            String extension,

            FileType fileType

    ) {
    }
}
