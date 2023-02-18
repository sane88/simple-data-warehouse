package com.github.sane88.sdw.graphql.query

import com.github.sane88.sdw.graphql.model.Filter
import com.github.sane88.sdw.graphql.model.GroupBy
import com.github.sane88.sdw.service.MetricsQueryService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class Queries(private val metricsQueryService: MetricsQueryService) {

    @QueryMapping
    fun totalClicks(@Argument filter: Filter) = metricsQueryService.getTotalClicks(filter)

    @QueryMapping
    fun totalClicksGroupedBy(@Argument filter: Filter, @Argument groupBy: GroupBy) =
        metricsQueryService.getTotalClicksGroupedBy(filter, groupBy)

    @QueryMapping
    fun clicksRate(@Argument filter: Filter) =  metricsQueryService.getClicksRate(filter).toFloat()

    @QueryMapping
    fun impressionsOverTime(@Argument filter: Filter) = metricsQueryService.getImpressionsGroupedByDate(filter)
}
