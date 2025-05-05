package com.authentication.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Model.ReplyModel;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyModel, Long> {

  List<ReplyModel> findByCommentIdOrderByCreatedAtAsc(Long commentId);

  ReplyModel findTopByOrderByIdDesc();



}
