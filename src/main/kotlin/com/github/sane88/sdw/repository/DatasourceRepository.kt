package com.github.sane88.sdw.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DatasourceRepository: CrudRepository<Datasource, Long> {
    fun findByName(name: String): Datasource?
}