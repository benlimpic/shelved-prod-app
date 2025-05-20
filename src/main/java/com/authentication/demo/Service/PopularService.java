package com.authentication.demo.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    all.addAll(collectionRepository.findAll()); // or your top-N query
    all.addAll(itemRepository.findAll());

    // Sort by popularity score descending
    all.sort(Comparator.comparingInt(PopularEntry::getPopularityScore).reversed());

    // Limit to top 100 if needed
    return all.stream().limit(100).toList();
  }

}
