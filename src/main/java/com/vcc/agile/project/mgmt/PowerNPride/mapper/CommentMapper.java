package com.vcc.agile.project.mgmt.PowerNPride.mapper;

import com.vcc.agile.project.mgmt.PowerNPride.dto.CommentsDto;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.PostNotFoundException;
import com.vcc.agile.project.mgmt.PowerNPride.model.Comment;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private PostRepository postRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    public abstract Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "createdDate", expression = "java(getDuration(comment.getCreatedDate()))")
    @Mapping(target = "postName", expression = "java(getPostName(comment.getPost().getPostId()))")
    public abstract CommentsDto mapToDto(Comment comment);

    String getDuration(Instant createdDate) {
        PrettyTime prettyTime = new PrettyTime();
        Date date = Date.from(createdDate);
        return prettyTime.format(date);
    }

    String getPostName(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return post.getPostName();
    }
}
