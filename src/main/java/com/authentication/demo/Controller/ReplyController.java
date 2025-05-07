package com.authentication.demo.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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




  // POST REPLY
  @PostMapping("/comments/{commentId}/replies")
  public String createReply(
      @PathVariable Long commentId,
      @RequestParam String content
  ) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
        System.out.println("User is not authenticated");
        throw new RuntimeException("User is not authenticated");
    } else {
        System.out.println("Authenticated user: " + authentication.getName());
    }
  

      System.out.println("Received POST request to create reply");
      System.out.println("Comment ID: " + commentId);
      System.out.println("Content: " + content);
  
      // Redirect to the desired page after saving the reply

      CommentModel comment = commentService.getCommentById(commentId);

      replyService.createReply(commentId, content);

      Long replyId = replyService.getLatestReplyId();

      if (replyId == null) {
        throw new RuntimeException("Failed to retrieve the latest reply ID");
      }

      if (comment.getCollection() != null) {
        return "redirect:/collection/" + comment.getCollection().getId() + "/comments#reply-" + replyId;

      } else if (comment.getItem() != null) {
        return "redirect:/item/" + comment.getItem().getId() + "/comments#reply-" + replyId;

      }

      return "redirect:/index";
  }
}
