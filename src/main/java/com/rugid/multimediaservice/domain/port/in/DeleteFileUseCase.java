package com.rugid.multimediaservice.domain.port.in;

import java.net.URL;

public interface DeleteFileUseCase {

    void delete(DeleteFileCommand deleteFileCommand);

    record DeleteFileCommand(

            String fileId
    ) {
    }
}
