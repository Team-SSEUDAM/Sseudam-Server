package com.sseudam.fixture.auth

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.auth.AuthorityType
import com.sseudam.auth.Token
import com.sseudam.auth.command.CredentialSocialCommand
import com.sseudam.auth.command.CredentialSseudamCommand
import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.client.oauth.AppleClientResult
import com.sseudam.client.oauth.KaKaoClientResult
import com.sseudam.common.SocialType
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.user.SocialUser

object AuthFixture {
    val token =
        fixtureBuilder<Token> {
            setExp(Token::accessToken, "accessToken")
            setExp(Token::refreshToken, "refreshToken")
        }

    val credentialSseudamCommand =
        fixtureBuilder<CredentialSseudamCommand> {
            setExp(CredentialSseudamCommand::loginId, "test@test.com")
            setExp(CredentialSseudamCommand::password, "password")
        }

    val credentialSocialCommand =
        fixtureBuilder<CredentialSocialCommand> {
            setExp(CredentialSocialCommand::email, "test@kakao.com")
            setExp(CredentialSocialCommand::name, "카카오유저")
            setExp(CredentialSocialCommand::socialId, "socialId123")
            setExp(CredentialSocialCommand::socialType, SocialType.KAKAO)
        }

    val credentialNewSocialCommand =
        fixtureBuilder<CredentialSocialCommand> {
            setExp(CredentialSocialCommand::email, "test@apple.com")
            setExp(CredentialSocialCommand::name, "")
            setExp(CredentialSocialCommand::socialId, "socialId123")
            setExp(CredentialSocialCommand::socialType, SocialType.APPLE)
        }

    val socialUser =
        fixtureBuilder<SocialUser> {
            setExp(SocialUser::id, 1L)
            setExp(SocialUser::key, "userKey")
            setExp(SocialUser::name, "카카오유저")
            setExp(SocialUser::socialId, "socialId123")
            setExp(SocialUser::socialType, SocialType.KAKAO)
        }

    val socialNewUser =
        fixtureBuilder<SocialUser> {
            setExp(SocialUser::id, 1L)
            setExp(SocialUser::key, "userKey")
            setExp(SocialUser::name, "")
            setExp(SocialUser::socialId, "socialId123")
            setExp(SocialUser::socialType, SocialType.APPLE)
        }

    val grantedAuthority =
        fixtureBuilder<GrantedAuthority> {
            setExp(GrantedAuthority::authorityType, AuthorityType.ADMIN)
        }

    val kaKaoClientResult =
        fixtureBuilder<KaKaoClientResult> {
            setExp(KaKaoClientResult::id, "kakaoId123")
            setExp(KaKaoClientResult::email, "test@kakao.com")
            setExp(KaKaoClientResult::name, "카카오유저")
            setExp(KaKaoClientResult::nickname, "카카오닉네임")
        }

    val appleClientResult =
        fixtureBuilder<AppleClientResult> {
            setExp(AppleClientResult::id, "appleId123")
            setExp(AppleClientResult::email, "test@apple.com")
        }
}
