package com.edstem.date_time.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeEntryRequestDTO {
	@NotNull(message = "Employee Id is required")
	private Long employeeId;

	@NotNull(message = "Project Id is required")
	private Long projectId;

	@NotNull(message = "Start time is required")
	private ZonedDateTime startTime;

	@NotNull(message = "End time is required")
	private ZonedDateTime endTime;

	@Size(max = 300)
	private String description;
}
