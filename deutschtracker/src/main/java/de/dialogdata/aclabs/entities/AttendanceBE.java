package de.dialogdata.aclabs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = AttendanceBE.FIND_BY_GROUPCOURSE, query = "Select a from AttendanceBE a where a.groupcourse.id = :"
				+ AttendanceBE.FIND_BY_GROUPCOURSE_ID_PARAM),

		@NamedQuery(name = AttendanceBE.COUNT_BY_USER_ID, query = "Select count(a) from AttendanceBE a where a.entryTime BETWEEN :START_MONTH AND :END_MONTH AND a.user.id = :"
				+ AttendanceBE.COUNT_BY_USER_ID_PARAM),
	
		@NamedQuery(name = AttendanceBE.FIND_BY_MONTH_AND_YEAR, query = "Select a from AttendanceBE a where month(a.entryTime) = :choisenMonth and year(a.entryTime) =:choisenYear")
				
//		
			})

		

public class AttendanceBE implements Serializable {
    
	private static final long serialVersionUID = 1L;

	public final static String FIND_BY_GROUPCOURSE = "AttendanceBE.FIND_BY_GROUPCOURSE";
	public final static String FIND_BY_GROUPCOURSE_ID_PARAM = "groupcourseid";

	public final static String COUNT_BY_USER_ID = "AttendanceBE.COUNT_BY_USER_ID";
	public final static String COUNT_BY_USER_ID_PARAM = "userid"; 
	
	
	public final static String FIND_BY_MONTH_AND_YEAR = "AttendanceBE.FIND_BY_MONTH_AND_YEAR";
	public final static String FIND_BY_MONTH = "month_year";
	
	public final static Date START_MONTH = new Date();
	public final static Date END_MONTH = new Date();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id = null;
	@Version
	@Column(name = "version")
	private int version = 0;

	@Temporal(TemporalType.TIMESTAMP)
	private Date entryTime = new Date();

	@ManyToOne
	private UserBE user;

	@ManyToOne
	private GroupCourseBE groupcourse;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		if (id != null) {
			return id.equals(((AttendanceBE) that).id);
		}
		return super.equals(that);
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

	public Date getEntryTime() {
		entryTime.getTime();
		return this.entryTime;
	}

	public void setEntryTime(final Date entryTime) {
		this.entryTime = entryTime;
	}

	public UserBE getUser() {
		return this.user;
	}

	public void setUser(final UserBE user) {
		this.user = user;
	}

	public GroupCourseBE getGroupcourse() {
		return this.groupcourse;
	}

	public void setGroupcourse(final GroupCourseBE groupcourse) {
		this.groupcourse = groupcourse;
	}
}