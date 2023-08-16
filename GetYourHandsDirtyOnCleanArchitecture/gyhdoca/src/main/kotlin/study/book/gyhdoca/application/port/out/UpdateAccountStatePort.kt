package study.book.gyhdoca.application.port.out

import study.book.gyhdoca.domain.Account

interface UpdateAccountStatePort {
    fun updateActivities(account: Account)
}