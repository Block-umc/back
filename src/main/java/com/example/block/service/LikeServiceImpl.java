package com.example.block.service;

import com.example.block.converter.LikeConverter;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Likes;
import com.example.block.domain.mapping.Matches;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.handler.EmailHandler;
import com.example.block.global.apiPayload.exception.handler.LikeHandler;
import com.example.block.global.apiPayload.exception.handler.UserHandler;
import com.example.block.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService{
    private final MatchesRepository matchRepository;
    private final ApplicantRepository applicantRepository;
    private final LikesRepository likeRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;

//    public Boolean hasMatched(Integer userId1, Integer applicantId, Integer contestId){
//        Integer userId2 = findUser(applicantId);
//        if(matchRepository.existsByUserId1AndUserId2AndContestId(userId1, userId2, contestId) || matchRepository.existsByUserId1AndUserId2AndContestId(userId2, userId1, contestId))
//            return true;
//        else
//            return false;
//    }
    public Boolean hasLiked(Integer userId1, Integer applicantId, Integer contestId){
        Integer userId2 = findUser(applicantId, contestId);
        return likeRepository.existsByUserLikerIdAndUserLikedIdAndContestId(userId2, userId1, contestId);
    }

    public Integer findUser(Integer applicantId, Integer contestId){
        User user = applicantRepository.findUserByIdAndContestId(applicantId, contestId);
        if (user == null) {
            throw new UserHandler(ErrorStatus.USERID_NOT_FOUND);
        }
        return user.getId();
    }
    public String findEmail(Integer userId){
        String email = userRepository.getEmailById(userId);
        if (email == null) {
            throw new EmailHandler(ErrorStatus.EMAIL_NOT_FOUND);
        }
        return email;
    }
    public Boolean hasMatched(Integer userId1, Integer applicantId, Integer contestId){
        Integer userId2 = findUser(applicantId, contestId);
        if(matchRepository.existsByUser1AndUser2AndContest(userRepository.findById(userId1).get(), userRepository.findById(userId2).get(), contestRepository.findById(contestId).get()) ||
                matchRepository.existsByUser1AndUser2AndContest(userRepository.findById(userId2).get(), userRepository.findById(userId1).get(), contestRepository.findById(contestId).get()))
            return true;
        else
            return false;
    }
    @Transactional
    public void likeUser(Integer userId, Integer applicantId, Integer contestId){
        Integer userId2 = findUser(applicantId, contestId);
        Likes newLike = LikeConverter.likeDTO(userRepository.findById(userId).get(),
                                               userRepository.findById(userId2).get(),
                                                contestRepository.findById(contestId).get());

        if(likeRepository.findByUserLikerIdAndUserLikedIdAndContestId(userId, userId2, contestId).isPresent()){
            throw new LikeHandler(ErrorStatus.LIKE_ALREADY_EXIST);
        }
        else{
            likeRepository.save(newLike);
        }

    }

    public void match(Integer userId, Integer applicantId, Integer contestId){
        Integer userId2 = findUser(applicantId, contestId);
        Matches newMatch = LikeConverter.matchDTO(userRepository.findById(userId).get(),
                                                    userRepository.findById(userId2).get(),
                                                    contestRepository.findById(contestId).get());

        matchRepository.save(newMatch);
    }
    @Transactional
    public void deleteLike(Integer userId, Integer applicantId, Integer contestId){
        Integer userId2 = findUser(applicantId, contestId);
        Optional<Likes> optionalLike = likeRepository.findByUserLikerIdAndUserLikedIdAndContestId(userId, userId2, contestId);
        if(optionalLike.isPresent()){
            likeRepository.delete(optionalLike.get());
        }
        else {
            throw new LikeHandler(ErrorStatus.LIKE_DOESNT_EXIST);
        }
    }
}
