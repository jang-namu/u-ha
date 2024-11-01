package inu.helloforeigner.domain.feed.controller;

import inu.helloforeigner.domain.feed.dto.FeedCreateResponse;
import inu.helloforeigner.domain.feed.dto.FeedLikeResponse;
import inu.helloforeigner.domain.feed.dto.FeedUpdateResponse;
import inu.helloforeigner.domain.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Feed", description = "피드 API")
@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 업로드", description = "새로운 피드를 업로드합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedCreateResponse> uploadFeed(
            @RequestPart("image") MultipartFile image,
            @RequestParam("content") String content,
            @RequestParam Long userId) {
        return ResponseEntity.ok(feedService.uploadFeed(userId, image, content));
    }

    @Operation(summary = "피드 수정", description = "기존 피드를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(path="/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedUpdateResponse> updateFeed(
            @PathVariable("feedId") Long feedId,
            @RequestPart("image") MultipartFile image,
            @RequestParam("content") String content,
            @RequestParam Long userId) {
        return ResponseEntity.ok(feedService.updateFeed(feedId, userId, image, content));
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "좋아요 토글", description = "피드에 대한 좋아요를 토글합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{feedId}/likes")
    public ResponseEntity<FeedLikeResponse> toggleLike(@PathVariable("feedId") Long feedId, @RequestParam Long userId) {
        return ResponseEntity.ok(feedService.toggleLike(feedId, userId));
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> getFeeds(@PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(feedService.getFeeds(pageable));
    }
}
