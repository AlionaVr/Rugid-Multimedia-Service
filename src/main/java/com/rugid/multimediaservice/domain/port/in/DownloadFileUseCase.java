package com.rugid.multimediaservice.domain.port.in;

import org.springframework.core.io.InputStreamResource;

import java.net.URL;

public interface DownloadFileUseCase {

    InputStreamResource download(String fileId);
}
