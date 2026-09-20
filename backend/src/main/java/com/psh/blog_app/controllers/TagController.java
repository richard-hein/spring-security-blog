package com.psh.blog_app.controllers;

import com.psh.blog_app.domain.dtos.CreateTagRequest;
import com.psh.blog_app.domain.dtos.TagDto;
import com.psh.blog_app.domain.entities.Tag;
import com.psh.blog_app.mappers.TagMapper;
import com.psh.blog_app.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
     List<Tag> tags = tagService.getTags();
     List<TagDto> tagDtos = tags.stream().map(tagMapper::toTagDto).toList();
     return ResponseEntity.ok(tagDtos);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTabs(@RequestBody CreateTagRequest createTagRequest) {
       List<Tag> savedTags =  tagService.createTags(createTagRequest.getNames());
       List<TagDto> createdTagDto = savedTags.stream().map(tagMapper::toTagDto).toList();
       return ResponseEntity.ok(createdTagDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
      tagService.deleteTag(id);
      return ResponseEntity.noContent().build();
    }

}
