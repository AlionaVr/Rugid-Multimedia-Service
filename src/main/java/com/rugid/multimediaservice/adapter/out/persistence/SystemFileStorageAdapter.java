package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.IORuntimeException;
import com.rugid.multimediaservice.domain.core.exception.NoSuchFileRuntimeException;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

@Component
public class SystemFileStorageAdapter implements FileOutputPort {

    private final Path storageFolder;

    public SystemFileStorageAdapter(@Value("${rugid.images.folder-path}") String storageFolder) {
        this.storageFolder = Path.of(storageFolder);
    }

    @Override
    public String upload(byte[] data, String extension) {
        String generatedFilename = generateFilename(data);
        Path generatedFilePath = generateFilePath(generatedFilename, extension);

        trySaveFile(generatedFilePath, data);

        return generateFileId(generatedFilePath);
    }

    @Override
    public InputStreamResource download(String fileId) {
        Path fullFilepath = storageFolder.resolve(fileId);
        byte[] fileData = getFileData(fullFilepath);

        return new InputStreamResource(new ByteArrayInputStream(fileData));
    }

    @Override
    public void delete(String imageId) {
        tryDeleteFile(imageId);
    }

    private byte[] getFileData(Path filePath) {
        byte[] fileData;
        try {
            fileData = Files.readAllBytes(filePath);
        } catch (NoSuchFileException e) {
            throw new NoSuchFileRuntimeException("Could not find file", e);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }

        return fileData;
    }

    private void trySaveFile(Path filePath, byte[] data) {
        try {
            Files.write(storageFolder.resolve(filePath), data);
        } catch (IOException e) {
            throw new IORuntimeException("Could not save file", e);
        }
    }

    private void tryDeleteFile(String filePath) {
        try {
            Files.delete(storageFolder.resolve(filePath));
        } catch (NoSuchFileException e) {
            throw new IORuntimeException("Could not find file", e);
        } catch (IOException e) {
            throw new IORuntimeException("Could not delete file", e);
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
