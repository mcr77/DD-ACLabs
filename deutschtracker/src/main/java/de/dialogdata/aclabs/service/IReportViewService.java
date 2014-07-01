package de.dialogdata.aclabs.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import de.dialogdata.aclabs.entities.AttendanceBE;

public interface IReportViewService {

	public List<AttendanceBE> findAllAttendances();

	public Set<String> findAllMonthsAndDates();

	public List<AttendanceBE> findAllAttendancesByYearAndMonth(String choisenDate);
	
}
