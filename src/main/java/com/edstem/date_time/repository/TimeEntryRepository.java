package com.edstem.date_time.repository;

import com.edstem.date_time.model.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
	List<TimeEntry> findByEmployeeIdAndStartTimeBetween(Long employeeId, ZonedDateTime start, ZonedDateTime end);

	@Query("SELECT t FROM TimeEntry t WHERE t.employeeId = :employeeId AND " +
			"((t.startTime < :endTime) AND (t.endTime > :startTime))")
	List<TimeEntry> findOverlappingEntries(@Param("employeeId") Long employeeId,
										   @Param("startTime") ZonedDateTime startTime,
										   @Param("endTime") ZonedDateTime endTime);
}
