package com.vcc.agile.project.mgmt.PowerNPride.repository;

import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByName(String topicName);
}
