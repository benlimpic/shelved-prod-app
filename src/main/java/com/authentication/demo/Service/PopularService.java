package com.authentication.demo.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authentication.demo.Interface.PopularEntry;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Repository.ItemRepository;

@Service
public class PopularService {

  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private ItemRepository itemRepository;

  public List<PopularEntry> getTopPopularEntries() {
    List<PopularEntry> all = new ArrayList<>();
    // Ensure the results are cast to PopularEntry if needed
    collectionRepository.findAll().forEach(item -> all.add((PopularEntry) item));
    itemRepository.findAll().forEach(item -> all.add((PopularEntry) item));

    // Sort by popularity score descending
    all.sort((a, b) -> {
        int aScore = a.getLikeCount() + a.getCommentCount();
        int bScore = b.getLikeCount() + b.getCommentCount();
        return Integer.compare(bScore, aScore); // descending
    });

    // Limit to top 100 if needed
    return all.stream().limit(100).toList();
  }

}
