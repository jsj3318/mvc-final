package com.nhnacademy.mvcfinal.service;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {
    private FileService fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @Test
    void cleanEmptyFiles() {
        MockMultipartFile validFile = new MockMultipartFile("images", "test.png", "image/png", new byte[]{1, 2, 3});
        MockMultipartFile emptyFile = new MockMultipartFile("images", "empty.jpg", "image/png", new byte[]{});

        List<MultipartFile> filteredFiles = fileService.cleanEmptyFiles(Arrays.asList(validFile, emptyFile));

        assertThat(filteredFiles).containsOnly(validFile);
    }

    @Test
    void deleteDirContents() throws IOException {
        // 내 서비스의 이 함수는 자기 자신 디렉토리는 지우지 않고 내용물만 다 지움

        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(subDir.resolve("file2.txt"));

        fileService.deleteDirContents(tempDir);

        assertTrue(Files.exists(tempDir));
        assertFalse(Files.exists(subDir));
        assertFalse(Files.exists(file1));
        assertFalse(Files.exists(file2));
    }

    @Test
    void createDir() throws IOException {
        Path newDir = tempDir.resolve("newDir");

        fileService.createDir(newDir);

        assertThat(Files.exists(newDir)).isTrue();
        assertThat(Files.isDirectory(newDir)).isTrue();
    }

    @Test
    void uploadInquiryImages() throws IOException {
        Inquiry inquiry = new Inquiry("Title", "Content", "userId", null);
        inquiry.setId(1);

        MockMultipartFile imageFile = new MockMultipartFile("images", "test.jpg", "image/jpg", new byte[]{1, 2, 3});
        MockMultipartFile imageFile2 = new MockMultipartFile("images", "test2.jpeg", "image/jpeg", new byte[]{1, 2, 3});
        MockMultipartFile imageFile3 = new MockMultipartFile("images", "test3.png", "image/png", new byte[]{1, 2, 3});
        String uploadPath = tempDir.toString() + "/";

        fileService.uploadInquiryImages(uploadPath, List.of(imageFile, imageFile2, imageFile3), inquiry);

        Path uploadedFile = tempDir.resolve("inquiry-1_1.jpg");
        assertThat(Files.exists(uploadedFile)).isTrue();
        assertThat(inquiry.getImages()).contains("1/inquiry-1_1.jpg");
    }

    @Test
    void uploadInquiryImages_invalidImage() {
        Inquiry inquiry = new Inquiry("Title", "Content", "userId", null);
        inquiry.setId(1);

        MockMultipartFile invalidFile = new MockMultipartFile("images", "test.txt", "text/plain", new byte[]{1, 2, 3});
        List<MultipartFile> images = List.of(invalidFile);
        String uploadPath = tempDir.toString() + "/";

        assertThrows(IllegalArgumentException.class, () -> {
            fileService.uploadInquiryImages(uploadPath, images, inquiry);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "null", "", "test" })  // null, empty, no extension cases
    void isValidImage_withDifferentFilenames(String filename) {
        // filename이 null인 경우, 빈 문자열인 경우, 확장자가 없는 경우
        MockMultipartFile imageFile;

        if ("null".equals(filename)) {
            imageFile = new MockMultipartFile("images", null, "image/jpg", new byte[]{1, 2, 3});
        } else {
            imageFile = new MockMultipartFile("images", filename, "image/jpg", new byte[]{1, 2, 3});
        }

        boolean result = fileService.isValidImage(imageFile);

        // filename이 null, 빈 문자열, 확장자가 없으면 false여야 한다.
        assertThat(result).isFalse();
    }


}