package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.dto.CommentsDto;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.PostNotFoundException;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.mapper.CommentMapper;
import com.vcc.agile.project.mgmt.PowerNPride.model.Comment;
import com.vcc.agile.project.mgmt.PowerNPride.model.NotificationEmail;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import com.vcc.agile.project.mgmt.PowerNPride.repository.CommentRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        //String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post.");
        //sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).toList();
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringPowerNPrideException("Comments contains unacceptable language");
        }
        return false;
    }
}
