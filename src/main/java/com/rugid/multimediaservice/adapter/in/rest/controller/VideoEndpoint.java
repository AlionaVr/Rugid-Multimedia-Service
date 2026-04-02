package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.domain.core.exception.FileUploadReadException;
import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultVideoIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.model.FileType;
import com.rugid.multimediaservice.domain.port.in.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
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
    public ResponseEntity<Resource> downloadVideo(@RequestParam(name = "videoId") String videoId) {
        FileResource video = downloadFileUseCase.download(videoId, FileType.VIDEO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(video.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoId + "\"")
                .body(video.resource());
    }

    @PostMapping(consumes = "multipart/form-data")
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

        try {
            String fileExtension = FilenameUtils.getExtension(video.getOriginalFilename());
            String contentType = video.getContentType() != null
                    ? video.getContentType()
                    : "application/octet-stream";

            return new UploadFileUseCase.UploadFileCommand(
                    video.getInputStream(),
                    video.getSize(),
                    fileExtension,
                    contentType,
                    FileType.VIDEO
            );
        } catch (Exception e) {
            throw new FileUploadReadException(e);
        }
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteVideoCommand(DeleteVideoRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.videoId(), FileType.VIDEO);
    }
}
