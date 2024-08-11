package example.com.di

import example.com.application.service.AuthenticationService
import example.com.application.service.ExpenseServiceImpl
import example.com.application.service.ReportServiceImpl
import example.com.application.service.RevenueServiceImpl
import example.com.domain.repository.ExpenseRepository
import example.com.domain.repository.RevenueRepository
import example.com.domain.repository.UserRepository
import example.com.domain.service.ExpenseService
import example.com.domain.service.ReportService
import example.com.domain.service.RevenueService
import example.com.infrastructure.persistence.ExpenseRepositoryImpl
import example.com.infrastructure.persistence.RevenueRepositoryImpl
import example.com.infrastructure.persistence.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    single<ExpenseRepository> { ExpenseRepositoryImpl() }
    single<RevenueRepository> { RevenueRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    single { AuthenticationService(get()) }

    single<ExpenseService> { ExpenseServiceImpl(get()) }
    single<RevenueService> { RevenueServiceImpl(get()) }
    single<ReportService> { ReportServiceImpl(get(), get()) }

}