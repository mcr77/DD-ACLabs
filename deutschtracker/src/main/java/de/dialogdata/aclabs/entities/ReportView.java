package de.dialogdata.aclabs.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.dialogdata.aclabs.service.IReportViewService;

@Named
@SessionScoped
public class ReportView implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB
	private IReportViewService reportViewService ;
	
	
	public Map<Long, Map<Long, Integer>> populateAttendanceByDay(List<AttendanceBE> attendances) {
		
		Long testIdUser 	= null;
		Long testIdGroupId 	= null;
		Map<Long, Map<Long, Integer>> attendancesByDay = new HashMap<Long, Map<Long,Integer>>();
		
		
		for(AttendanceBE a : attendances){
			
			if(a.getUser()!= null && a.getGroupcourse() != null && a.getGroupcourse().getGroup() != null){
				
				testIdUser 		= a.getUser().getId();
				testIdGroupId 	= a.getGroupcourse().getGroup().getId();
				
				// test if userId exist in HashMap
				if(attendancesByDay.containsKey(testIdUser)){
					
					Map<Long, Integer> testGroupIdAndCount = new HashMap<Long, Integer>();
					testGroupIdAndCount.putAll(attendancesByDay.get(testIdUser));

					// test if groupId exists in HashMap
					if(testGroupIdAndCount.containsKey(testIdGroupId)){	
						
						Map<Long, Integer> groupAndCountNew = new HashMap<Long, Integer>();
						
						System.out.println("GROUP KEY" + testGroupIdAndCount.keySet());
						System.out.println("GET SMALL MAP" + attendancesByDay.get(testIdUser));
						
						
						groupAndCountNew.putAll(attendancesByDay.get(testIdUser));
						groupAndCountNew.put(testIdGroupId, groupAndCountNew.get(testIdGroupId) + 1);
						
						attendancesByDay.put(testIdUser, groupAndCountNew);
					}
					// ELSE group doesn't exists 
					else {
						Map<Long, Integer> groupIdAndCount = new HashMap<Long, Integer>();
						Integer c1 = 0;
						groupIdAndCount.putAll(attendancesByDay.get(testIdUser));
						groupIdAndCount.put(testIdGroupId, ++c1);
						attendancesByDay.put(testIdUser, groupIdAndCount);
					
					}
				}
				// ELSE user doesn't exist in HashMap, when we add it
				else {
					Map<Long, Integer> groupIdAndCount = new HashMap<Long, Integer>();
					Integer c1 = 0;
					groupIdAndCount.put(testIdGroupId, c1+1);
					attendancesByDay.put(testIdUser, groupIdAndCount);
					
				}
			}
		}
		
		return attendancesByDay;
	}

	
	public Map<Long, Map<Long, Integer>> findAllAttendance () {
		List<AttendanceBE> attendances = reportViewService.findAllAttendances();
		Map<Long, Map<Long, Integer>> attendancesByDay = populateAttendanceByDay(attendances);
		
		return attendancesByDay;
	}

	
	public Map<Long, Map<Long, Integer>> findAllAttendanceByYearAndMonth(String choisenDate) {
		List<AttendanceBE> attendances = reportViewService.findAllAttendancesByYearAndMonth(choisenDate);
		Map<Long, Map<Long, Integer>> attendancesByDay = populateAttendanceByDay(attendances);	
		
		return attendancesByDay;
	}
	
	public IReportViewService getReportViewService() {
		return reportViewService;
	}

	public void setReportViewService(IReportViewService reportViewService) {
		this.reportViewService = reportViewService;
	}
	
	public Set<String> populateDropDownListOfMonthAndDate() {
		Set<String> finalListOfMonthAndDates = new HashSet<String>();
		finalListOfMonthAndDates = reportViewService.findAllMonthsAndDates();
		
		return finalListOfMonthAndDates;
	}
	
	public List<AttendanceBE> findAllAttendanceByMonthAndDay(String choisenDate){
		List<AttendanceBE> attendances = reportViewService.findAllAttendancesByYearAndMonth(choisenDate);

		return attendances;
	}
	
	
	
}
