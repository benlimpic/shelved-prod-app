package com.authentication.demo.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Model.CommentModel;
import com.authentication.demo.Service.CommentService;
import com.authentication.demo.Service.ReplyService;
import com.authentication.demo.Service.UserService;

@Controller
public class ReplyController {
  
  public final ReplyService replyService;
  public final CommentService commentService;
  public final UserService userService;

  public ReplyController(ReplyService replyService, CommentService commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
    this.replyService = replyService;
  }

  @PostMapping("/collections/{collectionId}/replies")
  public String postReply(@RequestParam Long commentId, String content, Principal principal) {
    String username = principal.getName();

    if (userService.getUserByUsername(username).isEmpty()) {
      throw new RuntimeException("User not found with username: " + username);
    }

    CommentModel comment = commentService.getCommentById(commentId);
    if (comment == null) {
      throw new RuntimeException("Comment not found with id: " + commentId);
    }

    replyService.createReply(commentId, content, username);
    Long latestReplyId = replyService.getLatestReplyId();
    String replyId = latestReplyId != null ? String.valueOf(latestReplyId) : "unknown";
    String collectionId = String.valueOf(comment.getCollection().getId());

    return "redirect:/collection/" + collectionId + "/comments#reply-" + replyId;
  }
}
