package de.dialogdata.aclabs.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.dialogdata.aclabs.entities.AttendanceBE;

@Stateless
public class ReportViewService implements IReportViewService{
	
	@EJB
	private IAttendanceService attendanceService;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<AttendanceBE> findAllAttendances() {
		List<AttendanceBE> attendances = attendanceService.findAll();
	
		return attendances;
	}

	@Override
	public Set<String> findAllMonthsAndDates() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		Set<String> dateMonthsAndDates = new HashSet<String>();
		List <AttendanceBE> attendancesMonthAndYear = attendanceService.findAll();
		
		for(AttendanceBE a : attendancesMonthAndYear){
			dateMonthsAndDates.add(formatter.format(a.getEntryTime()));
		}
		
		return dateMonthsAndDates;
	}

	
	@Override
	public List<AttendanceBE> findAllAttendancesByYearAndMonth(String choisenDate){
		List<AttendanceBE> attendances = attendanceService.findUsersForYearAndMonth(choisenDate);
		
		for(AttendanceBE a : attendances){	
			System.out.println("TIME: " + a.getEntryTime());
		}
		
		return attendances;
	}
}
