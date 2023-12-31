package com.vcc.agile.project.mgmt.PowerNPride.mapper;

import com.vcc.agile.project.mgmt.PowerNPride.dto.PostRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.PostResponse;
import com.vcc.agile.project.mgmt.PowerNPride.model.*;
import com.vcc.agile.project.mgmt.PowerNPride.repository.CommentRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.VoteRepository;
import com.vcc.agile.project.mgmt.PowerNPride.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

import static com.vcc.agile.project.mgmt.PowerNPride.model.VoteType.DOWNVOTE;
import static com.vcc.agile.project.mgmt.PowerNPride.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Topic topic, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPostOrderByCreatedDateDesc(post).size();
    }

    String getDuration(Post post) {
        PrettyTime prettyTime = new PrettyTime();
        Date date = Date.from(post.getCreateDate());
        return prettyTime.format(date);
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
