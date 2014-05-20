package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.service.IAttendanceService;
import de.dialogdata.aclabs.service.IGroupService;
import de.dialogdata.aclabs.service.IUserService;

@Named(value = "reportViewBean")
@SessionScoped
public class ReportViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("user",
			"userName", "group", "groupId", "count", "month", "group");

	private String columnTemplate = "userName group";

	private List<ColumnModel> columns;

	// private List<UserBE> users;

	@EJB
	private IGroupService groupService;
	@EJB
	private IUserService userService;
	@EJB
	private IAttendanceService attendanceService;

	private List<UserBE> users;

	private List<GroupBE> groups;

	private List<AttendanceBE> attendances;

	private List<UserBE> filteredUsers;

	@PostConstruct
	public void init() {

		this.users = findAllUser();
		this.groups = findAllGroups();
		this.attendances = findAllAttendance();

		System.out.println("\n\n\nTEST POST CONSTRUCT");
		System.out.println("Dimension of the LIST<UserBE>: " + users.size());
		System.out.println("Dimension of the LIST<GroupBE>: " + groups.size());
		System.out.println("Dimension of the LIST<AttendanceBE>: "
				+ attendances.size());
		createDynamicColumns();
		composeMapUserIdGroupId();
	}

	public List<UserBE> getUsers() {
		return users;
	}

	public List<UserBE> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<UserBE> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public void setColumnTemplate(String columnTemplate) {
		this.columnTemplate = columnTemplate;
	}

	public String getColumnTemplate() {
		return columnTemplate;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public List<GroupBE> findAllGroups() {
		return groupService.findAll();
	}

	public List<UserBE> findAllUser() {
		return userService.findAll();
	}

	public List<AttendanceBE> findAllAttendance() {
		return attendanceService.findAll();
	}
	
	public void composeMapUserIdGroupId(){
		
		Map<Long, Long> userIdGroupIdMap = new HashMap<Long, Long>();
		long val = 1L;
		// Iterator
		/*System.out.println("Iterator List<UserBe>");
		Iterator<UserBE> iterator = users.iterator();
		
			while (iterator.hasNext()) {
				System.out.println("Id User:" + iterator.next().getId());
		}*/
		
		for(UserBE i : users){
		
			userIdGroupIdMap.put(i.getId(), val);
		}
		
		// Output the value
		for (Map.Entry<Long, Long> entry : userIdGroupIdMap.entrySet()) {
			
			System.out.println("Key : " + entry.getKey() + " Value : "
				+ entry.getValue());
		}
	}

	public void createDynamicColumns() {
		String[] columnKeys = columnTemplate.split(" ");
		columns = new ArrayList<ColumnModel>();

		for (String columnKey : columnKeys) {
			String key = columnKey.trim();
			System.out.println("\n\n\n############key################" + key);
			if (VALID_COLUMN_KEYS.contains(key)) {
				columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
			}
		}
	}

	public void updateColumns() {
		// reset table state
		UIComponent table = FacesContext.getCurrentInstance().getViewRoot()
				.findComponent(":form:users");
		table.setValueExpression("sortBy", null);

		// update columns
		createDynamicColumns();
	}

	static public class ColumnModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private String header;
		private String property;

		public ColumnModel(String header, String property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			System.out.println("################# header #################"
					+ header);
			System.out.println("\n\n\n");
			return header;
		}

		public String getProperty() {
			System.out.println("################# property #################"
					+ property);
			return property;
		}
	}
}
