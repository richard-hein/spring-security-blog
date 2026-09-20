package com.psh.blog_app.services.impl;

import com.psh.blog_app.domain.entities.Category;
import com.psh.blog_app.repositories.CategoryRepository;
import com.psh.blog_app.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
       Optional<Category> category = categoryRepository.findById(id);
       if(category.isPresent()){
           if(!category.get().getPosts().isEmpty()) {
               throw new IllegalStateException("Category has posts associated with it");
           }
           categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category getCategory(UUID id) {
       return categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found with id " + id));

    }

}
