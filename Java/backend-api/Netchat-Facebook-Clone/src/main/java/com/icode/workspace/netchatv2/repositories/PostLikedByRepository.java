package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.PostLikedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikedByRepository extends JpaRepository<PostLikedBy, Long> {
}
