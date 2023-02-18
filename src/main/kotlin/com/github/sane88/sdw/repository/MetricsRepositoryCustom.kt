package com.github.sane88.sdw.repository

interface MetricsRepositoryCustom {
    fun getMetricsByCriteria(criteria: Criteria): List<Metrics>
}