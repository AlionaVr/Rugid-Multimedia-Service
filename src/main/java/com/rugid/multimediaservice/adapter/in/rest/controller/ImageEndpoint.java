package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultImageIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.core.exception.IORuntimeException;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageEndpoint {

    private final DownloadFileUseCase downloadFileUseCase;
    private final GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final JsonDtoValidator<UploadImageRequest> uploadImageRequestValidator;
    private final JsonDtoValidator<DeleteImageRequest> deleteImageRequestValidator;

    public ImageEndpoint(
            DownloadFileUseCase downloadFileUseCase,
            GetDefaultFileUrlUseCase getDefaultFileUrlUseCase,
            UploadFileUseCase uploadFileUseCase,
            DeleteFileUseCase deleteFileUseCase,
            JsonDtoValidator<UploadImageRequest> uploadImageRequestValidator,
            JsonDtoValidator<DeleteImageRequest> deleteImageRequestValidator
    ) {
        this.downloadFileUseCase = downloadFileUseCase;
        this.getDefaultFileUrlUseCase = getDefaultFileUrlUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.deleteFileUseCase = deleteFileUseCase;
        this.uploadImageRequestValidator = uploadImageRequestValidator;
        this.deleteImageRequestValidator = deleteImageRequestValidator;
    }

    @GetMapping
    public ResponseEntity<Resource> downloadImage(@RequestParam(name = "imageId") String imageId) {
        InputStreamResource image = downloadFileUseCase.download(imageId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @GetMapping("/default")
    public ResponseEntity<RetrieveDefaultImageIdResponse> getDefaultImageId() {
        String defaultImageId = getDefaultFileUrlUseCase.getDefaultImageId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultImageIdResponse(defaultImageId));
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadImageResponse> uploadImage(@ModelAttribute UploadImageRequest request) {
        uploadImageRequestValidator.validate(request);

        UploadFileUseCase.UploadFileCommand uploadFileCommand = createUploadImageCommand(request);
        String imageId = uploadFileUseCase.uploadImage(uploadFileCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UploadImageResponse(imageId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestBody DeleteImageRequest request) {
        deleteImageRequestValidator.validate(request);

        DeleteFileUseCase.DeleteFileCommand deleteImageCommand = createDeleteImageCommand(request);
        deleteFileUseCase.delete(deleteImageCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private UploadFileUseCase.UploadFileCommand createUploadImageCommand(UploadImageRequest request) {
        MultipartFile image = request.image();

        byte[] imageAsBytes;
        try {
            imageAsBytes = image.getBytes();
        } catch (IOException e) {
            throw new IORuntimeException("Could not read image data", e);
        }

        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());

        return new UploadFileUseCase.UploadFileCommand(
                imageAsBytes,
                fileExtension
        );
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteImageCommand(DeleteImageRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.imageId());
    }
}
