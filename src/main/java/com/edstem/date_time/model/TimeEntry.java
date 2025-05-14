package com.edstem.date_time.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TimeEntry")
@Entity
public class TimeEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long employeeId;
	private Long projectId;
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private String description;
}
