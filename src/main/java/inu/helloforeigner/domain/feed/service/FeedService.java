package inu.helloforeigner.domain.feed.service;

import inu.helloforeigner.common.PagingResponse;
import inu.helloforeigner.domain.feed.domain.Feed;
import inu.helloforeigner.domain.feed.domain.Like;
import inu.helloforeigner.domain.feed.dto.FeedCreateResponse;
import inu.helloforeigner.domain.feed.dto.FeedLikeResponse;
import inu.helloforeigner.domain.feed.dto.FeedResponse;
import inu.helloforeigner.domain.feed.dto.FeedUpdateResponse;
import inu.helloforeigner.domain.feed.repository.FeedRepository;
import inu.helloforeigner.domain.feed.repository.LikeRepository;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.dto.InterestResponse;
import inu.helloforeigner.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final FeedRepository feedRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedCreateResponse uploadFeed(Long userId, MultipartFile file, String content) {
        // 파일 유효성 검사
        validateFile(file);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // S3에 이미지 업로드
        String s3Key = generateS3Key(userId, file.getOriginalFilename());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ.toString())
                .build();

        try {
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to upload image");
        }

        // S3 URL 생성
        String imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);

        Feed feed = Feed.builder()
                .image(imageUrl)
                .content(content)
                .user(user)
                .s3Key(s3Key)
                .build();
        feedRepository.save(feed);
        return FeedCreateResponse.from(feed);
    }

    @Transactional
    public FeedUpdateResponse updateFeed(Long feedId, Long userId, MultipartFile file, String content) {
        // todo: 소유자 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (file != null) {
            validateFile(file);

            String s3Key = generateS3Key(userId, file.getOriginalFilename());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ.toString())
                    .build();

            try {
                s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to upload image");
            }

            // S3 URL 생성
            String imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);

            feed.updateImage(imageUrl, s3Key);
        }
        feed.updateContent(content);
        feedRepository.save(feed);
        return FeedUpdateResponse.from(feed);
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        // todo: 소유자 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found"));

        // S3 이미지 삭제
//        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(feed.getS3Key()));
        feed.delete();
        feedRepository.save(feed);
    }

    @Transactional
    public FeedLikeResponse toggleLike(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<Like> maybeLike = likeRepository.findByFeedAndUser(feed, user);
        if (maybeLike.isPresent()) {
            Like like = maybeLike.get();
            like.toggleLike();
            likeRepository.save(like);
            feedRepository.save(feed);
            return FeedLikeResponse.from(feedId, like.getDeletedAt() == null, feed.getNumOfLikes());
        }
        Like newLike = Like.builder().feed(feed).user(user).build();
        likeRepository.save(newLike);
        feedRepository.save(feed);
        return FeedLikeResponse.from(feedId, true, feed.getNumOfLikes());
    }

    @Transactional(readOnly = true)
    public PagingResponse<FeedResponse> getFeeds(Pageable pageable) {
        Page<Feed> feeds = feedRepository.findAll(pageable);

        List<FeedResponse> feedResponses = new ArrayList<>();
        for (Feed feed : feeds) {
            User user = feed.getUser();
            List<InterestResponse> interestResponses = user.getInterests().stream()
                    .map(userInterest -> InterestResponse.from(userInterest, userInterest.getInterest()))
                    .collect(Collectors.toList());
            feedResponses.add(FeedResponse.of(feed, user, interestResponses));
        }

        return PagingResponse.of(feedResponses, pageable, feeds.getTotalElements());
    }

    private String generateS3Key(Long userId, String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return String.format("feeds/%d/%s.%s", userId, UUID.randomUUID(), extension);
    }

    private void validateFile(MultipartFile file) {
        // 파일 크기 검사 (예: 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }

        // 파일 형식 검사
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
    }
}
