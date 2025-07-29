package com.example.invt.repository;

import com.example.invt.model.FlexibilityReservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


/**
 * Mock implementation of a repository for retrieving {@link FlexibilityReservation} objects.
 */
@Component
public class MockReservationRepository {

    public List<FlexibilityReservation> findByAssetAndMarket(UUID assetId, UUID marketId) {
        return List.of(
                FlexibilityReservation.builder()
                        .id(1L)
                        .timestamp(ZonedDateTime.parse("2022-10-24T14:15:22Z"))
                        .assetId(assetId)
                        .marketId(marketId)
                        .positiveBidId(UUID.randomUUID())
                        .negativeBidId(UUID.randomUUID())
                        .positiveValue(BigDecimal.valueOf(200_000))
                        .positiveCapacityPrice(BigDecimal.valueOf(1.5))
                        .positiveEnergyPrice(BigDecimal.valueOf(1.5))
                        .negativeValue(BigDecimal.valueOf(250_000))
                        .negativeCapacityPrice(BigDecimal.valueOf(2.0))
                        .negativeEnergyPrice(BigDecimal.valueOf(2.0))
                        .updatedAt(ZonedDateTime.parse("2022-10-10T11:42:12.794363Z"))
                        .build(),

                FlexibilityReservation.builder()
                        .id(2L)
                        .timestamp(ZonedDateTime.parse("2022-10-24T14:15:22Z"))
                        .assetId(assetId)
                        .marketId(marketId)
                        .positiveBidId(UUID.randomUUID())
                        .negativeBidId(UUID.randomUUID())
                        .positiveValue(BigDecimal.valueOf(100_000))
                        .positiveCapacityPrice(BigDecimal.valueOf(1.0))
                        .positiveEnergyPrice(BigDecimal.valueOf(1.0))
                        .negativeValue(BigDecimal.valueOf(150_000))
                        .negativeCapacityPrice(BigDecimal.valueOf(1.8))
                        .negativeEnergyPrice(BigDecimal.valueOf(1.8))
                        .updatedAt(ZonedDateTime.parse("2022-10-10T11:42:12.794363Z"))
                        .build(),

                FlexibilityReservation.builder()
                        .id(3L)
                        .timestamp(ZonedDateTime.parse("2022-10-20T14:15:22Z"))
                        .assetId(assetId)
                        .marketId(marketId)
                        .positiveBidId(UUID.randomUUID())
                        .negativeBidId(UUID.randomUUID())
                        .positiveValue(BigDecimal.valueOf(100_000))
                        .positiveCapacityPrice(BigDecimal.valueOf(1.0))
                        .positiveEnergyPrice(BigDecimal.valueOf(1.0))
                        .negativeValue(BigDecimal.valueOf(150_000))
                        .negativeCapacityPrice(BigDecimal.valueOf(1.8))
                        .negativeEnergyPrice(BigDecimal.valueOf(1.8))
                        .updatedAt(ZonedDateTime.parse("2022-10-10T11:42:12.794363Z"))
                        .build(),

                FlexibilityReservation.builder()
                        .id(4L)
                        .timestamp(ZonedDateTime.parse("2023-11-24T14:15:22Z"))
                        .assetId(assetId)
                        .marketId(marketId)
                        .positiveBidId(UUID.randomUUID())
                        .negativeBidId(UUID.randomUUID())
                        .positiveValue(BigDecimal.valueOf(110_000))
                        .positiveCapacityPrice(BigDecimal.valueOf(1.0))
                        .positiveEnergyPrice(BigDecimal.valueOf(1.0))
                        .negativeValue(BigDecimal.valueOf(110_000))
                        .negativeCapacityPrice(BigDecimal.valueOf(1.5))
                        .negativeEnergyPrice(BigDecimal.valueOf(1.5))
                        .updatedAt(ZonedDateTime.parse("2023-01-02T11:42:12.794363Z"))
                        .build()
        );
    }
}
