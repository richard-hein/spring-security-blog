package com.psh.blog_app.services.impl;

import com.psh.blog_app.domain.CreatePostRequest;
import com.psh.blog_app.domain.PostStatus;
import com.psh.blog_app.domain.UpdatePostRequest;
import com.psh.blog_app.domain.entities.Category;
import com.psh.blog_app.domain.entities.Post;
import com.psh.blog_app.domain.entities.Tag;
import com.psh.blog_app.domain.entities.User;
import com.psh.blog_app.repositories.PostRepository;
import com.psh.blog_app.services.CategoryService;
import com.psh.blog_app.services.PostService;
import com.psh.blog_app.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE =200;

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Post with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null) {
           Category category = categoryService.getCategory(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
                                                                             );
        }
        if(categoryId != null) {
            Category category = categoryService.getCategory(categoryId);
           return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
                                                     );
        }
        if(tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag);

        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);

    }

    @Override
    public List<Post> getAllDraftPosts(User user) {
       return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);

    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategory(createPostRequest.getCategoryId());
        newPost.setCategory(category);

       Set<UUID> tagIds= createPostRequest.getTagIds();
       List<Tag> tags = tagService.getTagsById(tagIds);
       newPost.setTags(new HashSet<>(tags));
       return postRepository.save(newPost);

    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Post doesn't exist with id "+ id));
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category newCategory = categoryService.getCategory(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.getTagsById(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }


        return postRepository.save(existingPost);
    }

    @Override
    public void deletePostById(UUID id) {
        Post post = getPostById(id);
        postRepository.delete(post);

    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount/WORDS_PER_MINUTE);

    }
}
