package com.nhnacademy.mvcfinal.service;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
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

    public void deleteDirContents(Path path) throws IOException {
        if (Files.exists(path)) {
            // 디렉토리 하위의 모든 파일 및 디렉토리 삭제 (하위 디렉토리부터 삭제)
            try (Stream<Path> paths = Files.walk(path)) {
                // 하위 디렉토리부터 삭제되도록 정렬
                paths
                        .filter(file -> !file.equals(path)) // 자기 자신 디렉토리는 제외
                        .sorted(Comparator.reverseOrder())  // 역순으로 정렬하여 하위 디렉토리부터 삭제
                        .forEach(file -> {
                            try {
                                if (Files.isDirectory(file)) {
                                    // 디렉토리인 경우, 비어있는 디렉토리 삭제
                                    Files.delete(file);
                                } else if (Files.isRegularFile(file)) {
                                    // 파일인 경우 파일 삭제
                                    Files.delete(file);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("파일 또는 디렉토리 삭제 중 오류 발생: " + file, e);
                            }
                        });
            }

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
                inquiry.addImage(inquiry.getId() + "/" + name);
            } else {
                throw new IllegalArgumentException("잘못된 파일입니다!");
            }
        }
    }

    // 파일이 알맞은 이미지 형식인지 검사
    public boolean isValidImage(MultipartFile image) {
        String filename = image.getOriginalFilename();
        if(filename == null || filename.isEmpty()) {
            return false;
        }

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return false;  // 확장자가 없으면 false 반환
        }

        String fileExtension = filename.substring(lastDotIndex);

        return fileExtension.equalsIgnoreCase(".jpg") ||
                fileExtension.equalsIgnoreCase(".jpeg") ||
                fileExtension.equalsIgnoreCase(".png") ||
                fileExtension.equalsIgnoreCase(".gif");
    }

}
