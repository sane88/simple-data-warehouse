package com.github.sane88.sdw.graphql.model

data class Filter(val datasource: String?, val campaign: String?, val range: DateRange?)

data class DateRange(val from: String?, val to: String?)

data class Group(val key: String, val value: Long)

enum class GroupBy {
    DATASOURCE, CAMPAIGN, DATE
}