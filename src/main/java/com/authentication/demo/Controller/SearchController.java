package com.authentication.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Service.SearchService;

@Controller
@RequestMapping("/search-live")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // SEARCH
    @GetMapping("/popular/search-fragment")
    public String searchFragment(@RequestParam("query") String query, Model model) {
        List<UserModel> users = searchService.searchUsers(query);
        List<CollectionModel> collections = searchService.searchCollections(query);
        List<ItemModel> items = searchService.searchItems(query);
        model.addAttribute("users", users);
        model.addAttribute("collections", collections);
        model.addAttribute("items", items);
        model.addAttribute("query", query);
        return "fragments/searchResults :: resultsList";
    }

}