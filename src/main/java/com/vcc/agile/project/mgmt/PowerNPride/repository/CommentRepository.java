package com.vcc.agile.project.mgmt.PowerNPride.repository;

import com.vcc.agile.project.mgmt.PowerNPride.model.Comment;
import com.vcc.agile.project.mgmt.PowerNPride.model.Post;
import com.vcc.agile.project.mgmt.PowerNPride.model.Token;
import com.vcc.agile.project.mgmt.PowerNPride.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedDateDesc(Post post);

    List<Comment> findAllByUserOrderByCreatedDateDesc(User user);

    @Query(value = "SELECT c FROM Comment c INNER JOIN c.post p INNER JOIN p.topic t " +
            "WHERE t.id = :id")
    List<Comment> findAllByTopic(Long id);
}
