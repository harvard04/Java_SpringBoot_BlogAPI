package com.code.blog.services;

import com.code.blog.payloads.CommentDto;

public interface CommentService {
	
	// create 
	CommentDto createComment(CommentDto commentDto, Integer postId);
	
	// delete
	void deleteComment(Integer commentId);
}
