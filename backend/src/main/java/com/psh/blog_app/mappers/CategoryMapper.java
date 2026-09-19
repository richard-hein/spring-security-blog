package com.psh.blog_app.mappers;

import com.psh.blog_app.domain.PostStatus;
import com.psh.blog_app.domain.dtos.CategoryDto;
import com.psh.blog_app.domain.dtos.CreateCategoryRequest;
import com.psh.blog_app.domain.entities.Category;
import com.psh.blog_app.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source="posts", qualifiedByName = "calculatedPostCount" )
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatedPostCount")
    default long calculatePostCount(List<Post> posts) {
        if(null == posts){
            return 0;
        }
        return posts.stream()
                .filter(post-> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();

    }
}
