package com.vcc.agile.project.mgmt.PowerNPride.repository;

import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Topic;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTopic(Topic topic);

    List<Post> findByUser(User user);
}
