package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.dto.PostRequest;
import com.vcc.agile.project.mgmt.PowerNPride.dto.PostResponse;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.PostNotFoundException;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.TopicNotFoundException;
import com.vcc.agile.project.mgmt.PowerNPride.mapper.PostMapper;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.TopicRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final AuthService authService;

    public void save(PostRequest postRequest) {
        Topic topic = topicRepository.findByName(postRequest.getTopicName())
                .orElseThrow(() -> new SpringPowerNPrideException(postRequest.getTopicName()));
        postRepository.save(postMapper.map(postRequest, topic, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"))
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException(topicId.toString()));
        List<Post> posts = postRepository.findAllByTopicOrderByCreateDateDesc(topic);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUserOrderByCreateDateDesc(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
