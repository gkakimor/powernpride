package com.vcc.agile.project.mgmt.PowerNPride.repository;

import com.vcc.agile.project.mgmt.PowerNPride.model.Comment;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
