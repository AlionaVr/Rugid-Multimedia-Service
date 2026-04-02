package com.rugid.multimediaservice.domain.core.model;

import org.springframework.core.io.FileSystemResource;

public record FileResource(
        FileSystemResource resource,
        String contentType
) {
}