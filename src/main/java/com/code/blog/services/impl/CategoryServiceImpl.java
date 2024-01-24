package com.code.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.blog.entities.Category;
import com.code.blog.exceptions.ResourceNotFoundException;
import com.code.blog.payloads.CategoryDto;
import com.code.blog.repositories.CategoryRepo;
import com.code.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category saveCategory = this.categoryRepo.save(category);
		return this.modelMapper.map(saveCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		// get the category from DB using the categoryId
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		// once we get the category saved in DB than update the data from categoryDto in category object we just fetched from the DB
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		// once we have updated the category save it back to DB
		Category updatedCategory = this.categoryRepo.save(category);
		// and return the new updated category object after converting it back to CategoryDto
		return this.modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categoryList = this.categoryRepo.findAll();
		List<CategoryDto> categoryDtoList = categoryList.stream()
				.map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
		return categoryDtoList;
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		this.categoryRepo.delete(category);
	}

}
