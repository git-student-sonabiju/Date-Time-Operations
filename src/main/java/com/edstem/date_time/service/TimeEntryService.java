package com.edstem.date_time.service;

import com.edstem.date_time.dto.TimeEntryRequestDTO;
import com.edstem.date_time.exception.NoOverlapException;
import com.edstem.date_time.model.TimeEntry;
import com.edstem.date_time.repository.TimeEntryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeEntryService {
	private final TimeEntryRepository timeEntryRepository;

	public TimeEntryService(TimeEntryRepository timeEntryRepository) {
		this.timeEntryRepository = timeEntryRepository;
	}

	public TimeEntry createEntry(@Valid TimeEntryRequestDTO timeEntryRequestDTO) {
		TimeEntry entry = new TimeEntry();
		entry.setEmployeeId(timeEntryRequestDTO.getEmployeeId());
		entry.setProjectId(timeEntryRequestDTO.getProjectId());
		entry.setDescription(timeEntryRequestDTO.getDescription());
		entry.setStartTime(timeEntryRequestDTO.getStartTime().withZoneSameInstant(ZoneOffset.UTC));
		entry.setEndTime(timeEntryRequestDTO.getEndTime().withZoneSameInstant(ZoneOffset.UTC));
		return timeEntryRepository.save(entry);
	}

	public Duration calculateTotalHours(Long employeeId, ZonedDateTime start, ZonedDateTime end) {
		List<TimeEntry> entries = timeEntryRepository.findByEmployeeIdAndStartTimeBetween(employeeId, start, end);
		return entries.stream()
				.map(e -> Duration.between(e.getStartTime(), e.getEndTime()))
				.reduce(Duration.ZERO, Duration::plus);
	}

	public List<TimeEntry> checkOverlap(Long employeeId, ZonedDateTime startTime, ZonedDateTime endTime) {
		List<TimeEntry> entries = timeEntryRepository.findOverlappingEntries(employeeId, startTime, endTime);

		if (entries.isEmpty()) {
			throw new NoOverlapException("No overlapping entries found.");
		}
		return entries;
	}

	public List<TimeEntry> getEntriesForReport(Long employeeId, ZonedDateTime from, ZonedDateTime to, ZoneId userZone) {
		return timeEntryRepository.findByEmployeeIdAndStartTimeBetween(employeeId, from, to).stream()
				.map(entry -> {
					entry.setStartTime(entry.getStartTime().withZoneSameInstant(userZone));
					entry.setEndTime(entry.getEndTime().withZoneSameInstant(userZone));
					return entry;
				})
				.collect(Collectors.toList());
	}
}
