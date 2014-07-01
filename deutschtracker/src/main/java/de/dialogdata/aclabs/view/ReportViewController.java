package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.ReportView;
import de.dialogdata.aclabs.service.IGroupCourseService;
import de.dialogdata.aclabs.service.IGroupService;
import de.dialogdata.aclabs.service.IUserService;

@ManagedBean(name = "reportViewBean")
@SessionScoped
public class ReportViewController implements Serializable {

	private static final String DEFAULT_PERIOD = " ";

	private static final long serialVersionUID = 1L;

	@Inject
	private ReportView reportView;

	@EJB
	private IUserService userService;

	@EJB
	private IGroupService groupService;

	@EJB
	private IGroupCourseService groupCourseService;

	private Map<Long, Map<Long, Integer>> userAttendanceByDay = new HashMap<Long, Map<Long, Integer>>();

	private List<String> header = new ArrayList<String>();

	private List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

	private List<Long> columns = new ArrayList<Long>();

	private Set<String> listOfMonthAndDates = new HashSet<String>();

	private String selectedDate = null;

	@PostConstruct
	public void initAllTime() {

		userAttendanceByDay.clear();
		userAttendanceByDay = reportView.findAllAttendance();
		int sizeOfRows = userAttendanceByDay.size();
		sizeOfRows++;
		iterateDataTable(sizeOfRows);
	}

	private void initByPeriod() {

		int sizeOfRows;
		userAttendanceByDay.clear();
		userAttendanceByDay = reportView.findAllAttendanceByYearAndMonth(selectedDate);
		sizeOfRows = userAttendanceByDay.size();
		sizeOfRows++;
		iterateDataTable(sizeOfRows);
	}

	public void yearMonthDateChanged(ValueChangeEvent e) {

		int sizeOfRows = 0;
		header.clear();
		rows.clear();

		if (e.getNewValue() != null) {
			selectedDate = e.getNewValue().toString();
			initByPeriod();
		} else {
			selectedDate = DEFAULT_PERIOD;
			initAllTime();
		}
		
	}

	public void iterateDataTable(int sizeOfRows) {

		Map<Long, Integer> courseCounts = new HashMap<Long, Integer>();
		populateHeaderLine(header, groupService.findAll().size());

		for (int i = 0; i < sizeOfRows - 1; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.clear();
			for (int j = 0; j < header.size(); j++) {
				if (j == 0) {
					m.put("UserName",userService.findUser((Long) userAttendanceByDay.keySet().toArray()[i]).getUserName());
				} else {
					courseCounts.putAll((Map<? extends Long, ? extends Integer>) userAttendanceByDay.values().toArray()[i]);
					m.put(header.get(j), courseCounts.get(columns.get(j - 1)));
					courseCounts.clear();
				}
			}

			rows.add(m);
			listOfMonthAndDates = new TreeSet<String>();
			listOfMonthAndDates.add(DEFAULT_PERIOD);
			listOfMonthAndDates.addAll(reportView.populateDropDownListOfMonthAndDate());
			// SendMailSSL.main(null);
		}
	}

	public void populateColumns(List<String> list, int size) {

		for (int i = 0; i < size - 1; i++) {
			list.add("Column" + i);
		}
	}

	public void populateHeaderLine(List<String> list, int size) {

		size++;
		List<String> populateHeader = new ArrayList<String>();
		List<GroupBE> groupNames = groupService.findAll();
		System.out.println("\n\n\nDimension of ArrayList groupNames"+ groupNames.size());
		list.add("UserName");
		
		for (int i = 0; i < size - 1; i++) {
			populateHeader.add(groupNames.get(i).getName());
			columns.add(groupNames.get(i).getId());
		}
		list.addAll(populateHeader);
	}
	
	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String choiseDate) {
		this.selectedDate = choiseDate;
	}

	public Set<String> getListOfMonthAndDates() {
		return listOfMonthAndDates;
	}

	public void setListOfMonthAndDates(Set<String> listOfMonthAndDates) {
		this.listOfMonthAndDates = listOfMonthAndDates;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public List<Long> getColumns() {
		return columns;
	}

	public void setColumns(List<Long> columns) {
		this.columns = columns;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public Map<Long, Map<Long, Integer>> getUserAttendanceByDay() {
		return userAttendanceByDay;
	}

	public void setUserAttendanceByDay(
			Map<Long, Map<Long, Integer>> userAttendanceByDay) {
		this.userAttendanceByDay = userAttendanceByDay;
	}
}
