package de.dialogdata.aclabs.entities;

import javax.persistence.Entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import java.lang.Override;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.dialogdata.aclabs.entities.UserBE;

import javax.persistence.ManyToOne;

import de.dialogdata.aclabs.entities.GroupCourseBE;

import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = AttendanceBE.FIND_BY_GROUPCOURSE, query = "Select a from AttendanceBE a where a.groupcourse.id = :"
		+ AttendanceBE.FIND_BY_GROUPCOURSE_ID_PARAM)})
public class AttendanceBE implements Serializable
{

	private static final long serialVersionUID = 1L;
  
	public final static String FIND_BY_GROUPCOURSE = "AttendanceBE.FIND_BY_GROUPCOURSE";
	public final static String FIND_BY_GROUPCOURSE_ID_PARAM = "groupcourseid";
	
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

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((AttendanceBE) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public Date getEntryTime()
   {
      entryTime.getTime();
      return this.entryTime;
   }

   public void setEntryTime(final Date entryTime)
   {
      this.entryTime = entryTime;
   }

   public UserBE getUser()
   {
      return this.user;
   }

   public void setUser(final UserBE user)
   {
      this.user = user;
   }

   public GroupCourseBE getGroupcourse()
   {
      return this.groupcourse;
   }

   public void setGroupcourse(final GroupCourseBE groupcourse)
   {
      this.groupcourse = groupcourse;
   }
}