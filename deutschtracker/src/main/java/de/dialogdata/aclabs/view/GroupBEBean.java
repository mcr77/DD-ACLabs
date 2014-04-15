package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.service.GroupService;
import de.dialogdata.aclabs.service.IGroupService;
import de.dialogdata.aclabs.service.IUserService;

/**
 * Backing bean for GroupBE entities.
 * <p>
 * This class provides CRUD functionality for all GroupBE entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@SessionScoped
public class GroupBEBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private IGroupService groupService;
	
	@EJB
	private IUserService userService;

	private Long id;

	private GroupBE groupBE;

	private GroupBE example = new GroupBE();

	private int page;
	private long count;
	private List<GroupBE> pageItems;

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return GroupService.PAGE_SIZE;
	}

	public GroupBE getExample() {
		return this.example;
	}

	public void setExample(GroupBE example) {
		this.example = example;
	}

	public void search() {
		this.page = 0;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GroupBE getGroupBE() {
		return this.groupBE;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			this.groupBE = this.example;
		} else {
			this.groupBE = groupService.findGroup(id);
			if (groupBE == null) {
				groupBE = new GroupBE();
				example = new GroupBE();
			}
		}
	}

	public GroupBE findById(Long id) {
		return groupService.findGroup(getId());
	}

	/*
	 * Support updating and deleting GroupBE entities
	 */

	@SuppressWarnings("incomplete-switch")
	public String update() {
		try {
			CrudOperation result = groupService.createOrUpdate(groupBE);
			switch (result) {
			case CREATE:
				return "search?faces-redirect=true";
			case UPDATE:
				return "view?faces-redirect=true&id=" + groupBE.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
		return null;
	}

	public String delete() {
		try {
			groupService.deleteGroup(getId());
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public void paginate() {
		pageItems = groupService.paginate(page, example);
		count = pageItems.size();
		example = new GroupBE();
	}

	public List<GroupBE> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	public List<GroupBE> getAll() {
		return groupService.findAll();
	}
	
	public List<UserBE> getUsers(){
		return userService.getUsersForGroup(getId());
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {

				return groupService.findGroup(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context, UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((GroupBE) value).getId());
			}
		};
	}

}