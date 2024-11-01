package inu.helloforeigner.domain.user.controller;

import inu.helloforeigner.common.exception.ErrorResponse;
import inu.helloforeigner.domain.user.dto.InterestUpdateResponse;
import inu.helloforeigner.domain.user.dto.UserInterestUpdateRequest;
import inu.helloforeigner.domain.user.dto.UserProfileResponse;
import inu.helloforeigner.domain.user.dto.UserResponse;
import inu.helloforeigner.domain.user.dto.UserUpdateRequest;
import inu.helloforeigner.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 정보 조회", description = "사용자 ID로 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUser(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "관심사 등록", description = "관심사를 등록합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/interests")
    public ResponseEntity<InterestUpdateResponse> updateInterests(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid UserInterestUpdateRequest request) {
        return ResponseEntity.ok(userService.updateInterest(id, request));
    }

}
