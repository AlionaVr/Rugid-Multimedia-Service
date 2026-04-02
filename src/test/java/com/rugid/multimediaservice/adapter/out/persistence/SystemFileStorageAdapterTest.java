package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.FileNotFoundException;
import com.rugid.multimediaservice.domain.core.model.FileResource;
import com.rugid.multimediaservice.domain.core.model.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SystemFileStorageAdapterTest {

    @TempDir
    Path tempDir;

    private SystemFileStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SystemFileStorageAdapter(tempDir.toString());
        adapter.init();
    }

    @Test
    void upload_shouldSaveFile_andReturnFileId() throws IOException {
        byte[] data = "hello".getBytes();

        String fileId = adapter.upload(new ByteArrayInputStream(data), "txt", "text/plain", FileType.IMAGE);

        Path savedFile = tempDir.resolve(FileType.IMAGE.getFolderName()).resolve(fileId);

        assertTrue(Files.exists(savedFile));
        assertArrayEquals(data, Files.readAllBytes(savedFile));
    }

    @Test
    void download_shouldReturnFileContent() throws IOException {
        byte[] data = "test-data".getBytes();
        String fileId = adapter.upload(new ByteArrayInputStream(data), "txt", "text/plain", FileType.IMAGE);

        FileResource resource = adapter.download(fileId, FileType.IMAGE);

        byte[] result = resource.resource().getInputStream().readAllBytes();

        assertArrayEquals(data, result);
    }

    @Test
    void download_shouldThrow_whenFileNotFound() {
        assertThrows(FileNotFoundException.class,
                () -> adapter.download("non-existent.txt", FileType.IMAGE));
    }

    @Test
    void delete_shouldRemoveFile() {
        byte[] data = "delete-me".getBytes();
        String fileId = adapter.upload(new ByteArrayInputStream(data), "txt", "text/plain", FileType.IMAGE);

        adapter.delete(fileId, FileType.IMAGE);

        assertFalse(Files.exists(tempDir.resolve(FileType.IMAGE.getFolderName()).resolve(fileId)));
    }

    @Test
    void delete_shouldThrow_whenFileNotFound() {
        assertThrows(FileNotFoundException.class,
                () -> adapter.delete("missing.txt", FileType.IMAGE));
    }
}
