package com.github.sane88.sdw.service

import com.github.sane88.sdw.graphql.model.Filter
import com.github.sane88.sdw.graphql.model.Group
import com.github.sane88.sdw.graphql.model.GroupBy
import com.github.sane88.sdw.repository.Criteria
import com.github.sane88.sdw.repository.MetricsRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate

@Service
class MetricsQueryService(private val metricsRepository: MetricsRepository) {

    fun getTotalClicks(filter: Filter) =
        metricsRepository.getMetricsByCriteria(filter.toCriteria()).sumOf { it.clicks }

    fun getTotalClicksGroupedBy(filter: Filter, groupBy: GroupBy) =
        metricsRepository.getMetricsByCriteria(filter.toCriteria())
            .groupBy {
                when (groupBy) {
                    GroupBy.DATASOURCE -> it.datasource.name
                    GroupBy.CAMPAIGN -> it.campaign.name
                    GroupBy.DATE -> it.date
                }
            }.map {
                Group(it.key.toString(), it.value.sumOf { it.clicks })
            }

    fun getClicksRate(filter: Filter): BigDecimal {
        val (totalClicks, totalImpressions) = metricsRepository.getMetricsByCriteria(filter.toCriteria())
            .map { it.clicks to it.impressions }
            .reduce { acc, pair -> acc.first + pair.first to acc.second + pair.second }
        return totalClicks.toBigDecimal().divide(totalImpressions.toBigDecimal(), MathContext(2, RoundingMode.HALF_UP))
    }

    fun getImpressionsGroupedByDate(filter: Filter) =
        metricsRepository.getMetricsByCriteria(filter.toCriteria())
            .groupBy { it.date }
            .map { Group(it.key.toString(), it.value.sumOf { it.impressions }) }

}

private fun Filter.toCriteria() =
    Criteria(
        this.datasource,
        this.campaign,
        this.range?.from?.let { LocalDate.parse(it) },
        this.range?.to?.let { LocalDate.parse(it) }
    )