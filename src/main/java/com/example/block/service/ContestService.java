package com.example.block.service;

import com.example.block.converter.ContestConverter;
import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.dto.ContestResponseDTO;
import com.example.block.domain.MyContest;
import com.example.block.repository.ContestRepository;
import com.example.block.repository.MyContestRepository;
import com.example.block.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final MyContestRepository myContestRepository;
    private final AuthService authService;

    public ContestResponseDTO.ContestInfoDTO getContestInfo(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new IllegalArgumentException("해당 대회가 존재하지 않습니다."));
        ContestResponseDTO.ContestInfoDTO contestInfoDTO = ContestConverter.toContestInfoDTO(contest);
        return contestInfoDTO;
    }

    // 공모전을 저장하는 메소드
    public String saveMyContest(Integer contestId) {
        Optional<Contest> contest = contestRepository.findById(contestId);
        if (contest.isEmpty())
            return "해당 대회가 존재하지 않습니다.";
        Optional<User> user = userRepository.findById(authService.getUserIdFromSecurity());
        if (user.isEmpty())
            return "해당 사용자가 존재하지 않습니다.";
        if (isSavedContest(contestId)) {
            return "이미 저장된 대회입니다.";
        }
        MyContest myContest = ContestConverter.toMyContest(contest.orElse(null), user.orElse(null));
        myContestRepository.save(myContest);
        return "대회가 저장되었습니다.";
    }

    // 저장된 공모전의 중복 여부를 확인하는 메소드
    public boolean isSavedContest(Integer contestId) {
        MyContest checkContest = myContestRepository.findByUserIdAndContestId(authService.getUserIdFromSecurity(), contestId);
        if (checkContest == null) {
            return false;
        }
        return true;
    }

    // 저장된 공모전을 삭제하는 메소드
    public void deleteMyContest(Integer contestId) {
        MyContest myContest = myContestRepository.findByUserIdAndContestId(authService.getUserIdFromSecurity(), contestId);
        myContestRepository.delete(myContest);
    }
}