package com.code.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.code.blog.entities.Category;
import com.code.blog.entities.Post;
import com.code.blog.entities.User;
import com.code.blog.exceptions.ResourceNotFoundException;
import com.code.blog.payloads.PostDto;
import com.code.blog.payloads.PostResponse;
import com.code.blog.repositories.CategoryRepo;
import com.code.blog.repositories.PostRepo;
import com.code.blog.repositories.UserRepo;
import com.code.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
		
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepo.findAll(pageable);
		List<Post> allPosts = pagePost.getContent();
		List<PostDto> allPostsDto = allPosts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(allPostsDto);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalElements(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		return postResponse;
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public List<PostDto> getAllPostsByCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> postsDto = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

	@Override
	public List<PostDto> getAllPostsByUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postsDto = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;		
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		//List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<Post> posts = this.postRepo.searchByTitle("%"+ keyword + "%");
		List<PostDto> postsDto = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

}
