package com.rugid.multimediaservice.domain.port.in;

import java.net.URL;

public interface UploadFileUseCase {

    String uploadImage(UploadFileCommand uploadFileCommand);

    record UploadFileCommand(

            byte[] imageData,

            String extension
    ) {
    }
}
