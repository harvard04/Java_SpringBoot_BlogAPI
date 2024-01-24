package com.code.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.code.blog.config.AppConstants;
import com.code.blog.payloads.ApiResponse;
import com.code.blog.payloads.PostDto;
import com.code.blog.payloads.PostResponse;
import com.code.blog.services.FileService;
import com.code.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	// POST - create post
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(
			@RequestBody PostDto PostDto, 
			@PathVariable Integer userId, 
			@PathVariable Integer categoryId){
		PostDto createdPostDto = this.postService.createPost(PostDto, userId, categoryId);
		return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
	}
	
	// GET - get Post by User
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
		List<PostDto> postDtoList = this.postService.getAllPostsByUser(userId);
		return new ResponseEntity<>(postDtoList, HttpStatus.OK);
	}
	
	// GET - get Post by Category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId){
		List<PostDto> postDtoList = this.postService.getAllPostsByCategory(categoryId);
		return new ResponseEntity<>(postDtoList, HttpStatus.OK);
	}
	
	// GET - get Post by Id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Integer postId){
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<>(postDto, HttpStatus.OK);
	}
	
	// GET - get All Posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}
	
	// PUT - update Category
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updateCategory(@RequestBody PostDto postDto, @PathVariable("postId") Integer postId){
		PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<>(updatedPostDto, HttpStatus.OK);
	}
	
	// DELETE - delete Category
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Integer postId){
		this.postService.deletePost(postId);
		return new ResponseEntity<>(new ApiResponse("category deleted successfully", true), HttpStatus.OK);
	}
	
	// SEARCH - search
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
		List<PostDto> result = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}
	
	// POST - image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable("postId") Integer postId) throws IOException{
		
		// first check if postId exists or not if not then throw exception
		PostDto postDto = this.postService.getPostById(postId);
		
		// fileName we get after uploading the image
		String fileName = this.fileService.uploadImage(path, image);
		// we set that fileName in the postDto
		postDto.setImageName(fileName);
		
		PostDto updatedPost =this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}
	
	// GET - get image from DB
	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
} 
