package com.example.block.service;

import com.example.block.converter.HomeRequestConverter;
import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.dto.HomeRequestDTO;
import com.example.block.repository.ContestRepository;
import com.example.block.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomePageService {
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;


    public HomeRequestDTO.HomePageRequestDTO getHomePageRequestDTO(Integer userId) {
        List<HomeRequestDTO.HomeContestDTO> contestList = this.getContestByPrefer(userId);
        Optional<User> user = userRepository.findById(userId);
        HomeRequestDTO.HomePageRequestDTO homePageRequestDTO = HomeRequestConverter.toHomePageRequestDTO(contestList, user.orElse(null));
        return homePageRequestDTO;
    }

    public List<HomeRequestDTO.HomeContestDTO> getContestByPrefer(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        List<Contest> contests = contestRepository.getContestByContestCategory(user.getInterestCategory());
        return HomeRequestConverter.toHomeContestDTOList(contests);
    }
}
