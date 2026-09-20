package com.psh.blog_app.mappers;

import com.psh.blog_app.domain.PostStatus;
import com.psh.blog_app.domain.dtos.TagDto;
import com.psh.blog_app.domain.entities.Post;
import com.psh.blog_app.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target="postCount", source="posts", qualifiedByName = "calculatePostCount")
    TagDto toTagDto(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if(posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post-> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }



}
