package com.psh.blog_app.mappers;

import com.psh.blog_app.domain.CreatePostRequest;
import com.psh.blog_app.domain.UpdatePostRequest;
import com.psh.blog_app.domain.dtos.CreatePostRequestDto;
import com.psh.blog_app.domain.dtos.PostDto;
import com.psh.blog_app.domain.dtos.UpdatePostRequestDto;
import com.psh.blog_app.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
