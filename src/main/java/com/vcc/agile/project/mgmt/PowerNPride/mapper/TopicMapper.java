package com.vcc.agile.project.mgmt.PowerNPride.mapper;

import com.vcc.agile.project.mgmt.PowerNPride.dto.TopicDto;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import com.vcc.agile.project.mgmt.PowerNPride.repository.CommentRepository;
import com.vcc.agile.project.mgmt.PowerNPride.repository.PostRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TopicMapper {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(topic))")
    @Mapping(target = "numberOfComments", expression = "java(mapComments(topic))")
    public abstract TopicDto mapTopicToDto(Topic topic);

    Integer mapPosts(Topic topic) {
        return postRepository.findAllByTopicOrderByCreateDateDesc(topic).size();
    }

    Integer mapComments(Topic topic) {
        return commentRepository.findAllByTopic(topic.getId()).size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    public abstract Topic mapDtoToTopic(TopicDto topicDto);
}
