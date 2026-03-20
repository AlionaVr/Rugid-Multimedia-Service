package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.adapter.in.exception.FileNotFoundException;
import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SystemFileStorageAdapterTest {

    @TempDir
    Path tempDir;

    private SystemFileStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        FileValidator fileValidator = mock(FileValidator.class);
        adapter = new SystemFileStorageAdapter(tempDir.toString(), fileValidator);
        adapter.init();
    }

    @Test
    void upload_shouldSaveFile_andReturnFileId() throws IOException {
        byte[] data = "hello".getBytes();

        String fileId = adapter.upload(data, "txt");

        Path savedFile = tempDir.resolve(fileId);

        assertTrue(Files.exists(savedFile));
        assertArrayEquals(data, Files.readAllBytes(savedFile));
    }

    @Test
    void download_shouldReturnFileContent() throws IOException {
        byte[] data = "test-data".getBytes();
        String fileId = adapter.upload(data, "txt");

        InputStreamResource resource = adapter.download(fileId);

        byte[] result = resource.getInputStream().readAllBytes();

        assertArrayEquals(data, result);
    }

    @Test
    void download_shouldThrow_whenFileNotFound() {
        assertThrows(FileNotFoundException.class,
                () -> adapter.download("non-existent.txt"));
    }

    @Test
    void delete_shouldRemoveFile() throws IOException {
        byte[] data = "delete-me".getBytes();
        String fileId = adapter.upload(data, "txt");

        adapter.delete(fileId);

        assertFalse(Files.exists(tempDir.resolve(fileId)));
    }

    @Test
    void delete_shouldThrow_whenFileNotFound() {
        assertThrows(FileNotFoundException.class,
                () -> adapter.delete("missing.txt"));
    }
}