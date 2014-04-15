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

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.service.IUserService;
import de.dialogdata.aclabs.service.UserService;
import de.dialogdata.aclabs.utils.SecurityUtils;

/**
 * Backing bean for UserBE entities.
 * <p>
 * This class provides CRUD functionality for all UserBE entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@SessionScoped
public class UserBEBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private IUserService userSerivce;

	private Long id;

	private UserBE userBE;

	private UserBE example = new UserBE();

	private int page;
	private long count;
	private List<UserBE> pageItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserBE getUserBE() {
		return userBE;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return UserService.PAGE_SIZE;
	}

	public UserBE getExample() {
		return this.example;
	}

	public void setExample(UserBE example) {
		this.example = example;
	}

	public void search() {
		this.page = 0;
	}

	public List<UserBE> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.userBE = this.example;
		} else {
			this.userBE = userSerivce.findUser(id);
			if (userBE == null) {
				userBE = new UserBE();
				example = new UserBE();
			}
		}
	}

	public UserBE findById(Long id) {
		return userSerivce.findUser(getId());
	}

	/*
	 * Support updating and deleting UserBE entities
	 */

	@SuppressWarnings("incomplete-switch")
	public String update() {
		userBE.setPassword(SecurityUtils.encryptString(userBE.getPassword()));
		try {
			CrudOperation result = userSerivce.createOrUpdate(userBE);
			switch (result) {
			case CREATE:
				return "search?faces-redirect=true";
			case UPDATE:
				return "view?faces-redirect=true&id=" + this.userBE.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
		return null;
	}

	public String delete() {
		try {
			userSerivce.deleteUser(getId());
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public void paginate() {
		pageItems = userSerivce.paginate(getPage(), example);
		count = pageItems.size();
		example = new UserBE();
	}

	public List<UserBE> getAll() {
		return userSerivce.findAll();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {

				return userSerivce.findUser(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context, UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((UserBE) value).getId());
			}
		};
	}

}