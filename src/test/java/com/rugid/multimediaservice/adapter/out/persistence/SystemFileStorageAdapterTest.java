package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.IORuntimeException;
import com.rugid.multimediaservice.domain.core.exception.NoSuchFileRuntimeException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SystemFileStorageAdapterTest {
    private static final String storageFolderPath = "content";
    @Autowired
    private SystemFileStorageAdapter fileStorageAdapter;

    @BeforeEach
    void setUp() throws IOException {
        fileStorageAdapter = new SystemFileStorageAdapter(storageFolderPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.cleanDirectory(new File(storageFolderPath));
    }

    @Test
    void testUpload_whenSuccessfullyUploadsFile() {
        var testData = "Test".getBytes();
        var extension = "txt";

        var fileId = fileStorageAdapter.upload(testData, extension);

        assertNotNull(fileId);
    }

    @Test
    void testUpload_whenFolderNotExists_thenReturnedIOException() throws IOException {
        var testData = "Test data".getBytes();
        var extension = "txt";
        fileStorageAdapter = new SystemFileStorageAdapter("asd");

        var ex = assertThrows(IORuntimeException.class, () -> {
            fileStorageAdapter.upload(testData, extension);
        });
        assertEquals("Could not save file", ex.getMessage());
    }

    @Test
    void testUpload_CheckUniqueFileId() {
        var testData = "test data 1".getBytes();
        var testData1 = "test data 2".getBytes();
        var extension = "jpg";

        String fileId = fileStorageAdapter.upload(testData, extension);
        String fileId2 = fileStorageAdapter.upload(testData1, extension);

        assertNotEquals(fileId, fileId2);
    }

    @Test
    void testDownload_whenValidData_thenDownloadedFile() throws IOException {
        var testData = "test data".getBytes();
        var extension = "jpg";
        String fileId = fileStorageAdapter.upload(testData, extension);

        InputStreamResource resource = fileStorageAdapter.download(fileId);

        assertArrayEquals(resource.getContentAsByteArray(), testData);
    }

    @Test
    void testDownload_whenFileNotFound_thenReturnedThrow() {
        Throwable ex = assertThrows(NoSuchFileRuntimeException.class, () -> {
            fileStorageAdapter.download("null");
        });

        assertEquals("Could not find file", ex.getMessage());
    }


    @Test
    void testDelete_whenValidData_thenDeletedFile() {
        var testData = "test data".getBytes();
        var extension = "jpg";
        String fileId = fileStorageAdapter.upload(testData, extension);

        fileStorageAdapter.delete(fileId);
    }

    @Test
    void testDelete_whenFileNotFound_thenReturnedThrow() {
        Throwable ex = assertThrows(IORuntimeException.class, () -> {
            fileStorageAdapter.delete("null");
        });

        assertEquals("Could not find file", ex.getMessage());
    }
}