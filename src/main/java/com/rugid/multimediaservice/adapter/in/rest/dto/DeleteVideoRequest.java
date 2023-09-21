package com.rugid.multimediaservice.adapter.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record DeleteVideoRequest(

        @NotEmpty(message = "field 'videoId' should not be empty")
        String videoId
) {
}
