package com.github.sane88.sdw.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MetricsRepositoryCustomImpl(private val entityManager: EntityManager): MetricsRepositoryCustom {
    override fun getMetricsByCriteria(criteria: Criteria): List<Metrics> {
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(Metrics::class.java)
        val root = query.from(Metrics::class.java)

        val predicates = mutableListOf<Predicate>()
        criteria.datasource?.also { predicates.add(builder.equal(root.get<Datasource>("datasource").get<String>("name"), it)) }
        criteria.campaign?.also { predicates.add(builder.equal(root.get<Campaign>("campaign").get<String>("name"), it)) }
        criteria.from?.also { predicates.add(builder.greaterThanOrEqualTo(root.get("date"), it)) }
        criteria.to?.also { predicates.add(builder.lessThanOrEqualTo(root.get("date"), it)) }

        if (predicates.isNotEmpty()){
            query.where(*predicates.toTypedArray())
        }

        return entityManager.createQuery(query).resultList
    }
}

data class Criteria(
    val datasource: String?,
    val campaign: String?,
    val from: LocalDate?,
    val to: LocalDate?
)