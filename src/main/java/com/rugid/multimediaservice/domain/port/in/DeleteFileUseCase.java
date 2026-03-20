package com.rugid.multimediaservice.domain.port.in;

public interface DeleteFileUseCase {

    void delete(DeleteFileCommand deleteFileCommand);

    record DeleteFileCommand(String fileId) {
    }
}
