package com.rugid.multimediaservice.domain.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    IMAGE("images"),
    VIDEO("videos");

    private final String folderName;
}