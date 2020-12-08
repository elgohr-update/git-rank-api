package com.mallycrip.rank.domain.usecase;

import com.mallycrip.rank.domain.repository.ContributionsRepository;
import com.mallycrip.rank.domain.repository.UserRepository;
import com.mallycrip.rank.infra.github.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {
    private final UserRepository userRepository;
    private final ContributionsRepository contributionsRepository;
    private final GithubService githubService;

    @Override
    public void execute(String email, String name, String description, String githubId) {
        userRepository.findById(email).ifPresent(
                user -> {
                    contributionsRepository.findById(email).ifPresent(
                            contributions -> {
                                contributions.changeGithubId(githubId);
                                contributions.updateNumOfContributions(githubService.getContributions(githubId));
                                contributions.updateGithubImage(githubService.getImageUrl(githubId));
                                contributionsRepository.save(contributions);
                    });

                    user.changeUserName(name);
                    user.changeDescription(description);
                    user.changeGithubId(githubId);

                    userRepository.save(user);
                }
        );
    }
}
