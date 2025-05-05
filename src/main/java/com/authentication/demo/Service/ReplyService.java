package com.authentication.demo.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Model.ReplyModel;
import com.authentication.demo.Repository.LikeRepository; // Ensure this path matches the actual location of ReplyModel
import com.authentication.demo.Repository.ReplyRepository; // Ensure this path matches the actual location of CommentModel


@Service
public class ReplyService {
  private final ReplyRepository replyRepository;
  private final CommentService commentService;
  private final UserService userService;
  private final LikeRepository likeRepository;

  public ReplyService(ReplyRepository replyRepository, CommentService commentService, UserService userService, LikeRepository likeRepository) {
    this.replyRepository = replyRepository;
    this.commentService = commentService;
    this.userService = userService;
    this.likeRepository = likeRepository;
  }
  

  // GET REPLIES BY COMMENT ID
  public List<ReplyModel> getRepliesByCommentId(Long commentId) {
    return replyRepository.findByCommentIdOrderByCreatedAtAsc(commentId);
  }

  // CREATE REPLY
  public Map<String, String> createReply(Map<String, String> params) {

    // Validate input parameters
    if (params.get("content") == null || params.get("content").isEmpty()) {
      throw new RuntimeException("Reply content is required");
    }

    if (params.get("commentId") == null) {
      throw new RuntimeException("Comment ID is required");
    }

    Long commentId = Long.valueOf(params.get("commentId"));
    CommentModel comment = commentService.getCommentById(commentId);

    // Create reply
    ReplyModel reply = new ReplyModel();
    reply.setComment(comment);
    reply.setContent(params.get("content"));
    reply.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    reply.setUser(userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found")));
    replyRepository.save(reply);

    return Map.of("message", "Reply created successfully");
  }

  // GET LATEST REPLY URL
  public Long getLatestReplyUrl() {
    return replyRepository.findTopByOrderByIdDesc().getId();
  }

  // GET REPLIES BY REPLY ID
  public ReplyModel getReplyById(Long replyId) {
    return replyRepository.findById(replyId)
        .orElseThrow(() -> new RuntimeException("Reply not found with id: " + replyId));
  }

  // Count reply likes
  public Integer countReplyLikes(Long replyId) {
    return likeRepository.countByReplyId(replyId);
  }

}