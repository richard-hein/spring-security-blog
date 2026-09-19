package com.psh.blog_app.controllers;

import com.psh.blog_app.domain.dtos.CategoryDto;
import com.psh.blog_app.domain.dtos.CreateCategoryRequest;
import com.psh.blog_app.domain.entities.Category;
import com.psh.blog_app.mappers.CategoryMapper;
import com.psh.blog_app.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> listCategories() {
      List<CategoryDto> categories = categoryService.listCategories()
              .stream().map(categoryMapper::toDto)
              .toList();
      return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest categoryRequest) {
        Category categoryToCreate = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryService.createCategory(categoryToCreate);

        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                 HttpStatus.CREATED
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
