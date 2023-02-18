package com.github.sane88.sdw

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.sane88.sdw.repository.Campaign
import com.github.sane88.sdw.repository.CampaignRepository
import com.github.sane88.sdw.repository.Datasource
import com.github.sane88.sdw.repository.DatasourceRepository
import com.github.sane88.sdw.repository.Metrics
import com.github.sane88.sdw.repository.MetricsRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class DataImporter(
    private val datasourceRepository: DatasourceRepository,
    private val campaignRepository: CampaignRepository,
    private val metricsRepository: MetricsRepository,
    @Value("classpath:data.csv") private val dataFile: Resource
) {

    companion object {
        val logger = LoggerFactory.getLogger(DataImporter::class.java)
    }

    @PostConstruct
    fun init() {
        logger.info("Ingesting data from the file...")
        csvReader().open(dataFile.inputStream) {
            val rowsImported = readAllWithHeaderAsSequence().map { row ->
                val datasource = row[Header.DATASOURCE.value]?.let {
                    datasourceRepository.findByName(it) ?: datasourceRepository.save(Datasource(name = it))
                } ?: throw ImportingException(Header.DATASOURCE.value)
                val campaign = row[Header.CAMPAIGN.value]?.let {
                    campaignRepository.findByName(it) ?: campaignRepository.save(Campaign(name = it))
                } ?: throw ImportingException(Header.CAMPAIGN.value)
                Metrics(
                    datasource = datasource,
                    campaign = campaign,
                    date = row[Header.DAILY.value]?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("MM/dd/yy")) }
                        ?: throw ImportingException(Header.DAILY.value),
                    clicks = row[Header.CLICKS.value]?.toLong() ?: throw ImportingException(Header.CLICKS.value),
                    impressions = row[Header.IMPRESSIONS.value]?.toLong() ?: throw ImportingException(Header.IMPRESSIONS.value)
                )
            }.map { metricsRepository.save(it) }.count()
            logger.info("$rowsImported rows imported")
        }
    }
}

class ImportingException(header: String) : RuntimeException("Data file import failed: header $header is not present")

enum class Header(val value: String) {
    DATASOURCE("Datasource"),
    CAMPAIGN("Campaign"),
    DAILY("Daily"),
    CLICKS("Clicks"),
    IMPRESSIONS("Impressions");
}