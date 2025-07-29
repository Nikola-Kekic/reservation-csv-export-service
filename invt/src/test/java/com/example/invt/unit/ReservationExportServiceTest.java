package com.example.invt.unit;

import com.example.invt.model.FlexibilityReservation;
import com.example.invt.repository.MockReservationRepository;
import com.example.invt.service.ReservationExportService;
import com.example.invt.util.ReservationCsvUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReservationExportServiceTest {

    @Mock
    private MockReservationRepository mockReservationRepository;

    @Mock
    private ReservationCsvUtil reservationCsvUtil;
    @InjectMocks
    private ReservationExportService reservationExportService;

    private static final UUID assetId = UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8");
    private static final UUID marketId = UUID.fromString("8a5075bf-2552-4119-b135-61ddcfd37ba2");

    @Test
    public void exportReservationCsv_returnsCsvStream_givenValidInput() {
        // Arrange
        var reservations = generateReservations();
        ByteArrayInputStream dummyCsv = new ByteArrayInputStream("csv,data".getBytes());

        when(mockReservationRepository.findByAssetAndMarket(assetId, marketId)).thenReturn(reservations);
        when(reservationCsvUtil.exportReservationsToCsv(anyList())).thenReturn(dummyCsv);

        // Act
        ByteArrayInputStream result = reservationExportService.exportReservationCsv(assetId, marketId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(new String(result.readAllBytes())).isEqualTo("csv,data");
        verify(mockReservationRepository).findByAssetAndMarket(assetId, marketId);
        verify(reservationCsvUtil).exportReservationsToCsv(anyList());
    }

    @Test
    public void exportReservationsCsv_filtersAndProcessesCorrectly() throws IOException {
        // Arrange
        var allReservations = generateReservations();
        LocalDateTime from = LocalDateTime.of(2022, 10, 23, 0, 0);
        LocalDateTime to = LocalDateTime.of(2022, 10, 25, 0, 0);
        var zone = ZoneId.of("UTC");
        ByteArrayInputStream dummyCsv = new ByteArrayInputStream("csv,data".getBytes());

        when(mockReservationRepository.findByAssetAndMarket(assetId, marketId))
                .thenReturn(allReservations);
        when(reservationCsvUtil.exportReservationsToCsv(anyList()))
                .thenReturn(dummyCsv);

        // Act
        ByteArrayInputStream result = reservationExportService.exportReservationsCsv(assetId, marketId, from, to, false);

        // Assert
        assertThat(result).isNotNull();
        assertThat(new String(result.readAllBytes())).isEqualTo("csv,data");

        ArgumentCaptor<List<FlexibilityReservation>> captor = ArgumentCaptor.forClass(List.class);
        verify(reservationCsvUtil).exportReservationsToCsv(captor.capture());
        List<FlexibilityReservation> passedList = captor.getValue();

        // Assert that only expected reservations were passed
        assertThat(passedList).allMatch(r -> !r.getTimestamp().isBefore(from.atZone(zone))
                && !r.getTimestamp().isAfter(to.atZone(zone)));
    }

    private List<FlexibilityReservation> generateReservations() {
        return List.of(                 FlexibilityReservation.builder()
                        .id(1L)
                        .timestamp(ZonedDateTime.parse("2022-10-24T14:15:22Z"))
                        .assetId(UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8"))
                        .marketId(UUID.fromString("8a5075bf-2552-4119-b135-61ddcfd37ba2"))
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
                        .assetId(UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8"))
                        .marketId(UUID.fromString("8a5075bf-2552-4119-b135-61ddcfd37ba2"))
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
                        .assetId(UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8"))
                        .marketId(UUID.fromString("8a5075bf-2552-4119-b135-61ddcfd37ba2"))
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
                        .assetId(UUID.fromString("3f2a7c9d-6b3e-4d68-99fa-7b8f12e845ca"))
                        .marketId(UUID.fromString("d26f8e43-1e02-4f65-8f89-11a7327bd89f"))
                        .positiveBidId(UUID.randomUUID())
                        .negativeBidId(UUID.randomUUID())
                        .positiveValue(BigDecimal.valueOf(110_000))
                        .positiveCapacityPrice(BigDecimal.valueOf(1.0))
                        .positiveEnergyPrice(BigDecimal.valueOf(1.0))
                        .negativeValue(BigDecimal.valueOf(110_000))
                        .negativeCapacityPrice(BigDecimal.valueOf(1.5))
                        .negativeEnergyPrice(BigDecimal.valueOf(1.5))
                        .updatedAt(ZonedDateTime.parse("2023-01-02T11:42:12.794363Z"))
                        .build());
    }
}
