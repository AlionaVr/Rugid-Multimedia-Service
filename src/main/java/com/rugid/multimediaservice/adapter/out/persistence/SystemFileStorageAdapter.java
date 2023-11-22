package com.rugid.multimediaservice.adapter.out.persistence;


import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;


import java.nio.file.Path;

@Component
public class SystemFileStorageAdapter implements FileOutputPort {

    private final Path storageFolder;

    public SystemFileStorageAdapter(@Value("${rugid.images.folder-path}") String storageFolder) {
        this.storageFolder = Path.of(storageFolder);
    }

    @Override
    public String upload(byte[] data, String extension) {
        return null;
    }

    @Override
    public InputStreamResource download(String fileId) {
        return null;
    }

    @Override
    public void delete(String imageId) {
    }

    private byte[] getFileData(Path filePath) {
        return null;
    }

    private void trySaveFile(Path filePath, byte[] data) {

    }

    private void tryDeleteFile(String filePath) {

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
