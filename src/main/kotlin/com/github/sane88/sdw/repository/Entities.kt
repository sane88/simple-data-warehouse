package com.github.sane88.sdw.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
class Datasource(var name: String, @Id @GeneratedValue var id: Long? = null)

@Entity
class Campaign(var name: String, @Id @GeneratedValue var id: Long? = null)

@Entity
class Metrics(
    var date: LocalDate,
    var clicks: Long,
    var impressions: Long,
    @ManyToOne var datasource: Datasource,
    @ManyToOne var campaign: Campaign,
    @Id @GeneratedValue var id: Long? = null,
)