package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.FileDeleteException;
import com.rugid.multimediaservice.domain.core.exception.FileNotFoundException;
import com.rugid.multimediaservice.domain.core.exception.FileSaveException;
import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.model.FileType;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Component
public class SystemFileStorageAdapter implements FileOutputPort {

    private final Path storageFolder;

    public SystemFileStorageAdapter(@Value("${rugid.storage.folder-path}") String storageFolder) {
        this.storageFolder = Path.of(storageFolder);
    }

    @PostConstruct
    public void init() {
        try {
            for (FileType fileType : FileType.values()) {
                Files.createDirectories(storageFolder.resolve(fileType.getFolderName()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize storage folders", e);
        }
    }

    @Override
    public String upload(InputStream inputStream, String extension, String contentType, FileType fileType) {
        String filename = UUID.randomUUID() + "." + extension;
        Path filePath = subfolderFor(fileType).resolve(filename);
        trySaveFile(filePath, inputStream);
        saveMeta(filePath, contentType);
        return filename;
    }

    @Override
    public FileResource download(String fileId, FileType fileType) {
        Path filePath = resolveAndValidatePath(fileId, fileType);
        ensureExists(filePath);
        return new FileResource(new FileSystemResource(filePath), readMeta(filePath));
    }

    @Override
    public void delete(String fileId, FileType fileType) {
        Path filePath = resolveAndValidatePath(fileId, fileType);
        tryDeleteFile(filePath, fileId);
    }

    private Path subfolderFor(FileType fileType) {
        return storageFolder.resolve(fileType.getFolderName());
    }

    private void saveMeta(Path filePath, String contentType) {
        Path metaPath = generateMetaPath(filePath);
        try {
            Files.writeString(metaPath, contentType,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new FileSaveException(metaPath.getFileName().toString(), e);
        }
    }

    private String readMeta(Path filePath) {
        try {
            return Files.readString(generateMetaPath(filePath));
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private Path generateMetaPath(Path filePath) {
        return Path.of(filePath + ".meta");
    }

    private void trySaveFile(Path filePath, InputStream inputStream) {
        try {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileSaveException(filePath.getFileName().toString(), e);
        }
    }

    private void tryDeleteFile(Path filePath, String fileId) {
        ensureExists(filePath);
        try {
            Files.deleteIfExists(generateMetaPath(filePath));
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileDeleteException(fileId, e);
        }
    }

    private void ensureExists(Path filePath) {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException();
        }
    }

    private Path resolveAndValidatePath(String fileId, FileType fileType) {
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("Invalid fileId");
        }

        Path subfolder = subfolderFor(fileType);
        Path resolved = subfolder.resolve(fileId).normalize();

        if (!resolved.startsWith(subfolder)) {
            throw new IllegalArgumentException("Path traversal attempt");
        }

        return resolved;
    }
}
