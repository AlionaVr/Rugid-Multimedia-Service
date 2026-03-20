package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetDefaultFileUrlService implements GetDefaultFileUrlUseCase {

    private static final String defaultImageId = "default.jpg";
    private static final String defaultVideoId = "default.mp4";

    @Override
    public String getDefaultImageId() {
        return defaultImageId;
    }

    @Override
    public String getDefaultVideoId() {
        return defaultVideoId;
    }
}
