package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.dto.DashboardResponse;
import com.vcc.agile.project.mgmt.PowerNPride.repository.CommentRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.TopicRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;

    public DashboardResponse getAllNumbers(){



        return DashboardResponse.builder()
                .numberOfTopics((int) topicRepository.count())
                .numberOfUsers((int) userRepository.count())
                .numberOfPosts((int) postRepository.count())
                .numberOfComments((int) commentRepository.count())
                .build();
    }
}
