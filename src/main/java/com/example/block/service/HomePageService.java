package com.example.block.service;

import com.example.block.converter.HomeRequestConverter;
import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.enums.ContestCategory;
import com.example.block.dto.HomeRequestDTO;
import com.example.block.repository.ContestRepository;
import com.example.block.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.block.domain.enums.ContestCategory.*;

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
        List<Contest> contests = contestRepository.findAllContest();
        return HomeRequestConverter.toHomeContestDTOList(contests, user);
    }

    public List<Contest> getContestByCategory(ContestCategory category) {
        if (category == IT)
            return contestRepository.findContestByHashTag("IT•소프트웨어•게임");
        else if (category == SPORTS)
            return contestRepository.findContestByHashTag("스포츠");
        else if (category == IDEA)
            return contestRepository.findContestByHashTag("아이디어•제안");
        else if (category == NAMING)
            return contestRepository.findContestByHashTag("네이밍•슬로건");
        else if (category == LITERATURE)
            return contestRepository.findContestByHashTag("경시•학문•논문");
        else if (category == INDUSTRY)
            return contestRepository.findContestByHashTag("산업•사회•건축•관광•창업");
        else if (category == SCIENCE)
            return contestRepository.findContestByHashTag("과학•공학•기술");
        else if (category == ART)
            return contestRepository.findContestByHashTagIn(List.of("디자인•캐릭터•웹툰", "문학•문예", "음악•가요•댄스•무용"));
        else
           return null;
    }
}
