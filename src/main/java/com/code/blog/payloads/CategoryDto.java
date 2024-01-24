package com.code.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDto {

	private Integer categoryId;
	
	@NotBlank
	@Size(min = 4, message = "title must be of minimum 4 characters")
	private String categoryTitle;
	
	@NotBlank
	@Size(min = 10, message = "description must be of minimum 10 characters")
	private String categoryDescription;
}
