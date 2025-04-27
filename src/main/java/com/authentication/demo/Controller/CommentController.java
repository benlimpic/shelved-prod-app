package com.authentication.demo.Controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Service.CommentService;
import com.authentication.demo.Service.UserService;

@Controller
public class CommentController {

  private final CommentService commentService;


  public CommentController(UserService userService, CollectionRepository collectionRepository,
      CommentService commentService) {
    this.commentService = commentService;
  }

    @PostMapping("/collections/{collectionId}/comments")
    public String postComment(@PathVariable Long collectionId, @RequestParam String content) {
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Comment content is required");
        }

        // Call the service to create the comment
        Map<String, String> commentData = Map.of(
            "collectionId", collectionId.toString(),
            "content", content
        );
        commentService.createComment(commentData);

        // Redirect back to the collection page
        return "redirect:/collection/" + collectionId + "#comments";
    }

    @PostMapping("/collections/{collectionId}/comments-from-index")
    public String postCommentFromIndex(@PathVariable Long collectionId, @RequestParam String content) {
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Comment content is required");
        }

        // Call the service to create the comment
        Map<String, String> commentData = Map.of(
            "collectionId", collectionId.toString(),
            "content", content
        );
        commentService.createComment(commentData);

        // Redirect back to the collection page
        return "redirect:/index#comments-" + collectionId;
    }

    @PostMapping("/items/{itemId}/comments")
    public String postCommentItem(@PathVariable Long itemId, @RequestParam String content) {
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Comment content is required");
        }

        // Call the service to create the comment
        Map<String, String> commentData = Map.of(
            "itemId", itemId.toString(),
            "content", content
        );
        commentService.createCommentItem(commentData);

        // Redirect back to the item page
        return "redirect:/item/" + itemId + "#comments";
    }

}
