package com.rugid.multimediaservice.domain.port.in;

import com.rugid.multimediaservice.domain.core.model.FileType;

public interface DeleteFileUseCase {

    void delete(DeleteFileCommand deleteFileCommand);

    record DeleteFileCommand(String fileId, FileType fileType) {
    }
}
