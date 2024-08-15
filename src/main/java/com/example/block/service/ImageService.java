package com.example.block.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.block.domain.User;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.global.config.S3Config;
import com.example.block.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    //@Value("${file.path}") //빈 등록 해야함 설정파일 수정하기
    @Value("${cloud.aws.s3.path.profile}")
    private String uploadFolder;

    @Transactional
    public String uploadImageToS3(Integer userId, MultipartFile imageFile) {
        // UUID 생성 및 S3 키 설정
        UUID uuid = UUID.randomUUID();
        String key = s3Config.getProfilePath() + "/" + uuid + "_" + imageFile.getOriginalFilename();

        // 파일의 Content-Type 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());
        metadata.setContentType(imageFile.getContentType()); // 이 부분을 추가

        // S3에 업로드
        try (InputStream inputStream = imageFile.getInputStream()) {
            amazonS3.putObject(s3Config.getBucket(), key, inputStream, metadata);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        // S3 URL 생성
        String imageUrl = amazonS3.getUrl(s3Config.getBucket(), key).toString();

        // 기존 db의 이미지 삭제
        deleteImageFromS3(userId);
        // 새로운 이미지 URL 업데이트
        userRepository.updateProfileImageUrl(userId, imageUrl);

        return imageUrl;
    }

    @Transactional
    public String deleteImageFromS3(Integer userId) {
        User user= userRepository.findById(userId).orElseThrow(
                ()-> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );
        String imageUrl = user.getImageUrl();
        // 이미지가 있다면 아래 내용 실행
        if(imageUrl!=null && !imageUrl.isEmpty()){
            // URL에서 S3 키를 추출
            String key = imageUrl.substring(imageUrl.indexOf(".com/") + 5); // ".com/" 이후의 문자열 추출
            System.out.println("Deleting S3 object with key: " + key);
            try {
                amazonS3.deleteObject(s3Config.getBucket(), key);
            } catch (AmazonServiceException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이전 이미지 제거에 실패했습니다.");
            }
            userRepository.updateProfileImageUrl(userId, null);
        }

        return "프로필 사진 삭제";
    }


    //로컬에 이미지 저장
    @Transactional
    public String uploadProfileImage(Integer userId, MultipartFile imageFile) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + imageFile.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        //폴더에 이미지 파일 저장
        try {
            Files.write(imageFilePath, imageFile.getBytes());
        }catch (Exception e){
            e.printStackTrace();
            throw new GeneralException(ErrorStatus.PROFILE_IMAGE_UPLOAD_FAIL);
        }

        User user= userRepository.findById(userId).orElseThrow(
                ()-> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );

        //기존 이미지가 있었다면 해당 이미지는 폴더에서 삭제
        String oldImageFileName = user.getImageUrl();
        if(oldImageFileName != null && !oldImageFileName.isEmpty()){
            Path oldImageFilePath = Paths.get(uploadFolder + oldImageFileName);
            try {
                Files.deleteIfExists(oldImageFilePath);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        userRepository.updateProfileImageUrl(userId,imageFileName);
        return imageFileName;
    }
    //로컬 이미지 삭제
    @Transactional
    public String deleteProfileImage(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );
        String imageFileName = user.getImageUrl(); //프로필 이미지 url get
        if (imageFileName != null && !imageFileName.isEmpty()) { //이미지가 등록된 상태라면 삭제
            Path imageFilePath = Paths.get(uploadFolder + imageFileName);
            try {
                Files.deleteIfExists(imageFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userRepository.updateProfileImageUrl(userId, null);
        }
        else
            throw new GeneralException(ErrorStatus.PROFILE_IMAGE_NOT_FOUND);

        return "프로필 사진 삭제";
    }
}
