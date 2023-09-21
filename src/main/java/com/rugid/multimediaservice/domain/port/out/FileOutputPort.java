package com.rugid.multimediaservice.domain.port.out;

import org.springframework.core.io.InputStreamResource;

import java.net.URL;

public interface FileOutputPort {

    String upload(byte[] data, String extension);

    InputStreamResource download(String fileId);

    void delete(String imageId);
}
