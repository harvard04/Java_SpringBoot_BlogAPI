package com.code.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.blog.entities.Comment;
import com.code.blog.entities.Post;
import com.code.blog.exceptions.ResourceNotFoundException;
import com.code.blog.payloads.CommentDto;
import com.code.blog.repositories.CommentRepo;
import com.code.blog.repositories.PostRepo;
import com.code.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		// fetch the post by postId
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
		// convert CommentDto to comment
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		// set the post in the comment
		comment.setPost(post);
		// save the comment in comment table
		Comment savedComment = this.commentRepo.save(comment);
		// return the saved comment object after converting it to CommentDto
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment id", commentId));
		this.commentRepo.delete(comment);
	}

}
