package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String postReply(@PathVariable Long commentId, @RequestParam String content) {
    if (content == null || content.isEmpty()) {
      throw new RuntimeException("Reply content is required");
    }

    // Call the service to create the reply
    Map<String, String> replyData = Map.of(
        "commentId", commentId.toString(),
        "content", content
    );
    replyService.createReply(replyData);

    Long replyId = replyService.getLatestReplyUrl();

    // Redirect back to the comment page
    return "redirect:/comments/" + commentId + "/replies#reply-" + replyId;
  }

}
