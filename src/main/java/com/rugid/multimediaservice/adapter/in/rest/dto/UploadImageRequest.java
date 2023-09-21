package com.rugid.multimediaservice.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadImageRequest(

        @NotNull(message = "Image should not be null")
        MultipartFile image
) {
}
