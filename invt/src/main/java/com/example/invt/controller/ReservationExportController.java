package com.example.invt.controller;

import com.example.invt.service.ReservationExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.invt.constant.ReservationExportRootPath.*;

@RestController
@RequestMapping( path = API_FLEXIBILITY + RESERVATION_EXPORT)
public class ReservationExportController {

    @Autowired
    private ReservationExportService reservationExportService;

    @GetMapping(ASSETID + MARKET + MARKETID)
    public ResponseEntity<InputStreamResource> getReservationCsv(@PathVariable String assetId, @PathVariable String marketId) {
        var assetUuid = parseUuid(assetId, "assetId");
        var marketUuid = parseUuid(marketId, "marketId");
        ByteArrayInputStream csvStream = Optional.ofNullable(reservationExportService.exportReservationCsv(assetUuid, marketUuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No data available for export"));

        return createCsvResponse(csvStream, "reservation.csv");
    }

    @GetMapping(ASSETID + MARKET + MARKETID + EXPORT)
    public ResponseEntity<InputStreamResource> getReservationsCsv(@PathVariable String assetId, @PathVariable String marketId,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                                                     @RequestParam(defaultValue = "false") boolean total) {
        if (from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'from' must be before 'to'");
        }
        var assetUuid = parseUuid(assetId, "assetId");
        var marketUuid = parseUuid(marketId, "marketId");
        ByteArrayInputStream csvStream = Optional.ofNullable(reservationExportService.exportReservationsCsv(assetUuid, marketUuid, from, to, total))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No data available for export"));

        return createCsvResponse(csvStream, "reservations.csv");
    }

    private UUID parseUuid(String input, String fieldName) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format for " + fieldName);
        }
    }

    private ResponseEntity<InputStreamResource> createCsvResponse(ByteArrayInputStream csvStream, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvStream));
    }
}
