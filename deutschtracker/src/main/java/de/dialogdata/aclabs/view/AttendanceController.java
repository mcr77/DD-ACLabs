package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.service.AttendanceService;
import de.dialogdata.aclabs.service.GroupService;
import de.dialogdata.aclabs.service.IAttendanceService;

@Named(value = "attendanceBEBean")
@javax.enterprise.context.SessionScoped
public class AttendanceController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private IAttendanceService attendanceService;

	private Long id;
	
	private AttendanceBE attendanceBE;

	private AttendanceBE example = new AttendanceBE();

	private int page;
	private long count;
	private List<AttendanceBE> pageItems;
	
	public AttendanceBE getAttendanceBE() {
		return this.attendanceBE;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return AttendanceService.PAGE_SIZE;
	}

	public AttendanceBE getExample() {
		return example;
	}

	public void setExample(AttendanceBE example) {
		this.example = example;
	}

	public void search() {
		this.page = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
			this.attendanceBE = this.example;
		} else {
			this.attendanceBE = attendanceService.findAttendance(id);
			if (attendanceBE == null) {

				attendanceBE = new AttendanceBE();
				example = new AttendanceBE();
			}

		}
	}

	public AttendanceBE findById(Long id) {
		return attendanceService.findAttendance(getId());
	}

	@SuppressWarnings("incomplete-switch")
	public String update() {
		try {

			System.out.println("\n\n\nUPDATE\n\n\n");
			CrudOperation result = attendanceService
					.createOrUpdate(attendanceBE);
			switch (result) {
			case CREATE:
				return "search?faces-redirect=true";
			case UPDATE:
				return "view?faces-redirect=true&id=" + attendanceBE.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
		}
		return null;
	}

	public String delete() {
		try {
			attendanceService.deleteAttendance(getId());
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public void paginate() {
		pageItems = attendanceService.paginate(page, example);
		count = pageItems.size();
		example = new AttendanceBE();
		id = null;
	}

	public List<AttendanceBE> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return attendanceService.findAttendance(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((AttendanceBE) value).getId());
			}
		};
	}

}
