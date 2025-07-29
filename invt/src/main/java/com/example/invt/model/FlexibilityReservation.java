package com.example.invt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FlexibilityReservation {
    private Long id;
    private ZonedDateTime timestamp;
    private UUID assetId;
    private UUID marketId;
    private UUID positiveBidId;
    private UUID negativeBidId;
    private BigDecimal positiveValue;
    private BigDecimal positiveCapacityPrice;
    private BigDecimal positiveEnergyPrice;
    private BigDecimal negativeValue;
    private BigDecimal negativeCapacityPrice;
    private BigDecimal negativeEnergyPrice;
    private ZonedDateTime updatedAt;
}
