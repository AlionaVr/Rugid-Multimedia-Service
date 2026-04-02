package com.rugid.multimediaservice.domain.port.out;

import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.model.FileType;

import java.io.InputStream;

public interface FileOutputPort {

    String upload(InputStream inputStream, String extension, String contentType, FileType fileType);

    FileResource download(String fileId, FileType fileType);

    void delete(String imageId, FileType fileType);
}
