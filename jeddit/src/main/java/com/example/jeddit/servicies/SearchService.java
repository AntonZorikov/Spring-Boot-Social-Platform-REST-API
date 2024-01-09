package com.example.jeddit.servicies;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;

import java.util.List;

public interface SearchService {

    List<User> searchUser(SearchRequest request, int from, int to);

    List<Community> searchCommunity(SearchRequest request, int from, int to);

    List<Post> searchPost(SearchRequest request, int from, int to);

}
