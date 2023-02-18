package com.github.sane88.sdw.repository

import org.springframework.data.repository.CrudRepository

interface MetricsRepository: CrudRepository<Metrics, Long>, MetricsRepositoryCustom