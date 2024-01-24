package com.code.blog.services;

import java.util.List;

import com.code.blog.payloads.PostDto;
import com.code.blog.payloads.PostResponse;

public interface PostService {
	
	// create
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
		
	// update
	PostDto updatePost(PostDto postDto, Integer postId);
		
	// get
	PostDto getPostById(Integer postId);
		
	// getAll
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
		
	// delete
	void deletePost(Integer postId);
	
	// getAll posts by category
	List<PostDto> getAllPostsByCategory(Integer categoryId);
	
	// getAll posts by user
	List<PostDto> getAllPostsByUser(Integer userId);
	
	// search posts
	List<PostDto> searchPosts(String keyword);

}
