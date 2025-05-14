package com.edstem.date_time.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeEntryResponseDTO {
	private Long id;
	private Long employeeId;
	private Long projectId;
	private String startTime;
	private String endTime;
	private String description;
}
