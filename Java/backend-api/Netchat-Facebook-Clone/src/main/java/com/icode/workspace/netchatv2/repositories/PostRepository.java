package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
}
