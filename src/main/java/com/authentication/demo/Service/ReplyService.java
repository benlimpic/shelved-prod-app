package com.authentication.demo.Service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Model.ReplyModel;
import com.authentication.demo.Repository.LikeRepository;
import com.authentication.demo.Repository.ReplyRepository;


@Service
public class ReplyService {
  private final ReplyRepository replyRepository;
  private final CommentService commentService;

  private final LikeRepository likeRepository;

  public ReplyService(ReplyRepository replyRepository, CommentService commentService,  LikeRepository likeRepository) {
    this.replyRepository = replyRepository;
    this.commentService = commentService;

    this.likeRepository = likeRepository;
  }
  

  // GET REPLIES BY COMMENT ID
  public List<ReplyModel> getRepliesByCommentId(Long commentId) {
    return replyRepository.findByCommentIdOrderByCreatedAtAsc(commentId);
  }


  @Transactional
  public void createReply(Long commentId, String content) {
      System.out.println("Fetching comment with ID: " + commentId);
      CommentModel comment = commentService.getCommentById(commentId);
      if (comment == null) {
          throw new RuntimeException("Comment not found with ID: " + commentId);
      }
  
      Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
  
      ReplyModel reply = new ReplyModel();
      reply.setComment(comment);
      reply.setContent(content);
      reply.setUserId(userId);
      reply.setCreatedAt(new Timestamp(System.currentTimeMillis()));
  
      System.out.println("Saving reply: " + reply);
      replyRepository.save(reply);
      System.out.println("Reply saved successfully");
  }


  // GET LATEST REPLY URL
  public Long getLatestReplyId() {
    ReplyModel latestReply = replyRepository.findTopByOrderByIdDesc();
    if (latestReply == null) {
      throw new RuntimeException("No replies found");
    }
    return latestReply.getId();
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

  // Removed duplicate and erroneous method definition

}