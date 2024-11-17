package com.nhnacademy.mvcfinal.config;

import com.nhnacademy.mvcfinal.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

@Component
public class ImageCleanService {
    // 어플리케이션이 실행될 때 이미 존재하는 이미지 파일들을 삭제하는 빈

    @Autowired
    private FileService fileService;

    @Value("${imagesPath}")
    private String imagesPath;

    @PostConstruct
    public void init() throws IOException {
        fileService.deleteDirContents(Paths.get(imagesPath));
    }

}
