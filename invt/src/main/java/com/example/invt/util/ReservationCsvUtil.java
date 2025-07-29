package com.example.invt.util;

import com.example.invt.model.FlexibilityReservation;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.invt.constant.CsvHeaders.RESERVATION_EXPORT_HEADERS;

@Component
public class ReservationCsvUtil {

    public static ByteArrayInputStream exportReservationsToCsv(List<FlexibilityReservation> reservations) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            CSVWriter csvWriter = new CSVWriter(writer)) {

            csvWriter.writeNext(RESERVATION_EXPORT_HEADERS);

            reservations.stream().forEach( r -> {
                String[] data = {
                        String.valueOf(r.getTimestamp()),
                        String.valueOf(r.getAssetId()),
                        String.valueOf(r.getMarketId()),
                        String.valueOf(r.getPositiveBidId()),
                        String.valueOf(r.getNegativeBidId()),
                        String.valueOf(r.getPositiveValue()),
                        String.valueOf(r.getPositiveCapacityPrice()),
                        String.valueOf(r.getPositiveEnergyPrice()),
                        String.valueOf(r.getNegativeValue()),
                        String.valueOf(r.getNegativeCapacityPrice()),
                        String.valueOf(r.getNegativeEnergyPrice()),
                        String.valueOf(r.getUpdatedAt())
                };
                csvWriter.writeNext(data);
            });

            csvWriter.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException ioException) {
            throw new RuntimeException("Failed to export CSV", ioException);
        }
    }
}
