package com.example.jeddit.servicies;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<User> searchUser(SearchRequest request, int page, int size);

    Page<Community> searchCommunity(SearchRequest request, int page, int size);

    Page<Community> searchCommunityByDescription(SearchRequest request, int page, int size);

    Page<Post> searchPost(SearchRequest request, int page, int size);

    Page<Post> searchPostByText(SearchRequest request, int page, int size);

}
