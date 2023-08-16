package study.book.gyhdoca.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountJpaEntity, Long>