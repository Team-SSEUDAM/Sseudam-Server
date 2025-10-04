package com.sseudam.fixture.common

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.test.helper.fixtureBuilder

object PageFixture {
    val offsetPageRequest =
        fixtureBuilder<OffsetPageRequest> {
            setExp(OffsetPageRequest::page, 0)
            setExp(OffsetPageRequest::size, 10)
        }
}
