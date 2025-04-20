package com.authentication.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Service.FollowService;
import com.authentication.demo.Service.UserService;

@Controller
public class FollowController {

  private final FollowService followService;
  private final UserService userService;

  public FollowController(FollowService followService, UserService userService) {
    this.followService = followService;
    this.userService = userService;
  }

  @PostMapping("/follow/{id}")
  public String followUser(@PathVariable Long id) {
    if (id == null || userService.getCurrentUser().isEmpty() || userService.getUserById(id) == null) {
      return "redirect:/profile";
    }

    UserModel currentUser = userService.getCurrentUser().orElse(null);
    UserModel targetUser = userService.getUserById(id);

    if (currentUser == null || targetUser == null) {
      return "redirect:/profile";
    }

    if (!followService.isFollowing(currentUser, targetUser)) {
      followService.followUser(currentUser, targetUser);
    }

    return "redirect:/profile/" + id;
  }

  @PostMapping("/unfollow/{id}")
  public String unfollowUser(@PathVariable Long id) {
    if (id == null || userService.getCurrentUser().isEmpty() || userService.getUserById(id) == null) {
      return "redirect:/profile";
    }

    UserModel currentUser = userService.getCurrentUser().orElse(null);
    UserModel targetUser = userService.getUserById(id);

    if (currentUser == null || targetUser == null) {
      return "redirect:/profile";
    }

    if (followService.isFollowing(currentUser, targetUser)) {
      followService.unfollowUser(currentUser, targetUser);
    }

    return "redirect:/profile/" + id;
  }
}
