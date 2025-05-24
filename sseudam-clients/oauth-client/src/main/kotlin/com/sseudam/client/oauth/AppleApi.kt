package com.sseudam.client.oauth

import com.sseudam.client.oauth.response.ApplePublicKeysResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "apple-auth-api", url = "https://appleid.apple.com")
internal interface AppleApi {
    @GetMapping("/auth/keys")
    fun getApplePublicKeys(): ApplePublicKeysResponse
}
