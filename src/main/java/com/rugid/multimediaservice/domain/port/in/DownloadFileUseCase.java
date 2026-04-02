package com.rugid.multimediaservice.domain.port.in;

import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.model.FileType;

public interface DownloadFileUseCase {

    FileResource download(String fileId, FileType fileType);
}
