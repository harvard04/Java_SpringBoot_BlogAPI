package com.code.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.blog.payloads.ApiResponse;
import com.code.blog.payloads.CategoryDto;
import com.code.blog.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	// POST - create Category
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto createdCategoryDto = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
	}
	
	// PUT - update Category
	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("catId") Integer catId){
		CategoryDto updatedCategoryDto = this.categoryService.updateCategory(categoryDto, catId);;
		return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
	}
	
	// DELETE - delete Category
	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("catId") Integer catId){
		this.categoryService.deleteCategory(catId);
		return new ResponseEntity<>(new ApiResponse("category deleted successfully", true), HttpStatus.OK);
	}
	
	// GET - get Category
	@GetMapping("/{catId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("catId") Integer catId){
		CategoryDto categoryDto = this.categoryService.getCategory(catId);
		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}
	
	// GET - get All Categories
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		List<CategoryDto> categoryDtoList = this.categoryService.getAllCategory();
		return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
	}
}
