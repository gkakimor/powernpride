package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.dto.TopicDto;
import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.mapper.TopicMapper;
import com.vcc.agile.project.mgmt.PowerNPride.model.Comment;
import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import com.vcc.agile.project.mgmt.PowerNPride.repository.CommentRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.TopicRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    private final CommentRepository commentRepository;

    @Transactional
    public TopicDto save (TopicDto topicDto)
    {
        Topic save = topicRepository.save(topicMapper.mapDtoToTopic(topicDto));
        topicDto.setId(save.getId());
        return topicDto;
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getAll() {
        List<Topic> topics = topicRepository.findAll();

        List<Comment> comments = commentRepository.findAllByTopic(3L);

        return topicRepository.findAll()
                .stream()
                .map(topicMapper::mapTopicToDto)
                .collect(toList());
    }

    public TopicDto getTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new SpringPowerNPrideException("No topic found with ID - " + id));
        return topicMapper.mapTopicToDto(topic);
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicByTopicName(String topicName) {
        return topicRepository.findByNameContains(topicName)
                .stream()
                .map(topicMapper::mapTopicToDto)
                .collect(toList());
    }
}
