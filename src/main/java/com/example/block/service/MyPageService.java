package com.example.block.service;

import com.example.block.converter.MyPageConverter;
import com.example.block.domain.Contest;
import com.example.block.domain.MyContest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Applicant;
import com.example.block.domain.mapping.Likes;
import com.example.block.dto.MyPageResponseDTO;
import com.example.block.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final LikesRepository likesRepository;
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final MyContestRepository mycontestRepository;
    private final MatchesRepository matchesRepository;
    private final AuthService authService;
    private final ContestRepository contestRepository;

    public Applicant getChallenger(Integer contestId, Integer userId){
        return applicantRepository.findByContestIdAndUserId(contestId, userId).get();
    }

    //  내가 좋아요 누른 사람 목록
    public List<Applicant> getLikeChallengerList(Integer userId){
        List<Likes> likeList = likesRepository.findByUserLikerId(userId);

        return likeList.stream()
                .map(like -> getChallenger(like.getContest().getId(), like.getUserLiked().getId()))
                .collect(Collectors.toList());
    }

    //  나에게 좋아요를 누른 사람 목록
    public List<Applicant> getLikeByChallengerList(Integer userId){
        List<Likes> likedList = likesRepository.findByUserLikedId(userId);

        return likedList.stream()
                .map(like -> getChallenger(like.getContest().getId(), like.getUserLiker().getId()))
                .collect(Collectors.toList());
    }

    // 마이페이지를 띄워줄 유저 정보
    public MyPageResponseDTO.myPageDTO getMyPageUser(){
        Optional<User> user = userRepository.findById(authService.getUserIdFromSecurity());
        return MyPageConverter.toMyPageDTO(user.orElse(null));
    }

    // 유저 정보 수정
    public MyPageResponseDTO.myPageEditDataDTO updateUser(MyPageResponseDTO.myPageEditDataDTO updatedUser) {
        Optional<User> user = userRepository.findById(authService.getUserIdFromSecurity());
        user.get().setUserId(updatedUser.getUserId());
        user.get().setPassWord(updatedUser.getPassWord());
        user.get().setBirthDay(updatedUser.getBirthDay());
        user.get().setPhoneNumber(updatedUser.getPhoneNumber());
        user.get().setAddress(updatedUser.getAddress());
        user.get().setUniversity(updatedUser.getUniversity());
        user.get().setUnivMajor(updatedUser.getUnivMajor());
        user.get().setPortfolio(updatedUser.getPortfolio());
        user.get().setInterestCategory(updatedUser.getCategory());
        userRepository.save(user.get());
        return MyPageConverter.toMyPageEditDataDTO(user.orElse(null));
    }

    // 저장한 공모전을 모두 조회
    public List<MyPageResponseDTO.contestDTO> getMyContestList(){
        List<MyContest> myContestList = mycontestRepository.findByUserId(authService.getUserIdFromSecurity());
        return myContestList.stream()
                .map(contest -> MyPageConverter.toMyContestDTO(contest.getContest()))
                .collect(Collectors.toList());
    }

    //매칭된 공모전 조회
    public List<MyPageResponseDTO.matchContestDTO> getMatchedContestList() {
        List<Integer> myContestList = matchesRepository.findMatchedContestByUserId(authService.getUserIdFromSecurity());

        // 중복된 contestId를 제거하고, contestDTO 리스트로 변환
        return myContestList.stream()
                .distinct()  // 중복된 contestId 제거
                .map(contestId -> contestRepository.findById(contestId).orElse(null)) // contestId로 Contest 객체 조회
                .map(MyPageConverter::toMatchContest) // Contest 객체를 contestDTO로 변환
                .collect(Collectors.toList()); // 최종적으로 리스트로 수집
    }
}
