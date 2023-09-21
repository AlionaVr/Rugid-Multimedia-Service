package com.rugid.multimediaservice.adapter.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record DeleteImageRequest(

        @NotEmpty(message = "field 'imageId' should not be empty")
        String imageId
) {
}
