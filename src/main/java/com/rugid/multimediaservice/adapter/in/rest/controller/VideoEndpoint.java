package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.adapter.in.exception.FileReadBytesException;
import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultVideoIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.port.in.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/video")
public class VideoEndpoint {

    private final GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final DownloadFileUseCase downloadFileUseCase;
    private final JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator;
    private final JsonDtoValidator<UploadVideoRequest> uploadVideoRequestValidator;

    public VideoEndpoint(
            GetDefaultFileUrlUseCase getDefaultFileUrlUseCase,
            UploadFileUseCase uploadFileUseCase,
            DeleteFileUseCase deleteFileUseCase,
            DownloadFileUseCase downloadFileUseCase,
            JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator,
            JsonDtoValidator<UploadVideoRequest> uploadVideoRequestValidator
    ) {
        this.getDefaultFileUrlUseCase = getDefaultFileUrlUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.deleteFileUseCase = deleteFileUseCase;
        this.downloadFileUseCase = downloadFileUseCase;
        this.deleteVideoRequestValidator = deleteVideoRequestValidator;
        this.uploadVideoRequestValidator = uploadVideoRequestValidator;
    }

    @GetMapping("/default")
    public ResponseEntity<RetrieveDefaultVideoIdResponse> getDefaultVideoId() {
        String defaultVideoId = getDefaultFileUrlUseCase.getDefaultVideoId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultVideoIdResponse(defaultVideoId));
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> downloadVideo(@RequestParam(name = "videoId") String videoId) {
        InputStreamResource video = downloadFileUseCase.download(videoId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoId + "\"")
                .body(video);
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadVideoResponse> uploadVideo(@ModelAttribute("request") UploadVideoRequest uploadVideoRequest) {
        uploadVideoRequestValidator.validate(uploadVideoRequest);

        UploadFileUseCase.UploadFileCommand uploadFileCommand = createUploadVideoCommand(uploadVideoRequest);
        String videoId = uploadFileUseCase.upload(uploadFileCommand);

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

    private UploadFileUseCase.UploadFileCommand createUploadVideoCommand(UploadVideoRequest request) {
        MultipartFile video = request.video();

        byte[] videoAsBytes;
        try {
            videoAsBytes = video.getBytes();
        } catch (Exception e) {
            throw new FileReadBytesException();
        }

        String fileExtension = FilenameUtils.getExtension(video.getOriginalFilename());

        return new UploadFileUseCase.UploadFileCommand(
                videoAsBytes,
                fileExtension,
                FileType.VIDEO
        );
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteVideoCommand(DeleteVideoRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.videoId());
    }
}
