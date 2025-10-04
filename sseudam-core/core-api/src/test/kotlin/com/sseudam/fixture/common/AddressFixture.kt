package com.sseudam.fixture.common

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.Address
import com.sseudam.test.helper.fixtureBuilder

object AddressFixture {
    val addressFixture =
        fixtureBuilder<Address> {
            setExp(Address::city, "구로구")
            setExp(Address::site, "서울특별시 구로구 가마산로 250")
        }
}
