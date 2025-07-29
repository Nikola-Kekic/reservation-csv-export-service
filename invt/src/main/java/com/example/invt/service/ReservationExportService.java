package com.example.invt.service;

import com.example.invt.model.FlexibilityReservation;
import com.example.invt.repository.MockReservationRepository;
import com.example.invt.util.ReservationCsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.invt.constant.Constants.C1000;

@Service
public class ReservationExportService {

    @Autowired
    private MockReservationRepository mockReservationRepository;
    @Autowired
    private ReservationCsvUtil reservationCsvUtil;

    public ByteArrayInputStream exportReservationCsv(UUID assetId, UUID marketId) {
        var filtered = mockReservationRepository.findByAssetAndMarket(assetId, marketId).stream().collect(Collectors.toList());
        var convertedReservations = convertToMw(filtered);
        return reservationCsvUtil.exportReservationsToCsv(convertedReservations);
    }

    public ByteArrayInputStream exportReservationsCsv(UUID assetId, UUID marketId, LocalDateTime fromZoned, LocalDateTime toZoned, boolean total) {
        var zone = ZoneId.of("UTC");
        var filtered = mockReservationRepository.findByAssetAndMarket(assetId, marketId).stream()
                .filter(r -> r.getTimestamp().isAfter(fromZoned.atZone(zone)) && r.getTimestamp().isBefore(toZoned.atZone(zone))).collect(Collectors.toList());
        var convertedReservations = convertToMw(filtered);
        var calculatedReservations = total ? sumPositiveAndNegativeValues(convertedReservations) : convertedReservations;
        return reservationCsvUtil.exportReservationsToCsv(calculatedReservations);
    }

    private List<FlexibilityReservation> convertToMw(List<FlexibilityReservation> reservations) {
        return reservations.stream()
                .map( r -> FlexibilityReservation.builder()
                        .id(r.getId())
                        .timestamp(r.getTimestamp())
                        .assetId(r.getAssetId())
                        .marketId(r.getMarketId())
                        .positiveBidId(r.getPositiveBidId())
                        .negativeBidId(r.getNegativeBidId())
                        .positiveValue(r.getPositiveValue().multiply(C1000))
                        .positiveCapacityPrice(r.getPositiveCapacityPrice().multiply(C1000))
                        .positiveEnergyPrice(r.getPositiveEnergyPrice().multiply(C1000))
                        .negativeValue(r.getNegativeValue().multiply(C1000))
                        .negativeCapacityPrice(r.getNegativeCapacityPrice().multiply(C1000))
                        .negativeEnergyPrice(r.getNegativeEnergyPrice().multiply(C1000))
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private List<FlexibilityReservation> sumPositiveAndNegativeValues(List<FlexibilityReservation> reservations) {
        Map<String, FlexibilityReservation> grouped = reservations.stream()
                .collect(Collectors.toMap(
                        r -> r.getTimestamp().toString() + "|" + r.getAssetId() + "|" + r.getMarketId(),
                        r -> r,
                        (r1, r2) -> {
                            return FlexibilityReservation.builder()
                                    .id(r1.getId())
                                    .timestamp(r1.getTimestamp())
                                    .assetId(r1.getAssetId())
                                    .marketId(r1.getMarketId())
                                    .positiveValue(r1.getPositiveValue().add(r2.getPositiveValue()))
                                    .negativeValue(r1.getNegativeValue().add(r2.getNegativeValue()))
                                    .updatedAt(r1.getUpdatedAt())
                                    .positiveBidId(r1.getPositiveBidId())
                                    .negativeBidId(r1.getNegativeBidId())
                                    .positiveCapacityPrice(r1.getPositiveCapacityPrice())
                                    .negativeCapacityPrice(r1.getNegativeCapacityPrice())
                                    .positiveEnergyPrice(r1.getPositiveEnergyPrice())
                                    .negativeEnergyPrice(r1.getNegativeEnergyPrice())
                                    .build();
                        }

                ));

        return new ArrayList<>(grouped.values());
    }
}
