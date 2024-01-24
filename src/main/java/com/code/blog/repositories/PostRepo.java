package com.code.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.blog.entities.Category;
import com.code.blog.entities.Post;
import com.code.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer>{
	
	List<Post> findByUser(User user);
	
	List<Post> findByCategory(Category category);
	
	List<Post> findByTitleContaining(String title);

	// this method is same as the method above i.e findByTitleContaining, it is just that here we are searching by using a custom query
	@Query("select p from Post p where p.title like :key")
	List<Post> searchByTitle(@Param("key") String title);
}
