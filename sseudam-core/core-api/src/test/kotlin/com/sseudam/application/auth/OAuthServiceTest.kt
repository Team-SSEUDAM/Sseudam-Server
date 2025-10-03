package com.sseudam.application.auth

import com.sseudam.DevelopTest
import com.sseudam.client.oauth.AppleClient
import com.sseudam.client.oauth.AppleClientResult
import com.sseudam.client.oauth.KaKaoClient
import com.sseudam.client.oauth.KaKaoClientResult
import com.sseudam.client.oauth.OAuthService
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import feign.FeignException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class OAuthServiceTest :
    DescribeSpec({
        val kaKaoClient: KaKaoClient = mockk()
        val appleClient: AppleClient = mockk()
        val oAuthService = OAuthService(kaKaoClient, appleClient)

        describe("카카오 사용자 정보 조회") {
            context("유효한 토큰인 경우") {
                it("카카오 사용자 정보를 반환한다") {
                    val token = "valid_kakao_token"
                    val expected = KaKaoClientResult("kakaoId123", "test@kakao.com", "카카오유저", "닉네임")

                    every { kaKaoClient.getUserInfo(token) } returns expected

                    val result = oAuthService.getKaKaoUserInfo(token)

                    result shouldBe expected
                    verify { kaKaoClient.getUserInfo(token) }
                }
            }

            context("401 에러가 발생한 경우") {
                it("INVALID_KAKAO_TOKEN 예외를 던진다") {
                    val token = "invalid_token"
                    val feignException: FeignException = mockk()

                    every { feignException.status() } returns 401
                    every { kaKaoClient.getUserInfo(token) } throws feignException

                    val exception =
                        shouldThrow<AuthenticationErrorException> {
                            oAuthService.getKaKaoUserInfo(token)
                        }

                    exception.authenticationErrorType shouldBe AuthenticationErrorType.INVALID_KAKAO_TOKEN
                }
            }

            context("401이 아닌 다른 에러가 발생한 경우") {
                it("INVALID_KAKAO_TOKEN 예외를 메시지와 함께 던진다") {
                    val token = "error_token"
                    val feignException: FeignException = mockk()

                    every { feignException.status() } returns 500
                    every { feignException.message } returns "Internal Server Error"
                    every { kaKaoClient.getUserInfo(token) } throws feignException

                    val exception =
                        shouldThrow<AuthenticationErrorException> {
                            oAuthService.getKaKaoUserInfo(token)
                        }

                    exception.authenticationErrorType shouldBe AuthenticationErrorType.INVALID_KAKAO_TOKEN
                    exception.data shouldBe "Internal Server Error"
                }
            }

            context("빈 토큰인 경우") {
                it("카카오 클라이언트를 호출한다") {
                    val token = ""
                    val expected = KaKaoClientResult("id", "email@test.com", "name", "nickname")

                    every { kaKaoClient.getUserInfo(token) } returns expected

                    val result = oAuthService.getKaKaoUserInfo(token)

                    result shouldBe expected
                }
            }
        }

        describe("애플 사용자 정보 조회") {
            context("유효한 토큰인 경우") {
                it("애플 사용자 정보를 반환한다") {
                    val token = "valid_apple_token"
                    val expected = AppleClientResult("appleId123", "test@apple.com")

                    every { appleClient.verify(token) } returns true
                    every { appleClient.getUserInfo(token) } returns expected

                    val result = oAuthService.getAppleUserInfo(token)

                    result shouldBe expected
                    verify { appleClient.verify(token) }
                    verify { appleClient.getUserInfo(token) }
                }
            }

            context("토큰 검증에 실패한 경우") {
                it("INVALID_APPLE_TOKEN 예외를 던진다") {
                    val token = "invalid_apple_token"

                    every { appleClient.verify(token) } returns false

                    val exception =
                        shouldThrow<AuthenticationErrorException> {
                            oAuthService.getAppleUserInfo(token)
                        }

                    exception.authenticationErrorType shouldBe AuthenticationErrorType.INVALID_APPLE_TOKEN
                }
            }

            context("빈 토큰인 경우") {
                it("INVALID_APPLE_TOKEN 예외를 던진다") {
                    val token = ""

                    every { appleClient.verify(token) } returns false

                    val exception =
                        shouldThrow<AuthenticationErrorException> {
                            oAuthService.getAppleUserInfo(token)
                        }

                    exception.authenticationErrorType shouldBe AuthenticationErrorType.INVALID_APPLE_TOKEN
                }
            }

            context("토큰 검증은 성공했지만 사용자 정보 조회 중 예외가 발생한 경우") {
                it("예외를 그대로 전파한다") {
                    val token = "valid_but_error_token"

                    every { appleClient.verify(token) } returns true
                    every { appleClient.getUserInfo(token) } throws RuntimeException("Unexpected error")

                    shouldThrow<RuntimeException> {
                        oAuthService.getAppleUserInfo(token)
                    }
                }
            }
        }
    })
