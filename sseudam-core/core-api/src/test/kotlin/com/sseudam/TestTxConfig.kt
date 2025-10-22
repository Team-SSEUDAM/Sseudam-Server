package com.sseudam

import com.sseudam.support.tx.Tx
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

@TestConfiguration
class TestTxConfig {
    @Bean
    fun platformTransactionManager(): PlatformTransactionManager = mockk(relaxed = true)

    @Bean
    fun tx(transactionManager: PlatformTransactionManager): Tx = Tx(Tx.TxAdvice(transactionManager))
}
