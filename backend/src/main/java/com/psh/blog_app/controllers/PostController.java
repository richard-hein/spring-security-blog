package com.psh.blog_app.controllers;

import com.psh.blog_app.domain.CreatePostRequest;
import com.psh.blog_app.domain.UpdatePostRequest;
import com.psh.blog_app.domain.dtos.CreatePostRequestDto;
import com.psh.blog_app.domain.dtos.PostDto;
import com.psh.blog_app.domain.dtos.UpdatePostRequestDto;
import com.psh.blog_app.domain.entities.Post;
import com.psh.blog_app.domain.entities.User;
import com.psh.blog_app.mappers.PostMapper;
import com.psh.blog_app.security.BlogUserDetails;
import com.psh.blog_app.services.PostService;
import com.psh.blog_app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
            ) {
     List<Post> posts=  postService.getAllPosts(categoryId, tagId);
     List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
     return ResponseEntity.ok(postDtos);

    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getAllDrafts(@AuthenticationPrincipal BlogUserDetails userDetails){
        User loggedInUser = userDetails.getUser();
        List<Post> draftPosts = postService.getAllDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);

    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal BlogUserDetails userDetails
                                             ){
        User loggedInUser = userDetails.getUser();
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
            ){
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);
        return  ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id  ){
        Post post = postService.getPostById(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id
                                          ){
         postService.deletePostById(id);
         return ResponseEntity.noContent().build();
    }

}
