package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.adapter.in.exception.FileDeleteException;
import com.rugid.multimediaservice.adapter.in.exception.FileNotFoundException;
import com.rugid.multimediaservice.adapter.in.exception.FileReadBytesException;
import com.rugid.multimediaservice.adapter.in.exception.FileSaveException;
import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class SystemFileStorageAdapter implements FileOutputPort {

    private final Path storageFolder;
    private final FileValidator fileValidator;

    public SystemFileStorageAdapter(@Value("${rugid.images.folder-path}") String storageFolder, FileValidator fileValidator) {
        this.storageFolder = Path.of(storageFolder);
        this.fileValidator = fileValidator;
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(storageFolder)) {
                Files.createDirectories(storageFolder);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize storage folder", e);
        }
    }

    @Override
    public String upload(byte[] data, String extension) {
        String filename = generateFilename(data);
        Path filePath = storageFolder.resolve(generateFilePath(filename, extension));

        trySaveFile(filePath, data);
        return generateFileId(filePath);

    }

    @Override
    public InputStreamResource download(String fileId) {
        Path filePath = storageFolder.resolve(fileId);
        byte[] fileData = getFileData(filePath);
        return new InputStreamResource(new ByteArrayInputStream(fileData));
    }

    @Override
    public void delete(String fileId) {
        Path filePath = storageFolder.resolve(fileId);
        tryDeleteFile(filePath, fileId);
    }

    private byte[] getFileData(Path filePath) {
        try {
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException();
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileReadBytesException();
        }
    }


    private void trySaveFile(Path filePath, byte[] data) {
        try {
            Files.write(filePath, data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new FileSaveException(filePath.getFileName().toString(), e);
        }
    }

    private void tryDeleteFile(Path filePath, String fileId) {
        try {
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException();
            }
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileDeleteException(fileId, e);
        }
    }

    private String generateFileId(Path filename) {
        return filename.getFileName().toString();
    }

    private Path generateFilePath(String filename, String extension) {
        return Path.of(filename + "." + extension);
    }

    private String generateFilename(byte[] fileData) {
        return generateFileHash(fileData);
    }

    private String generateFileHash(byte[] file) {
        return DigestUtils.md5DigestAsHex(file);
    }
}
