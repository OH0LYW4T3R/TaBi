package com.example.tabi.login.controller;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.login.JwtUtil;
import com.example.tabi.login.RefreshTokenService;
import com.example.tabi.login.vo.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Operation(
      summary = "액세스 토큰 갱신",
      description = "액세스 토큰 만료시 HttpOnly 쿠키 refreshToken을 통해 리프레시 토큰을 전달받아, 새로운 액세스 토큰을 Authorization(Bearer accessToken) 헤더에 담아 응답",
      parameters = {
          @Parameter(
              in = ParameterIn.COOKIE,
              name = "refreshToken",
              required = true,
              description = "리프레시 토큰이 담긴 HttpOnly 쿠키"
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "새 액세스 토큰 발급 성공",
              headers = @Header(
                  name = HttpHeaders.AUTHORIZATION,
                  description = "Bearer {newAccessToken}"
              )
          ),
          @ApiResponse(
            responseCode = "401",
            description = "리프레시 토큰 누락 또는 유효하지 않음"
          )
      }
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");

        if (cookie == null)
            return new ResponseEntity<>("No Refresh Token", HttpStatus.UNAUTHORIZED);

        String refreshToken = cookie.getValue();

        AppUser user = refreshTokenService.validateRefreshToken(refreshToken);

        if (user == null)
            return new  ResponseEntity<>("Invalid Refresh Token", HttpStatus.UNAUTHORIZED);

        String newAccessToken = jwtUtil.generateAccessToken(user.getMember().getEmail());
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

        return new ResponseEntity<>("Create New Access Token", HttpStatus.OK);
    }

    @Operation(
        summary = "로그아웃",
        description = "프론트에서는 Access Token을 삭제하고, 해당 주소로 로그아웃 시도",
        parameters = {
            @Parameter(
                    in = ParameterIn.COOKIE,
                    name = "refreshToken",
                    required = true,
                    description = "리프레시 토큰이 담긴 HttpOnly 쿠키"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "로그아웃 성공",
                headers = {
                    @Header(name = "Set-Cookie", description = "refreshToken=None;")
                }
            ),
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout( HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            accessToken = header.substring(7); // "Bearer " 이후 토큰만 추출
        }

        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        if (cookie != null) {
            String refreshToken = cookie.getValue();
            refreshTokenService.deleteByRefreshToken(refreshToken);
        }

        ResponseCookie expired = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build();
        response.setHeader(HttpHeaders.SET_COOKIE, expired.toString());

        return new ResponseEntity<>("Success Logout", HttpStatus.OK);
    }

    @Operation(
        summary = "로그인",
        description = "아이디/비밀번호로 로그인하고, 성공 시 Authorization 헤더(Bearer accessToken)와 HttpOnly 쿠키(refreshToken)를 발급",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                headers = {
                    @Header(name = HttpHeaders.AUTHORIZATION, description = "Bearer {accessToken}"),
                    @Header(name = "Set-Cookie", description = "refreshToken={refreshToken};")
                }
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패(잘못된 아이디/비번)")
        }
    )
    @PostMapping("/login")
    // 문서용 api
    public ResponseEntity<Void> loginDoc() {
        return ResponseEntity.noContent().build();
    }
}
