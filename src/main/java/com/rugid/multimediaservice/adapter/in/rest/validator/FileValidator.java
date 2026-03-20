package com.rugid.multimediaservice.adapter.in.rest.validator;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FileValidator {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "mov", "avi", "mkv");

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024;

    public void validateImage(byte[] data, String extension) {
        validateCommon(data, extension);

        if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported image extension: " + extension);
        }

        if (data.length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image too large");
        }
    }

    public void validateVideo(byte[] data, String extension) {
        validateCommon(data, extension);

        if (!VIDEO_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported video extension: " + extension);
        }

        if (data.length > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("Video too large");
        }
    }

    private void validateCommon(byte[] data, String extension) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("File is empty");
        }

        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException("Extension is missing");
        }
    }

    public void validateFileId(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("File ID is empty");
        }
        if (fileId.contains("..") || fileId.contains("/") || fileId.contains("\\")) {
            throw new IllegalArgumentException("Invalid file ID");
        }
        if (!fileId.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("Invalid file ID format");
        }
    }
}