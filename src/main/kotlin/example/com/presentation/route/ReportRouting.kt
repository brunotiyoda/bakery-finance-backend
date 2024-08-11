package example.com.presentation.route

import example.com.domain.service.ReportService
import example.com.presentation.route.responses.DailyTotalResponse
import example.com.presentation.route.responses.MonthlyTotalResponse
import example.com.presentation.route.responses.PeriodReportResponse
import example.com.utils.authenticatedAdmin
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject

fun Application.reportRouting() {

    val service by inject<ReportService>()

    routing {
        authenticate("auth-jwt") {
            route("/reports") {
                get("/daily/total/{date}") {
                    authenticatedAdmin {
                        val date = call.parameters["date"] ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Date is required"
                        )
                        val totalDaily = service.dailyTotal(date)
                        val response = DailyTotalResponse(
                            revenue = totalDaily.revenues.toDouble(),
                            expense = totalDaily.expenses.toDouble(),
                            netBalance = totalDaily.netBalance.toDouble()
                        )
                        call.respond(response)
                    }
                }

                get("/by-registered/{date}") {
                    authenticatedAdmin {
                        val date = call.parameters["date"] ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Date is required"
                        )
                        val response = service.getSumOfRevenuesByRegistrarAndDate(date)
                        call.respond(response)
                    }
                }

                get("/period/{startDate}/{endDate}") {
                    authenticatedAdmin {
                        val startDate = call.parameters["startDate"] ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Data inicial é obrigatória"
                        )
                        val endDate = call.parameters["endDate"] ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Data final é obrigatória"
                        )

                        val periodReport = service.getPeriodReport(LocalDate.parse(startDate), LocalDate.parse(endDate))
                        val response = PeriodReportResponse.fromDomainModel(periodReport)
                        call.respond(response)
                    }
                }

                get("/monthly/total/{year}/{month}") {
                    authenticatedAdmin {
                        val year = call.parameters["year"]?.toIntOrNull() ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Valid year is required"
                        )
                        val month = call.parameters["month"]?.toIntOrNull() ?: return@authenticatedAdmin call.respond(
                            HttpStatusCode.BadRequest,
                            "Valid month is required"
                        )
                        val totalMonthly = service.monthlyTotal(month, year)
                        val response = MonthlyTotalResponse(
                            revenue = totalMonthly.revenues.toDouble(),
                            expense = totalMonthly.expenses.toDouble(),
                            netBalance = totalMonthly.netBalance.toDouble()
                        )
                        call.respond(response)
                    }
                }
            }
        }
    }
}