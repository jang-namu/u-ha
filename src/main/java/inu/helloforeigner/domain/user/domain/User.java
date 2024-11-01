package inu.helloforeigner.domain.user.domain;

import inu.helloforeigner.domain.feed.domain.Feed;
import inu.helloforeigner.common.BaseTimeEntity;
import inu.helloforeigner.common.SoftDeletable;
import inu.helloforeigner.domain.user.dto.UserCreateRequest;
import inu.helloforeigner.domain.user.dto.UserUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


// User Entity
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

//    @Convert(converter = PasswordConverter.class)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    private String profileImage;

    @Column(nullable = false)
    private String preferredLanguage;

    private String major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "user")
    private List<UserInterest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Feed> feeds = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String country,
                String preferredLanguage, String major) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.country = country;
        this.preferredLanguage = preferredLanguage;
        this.major = major;
        this.status = UserStatus.ACTIVE;
    }

    // todo: 삭제
    public static User from(UserCreateRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .country(request.getCountry())
                .preferredLanguage(request.getLanguage()) // todo: 입력받기
                .major(request.getMajor())
                .build();
    }

    public void update(UserUpdateRequest request) {
        this.name = request.getName();
        this.country = request.getCountry();
        this.profileImage = request.getProfileImage();
        this.preferredLanguage = request.getPreferredLanguage();
        this.major = request.getMajor();
    }

    public void updateInterests(List<UserInterest> userInterests) {
        interests = userInterests;
    }
}
