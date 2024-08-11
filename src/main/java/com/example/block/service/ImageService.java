//package com.example.block.service;
//
//import com.example.block.domain.User;
//import com.example.block.global.apiPayload.code.status.ErrorStatus;
//import com.example.block.global.apiPayload.exception.GeneralException;
//import com.example.block.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class ImageService {
//    private final UserRepository userRepository;
//
//    @Value("${file.path}") //빈 등록 해야함 설정파일 수정하기
//    private String uploadFolder;
//
//    @PostConstruct
//    public void init() {
//        File uploadDir = new File(uploadFolder);
//        if (!uploadDir.exists()) {
//            boolean created = uploadDir.mkdirs();
//            if (!created) {
//                throw new RuntimeException("파일 업로드 디렉토리 생성 실패: " + uploadFolder);
//            }
//        }
//    }
//
//    @Transactional
//    public String uploadProfileImage(Integer userId, MultipartFile imageFile) {
//        UUID uuid = UUID.randomUUID();
//        String imageFileName = uuid + "_" + imageFile.getOriginalFilename();
//        Path imageFilePath = Paths.get(uploadFolder + imageFileName);
//
//        //폴더에 이미지 파일 저장
//        try {
//            Files.write(imageFilePath, imageFile.getBytes());
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new GeneralException(ErrorStatus.PROFILE_IMAGE_UPLOAD_FAIL);
//        }
//
//        User user= userRepository.findById(userId).orElseThrow(
//                ()-> new GeneralException(ErrorStatus._USER_NOT_FOUND)
//        );
//
//        //기존 이미지가 있었다면 해당 이미지는 폴더에서 삭제
//        String oldImageFileName = user.getImageUrl();
//        if(oldImageFileName != null && !oldImageFileName.isEmpty()){
//            Path oldImageFilePath = Paths.get(uploadFolder + oldImageFileName);
//            try {
//                Files.deleteIfExists(oldImageFilePath);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        userRepository.updateProfileImageUrl(userId,imageFileName);
//        return imageFileName;
//    }
//
//    @Transactional
//    public String deleteProfileImage(Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
//        );
//        String imageFileName = user.getImageUrl(); //프로필 이미지 url get
//        if (imageFileName != null && !imageFileName.isEmpty()) { //이미지가 등록된 상태라면 삭제
//            Path imageFilePath = Paths.get(uploadFolder + imageFileName);
//            try {
//                Files.deleteIfExists(imageFilePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            userRepository.updateProfileImageUrl(userId, null);
//        }
//        else
//            throw new GeneralException(ErrorStatus.PROFILE_IMAGE_NOT_FOUND);
//
//        return "프로필 사진 삭제";
//    }
//}
