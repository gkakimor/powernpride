package com.vcc.agile.project.mgmt.PowerNPride.mapper;

import com.vcc.agile.project.mgmt.PowerNPride.dto.TopicDto;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(topic.getPosts()))")
    TopicDto mapTopicToDto(Topic topic);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Topic mapDtoToTopic(TopicDto topicDto);
}
