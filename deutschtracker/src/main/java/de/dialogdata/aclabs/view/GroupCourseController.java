package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.service.GroupCourseService;
import de.dialogdata.aclabs.service.IAttendanceService;
import de.dialogdata.aclabs.service.IGroupCourseService;

@Named(value = "groupCourseBEBean")
@SessionScoped
public class GroupCourseController implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private IGroupCourseService groupCourseService;

	@EJB
	private IAttendanceService attendanceService;
	
	private Long id;

	private GroupCourseBE groupCourseBE;

	private GroupCourseBE example = new GroupCourseBE();

	private int page;

	private long count;

	private List<GroupCourseBE> pageItems;

	public GroupCourseBE getGroupCourseBE() {
		return this.groupCourseBE;
	}

	public GroupCourseBE getExample() {
		return example;
	}

	public void setExample(GroupCourseBE example) {
		this.example = example;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void search() {
		this.page = 0;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize(){
		
		return GroupCourseService.PAGE_SIZE;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		System.out.println("\n\n\n\nEntered in RETRIVE");
		// Return the FacesContext instance for the request that is being
		// processed by the current thread, if any
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.groupCourseBE = this.example;
		} else {
			this.groupCourseBE = groupCourseService.findGroupCourse(id);
			if (groupCourseBE == null) {

				groupCourseBE = new GroupCourseBE();
				example = new GroupCourseBE();
			}

		}
	}

	public GroupCourseBE findById(Long id) {

		return groupCourseService.findGroupCourse(id);

	}

	@SuppressWarnings("incomplete-switch")
	public String update() {

		try {

			System.out.println("\n\n\nUPDATE\n\n\n");
			CrudOperation result = groupCourseService
					.createOrUpdate(groupCourseBE);

			switch (result) {
			case CREATE:
				return "search?faces-redirect=true";
			case UPDATE:
				return "view?faces-redirect=true&id=" + groupCourseBE.getId();
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));

		}

		return null;
	}

	public String delete() {
		try {

			System.out.println("\n\n\nUPDATE\n\n\n");
			groupCourseService.deleteGroupCourse(getId());
			return "search?faces-redirect=true";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));

		}
		return null;
	}

	public void paginate() {
		pageItems = groupCourseService.paginate(page, example);
		count = pageItems.size();
		example = new GroupCourseBE();
		id = null;
	}

	public List<GroupCourseBE> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}
	
	
	public List<GroupCourseBE> getAll()
	   {

	     return groupCourseService.findAll();
	   }
	
	public List<AttendanceBE> getAttendanceUsers() {
		return attendanceService.findUserForGroupCourse(getId());
	}
	

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return groupCourseService.findGroupCourse(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((GroupCourseBE) value).getId());
			}
		};
	}
	
	   
	   /*
	    * *****SChedule Code**
	    *
	    */
	   
	   
	   private ScheduleModel model;
	   
	//   {
//	       eventModel = new DefaultScheduleModel();
//	       Calendar cl = Calendar.getInstance();
//	       cl.set(Calendar.HOUR_OF_DAY, 10);
//	       Date st = cl.getTime();
//	       cl.set(Calendar.HOUR_OF_DAY, 12);
//	       Date end = cl.getTime();
//	       eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", st, end));
	//   }
	   
	   public ScheduleModel getModel(){
		   model = new DefaultScheduleModel();

		   List<GroupCourseBE> allCourses = this.getAll();

		   for(GroupCourseBE groupCourseBE : allCourses){

		   model.addEvent(this.makeEvent(groupCourseBE));

		   }
		   return model;

	   }
	   
	   public DefaultScheduleEvent makeEvent(GroupCourseBE groupCourseBE) {

		   Date startTime = groupCourseBE.getStartTime();

		   Calendar cal = Calendar.getInstance();

		   cal.set(Calendar.DAY_OF_WEEK, groupCourseBE.getDay().getDayOfWeek());
		   cal.set(Calendar.HOUR, getHourFromDate(startTime)/100);
		   cal.set(Calendar.MINUTE, getHourFromDate(startTime)%100);
		   Date start = cal.getTime();

		   cal.roll(Calendar.MINUTE, groupCourseBE.getDuration());
		  // cal.roll(Calendar.HOUR, 3);
		   Date end = cal.getTime();

		   return new DefaultScheduleEvent(groupCourseBE.toString(), start , end ) ;

	   }
	   
	   private int getHourFromDate(Date date){

		   int hour = 0 , min = 0 ;

		   Calendar instance = Calendar.getInstance();
		  instance.setTime(date);

		  hour = instance.get(Calendar.HOUR);
		  min = instance.get(Calendar.MINUTE);

		  return hour*100+min;

	   }
}
