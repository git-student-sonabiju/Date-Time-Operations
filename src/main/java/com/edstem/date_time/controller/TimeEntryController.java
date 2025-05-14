package com.edstem.date_time.controller;

import com.edstem.date_time.dto.TimeEntryRequestDTO;
import com.edstem.date_time.dto.TimeEntryResponseDTO;
import com.edstem.date_time.model.TimeEntry;
import com.edstem.date_time.service.TimeEntryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-entries")
public class TimeEntryController {

	private final TimeEntryService timeEntryService;

	public TimeEntryController(TimeEntryService timeEntryService) {
		this.timeEntryService = timeEntryService;
	}

	@PostMapping
	public ResponseEntity<TimeEntryResponseDTO> logTime(@Valid @RequestBody TimeEntryRequestDTO timeEntryRequestDTO) {
		TimeEntry saved = timeEntryService.createEntry(timeEntryRequestDTO);
		TimeEntryResponseDTO response = toResponseDTO(saved, timeEntryRequestDTO.getStartTime().getZone());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/total-hours")
	public ResponseEntity<Map<String, Long>> getTotalHours(@RequestParam Long employeeId,
														   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String start,
														   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String end) {
		ZonedDateTime startTime = LocalDate.parse(start).atStartOfDay(ZoneOffset.UTC);
		ZonedDateTime endTime = LocalDate.parse(end).plusDays(1).atStartOfDay(ZoneOffset.UTC);
		Duration total = timeEntryService.calculateTotalHours(employeeId, startTime, endTime);
		return ResponseEntity.ok(Map.of("totalHours", total.toHours()));
	}

	@GetMapping("/check-overlap")
	public ResponseEntity<List<TimeEntryResponseDTO>> checkOverlap(@RequestParam Long employeeId,
																   @RequestParam String start,
																   @RequestParam String end,
																   @RequestParam String timezone) {
		ZonedDateTime startTime = ZonedDateTime.parse(start);
		ZonedDateTime endTime = ZonedDateTime.parse(end);
		ZoneId zone = ZoneId.of(timezone);
		List<TimeEntry> entries = timeEntryService.checkOverlap(employeeId, startTime, endTime);
		List<TimeEntryResponseDTO> response = entries.stream()
				.map(e -> toResponseDTO(e, zone))
				.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/report")
	public ResponseEntity<List<TimeEntryResponseDTO>> getReport(@RequestParam Long employeeId,
																@RequestParam String from,
																@RequestParam String to,
																@RequestParam String timeZone) {
		ZoneId zone = ZoneId.of(timeZone);
		ZonedDateTime start = LocalDate.parse(from).atStartOfDay(zone).withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime end = LocalDate.parse(to).plusDays(1).atStartOfDay(zone).withZoneSameInstant(ZoneOffset.UTC);
		List<TimeEntry> entries = timeEntryService.getEntriesForReport(employeeId, start, end, zone);
		List<TimeEntryResponseDTO> response = entries.stream()
				.map(e -> toResponseDTO(e, zone))
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	private TimeEntryResponseDTO toResponseDTO(TimeEntry entry, ZoneId zone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
		TimeEntryResponseDTO dto = new TimeEntryResponseDTO();
		dto.setId(entry.getId());
		dto.setEmployeeId(entry.getEmployeeId());
		dto.setProjectId(entry.getProjectId());
		dto.setDescription(entry.getDescription());
		dto.setStartTime(entry.getStartTime().withZoneSameInstant(zone).format(formatter));
		dto.setEndTime(entry.getEndTime().withZoneSameInstant(zone).format(formatter));
		return dto;
	}
}
