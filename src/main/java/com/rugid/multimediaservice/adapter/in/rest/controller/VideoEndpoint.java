package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.adapter.in.rest.dto.*;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.core.exception.IORuntimeException;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/video")
public class VideoEndpoint {

    private final GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator;

    public VideoEndpoint(
            GetDefaultFileUrlUseCase getDefaultFileUrlUseCase,
            UploadFileUseCase uploadFileUseCase,
            DeleteFileUseCase deleteFileUseCase,
            JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator
    ) {
        this.getDefaultFileUrlUseCase = getDefaultFileUrlUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.deleteFileUseCase = deleteFileUseCase;
        this.deleteVideoRequestValidator = deleteVideoRequestValidator;
    }

    @GetMapping("/default")
    public ResponseEntity<RetrieveDefaultVideoIdResponse> getDefaultVideoId() {
        String defaultVideoId = getDefaultFileUrlUseCase.getDefaultVideoId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultVideoIdResponse(defaultVideoId));
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadVideoResponse> uploadImage(@ModelAttribute("request") UploadVideoRequest uploadVideoRequest) {
        UploadFileUseCase.UploadFileCommand uploadFileCommand = createUploadImageCommand(uploadVideoRequest);
        String videoId = uploadFileUseCase.uploadImage(uploadFileCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UploadVideoResponse(videoId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVideo(@RequestBody DeleteVideoRequest request) {
        deleteVideoRequestValidator.validate(request);

        DeleteFileUseCase.DeleteFileCommand deleteVideoCommand = createDeleteVideoCommand(request);
        deleteFileUseCase.delete(deleteVideoCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private UploadFileUseCase.UploadFileCommand createUploadImageCommand(UploadVideoRequest request) {
        MultipartFile video = request.video();

        byte[] videoAsBytes;
        try {
            videoAsBytes = video.getBytes();
        } catch (IOException e) {
            throw new IORuntimeException("Could not read video data", e);
        }

        String fileExtension = FilenameUtils.getExtension(video.getOriginalFilename());

        return new UploadFileUseCase.UploadFileCommand(
                videoAsBytes,
                fileExtension
        );
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteVideoCommand(DeleteVideoRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.videoId());
    }
}
