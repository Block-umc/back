package com.example.block.service;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Applicant;
import com.example.block.dto.TeamMatchRequestDTO;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.handler.ApplicantHandler;
import com.example.block.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamMatchService {

    private final UserRepository userRepository;
    private final ContestRepository contestRepository;
    private final ApplicantRepository applicantRepository;
    private final MatchesRepository matchRepository;
    private final LikesRepository likesRepository;

    public void applyToContest(TeamMatchRequestDTO.ApplyDTO request, Integer contestId, Integer userId){

        if(applicantRepository.findByContestIdAndUserId(contestId, userId).isPresent()){

            throw new ApplicantHandler(ErrorStatus.CHALLENGER_ALREADY_EXISTS);
        }
        else {

            User user = userRepository.findById(userId).get();
            Contest contest = contestRepository.findById(contestId).get();

            Applicant newApplicant = Applicant.builder()
                    .user(user)
                    .contest(contest)
                    .applyPart(request.getApplyPart())
                    .content(request.getContent())
                    .build();

            applicantRepository.save(newApplicant);
        }
    }

    public List<Applicant> getChallengerList(Integer contestId){
        return applicantRepository.findByContestId(contestId);
    }

    public Applicant getChallenger(Integer contestId, Integer challengerId){
        return applicantRepository.findByContestIdAndId(contestId, challengerId);
    }

    public List<User> getMemberList(Integer contestId, Integer userId){
        List<Integer> idList = matchRepository.findMatchedUsersByUserIdAndContestId(userId, contestId);

        return idList.stream()
                .map(id -> userRepository.findById(id).get()).collect(Collectors.toList());
    }

    public Boolean hasUserLiked(Integer userLikerId, Integer userLikedId, Integer contestId){
        return likesRepository.findByUserLikerIdAndUserLikedIdAndContestId(userLikerId, userLikedId, contestId).isPresent();
    }
}
