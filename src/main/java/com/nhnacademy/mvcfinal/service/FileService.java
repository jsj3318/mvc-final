package com.nhnacademy.mvcfinal.service;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    public List<MultipartFile> cleanEmptyFiles(List<MultipartFile> images){
        return images.stream()
                .filter(image -> !image.isEmpty()) // 빈 파일 제외
                .collect(Collectors.toList());
    }

    public void deleteDir(Path path) throws IOException {
        if(Files.exists(path)) {
            // 디렉토리 하위의 모든 파일 삭제
            Stream<Path> paths = Files.walk(path);
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                // 디렉토리 삭제
                Files.delete(path);
        }
    }

    public void createDir(Path path) throws IOException {
        // 새로 생성
        Files.createDirectories(path);
    }

    public void uploadInquiryImages(String path, List<MultipartFile> images, Inquiry inquiry) throws IOException {
        for(int i=0; i<images.size(); i++) {
            MultipartFile image = images.get(i);
            // 이미지가 유효한지 체크
            if (isValidImage(image)) {
                // 파일 확장자
                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                // 문의 id와 사진의 인덱스를 이용해서 새로운 이름 짓기
                String name = "inquiry-" + inquiry.getId() + "_" + (i + 1) + fileExtension;
                Path filePath = Paths.get(path + name);
                // 서버측으로 파일 업로드
                image.transferTo(filePath);
                // 문의 객체에 파일 경로 저장
                inquiry.addImage(filePath);
            } else {
                throw new IllegalArgumentException("잘못된 파일입니다!");
            }
        }
    }

    // 파일이 알맞은 이미지 형식인지 검사
    private boolean isValidImage(MultipartFile image) {
        String filename = image.getOriginalFilename();
        String fileExtension = filename != null ? filename.substring(filename.lastIndexOf(".")) : "";

        return fileExtension.equalsIgnoreCase(".jpg") ||
                fileExtension.equalsIgnoreCase(".jpeg") ||
                fileExtension.equalsIgnoreCase(".png") ||
                fileExtension.equalsIgnoreCase(".gif");
    }

}
