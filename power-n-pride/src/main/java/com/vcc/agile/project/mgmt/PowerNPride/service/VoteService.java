package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.dto.VoteDto;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.PostNotFoundException;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Vote;
import com.vcc.agile.project.mgmt.PowerNPride.model.VoteType;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.vcc.agile.project.mgmt.PowerNPride.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        boolean reverse = false;
        int count = 1;
        Vote vote = mapToVote(voteDto, post);

        if (voteByPostAndUser.isPresent()){// &&
                //voteByPostAndUser.get().getVoteType()
                        //.equals(voteDto.getVoteType())) {
            if (!voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
                count = -2;
            }
            reverse = true;
            voteRepository.deleteById(voteByPostAndUser.get().getVoteId());
            //throw new SpringPowerNPrideException("You have already "
            //        + voteDto.getVoteType() + "'d for this post");


        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(reverse ? post.getVoteCount() - count : post.getVoteCount() + count);
        } else {
            post.setVoteCount(reverse ? post.getVoteCount() + count : post.getVoteCount() - count);
        }

        if (!reverse || count == -2 )
            voteRepository.save(vote);

        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
