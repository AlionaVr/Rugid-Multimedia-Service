package com.rugid.multimediaservice.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadVideoRequest(

        @NotNull(message = "field 'video' should not be null")
        MultipartFile video
) {
}
