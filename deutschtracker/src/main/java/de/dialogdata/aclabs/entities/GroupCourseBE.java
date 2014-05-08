package de.dialogdata.aclabs.entities;

import javax.persistence.Entity;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

import java.lang.Override;

import de.dialogdata.aclabs.entities.GroupBE;

import javax.persistence.ManyToOne;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Set;
import java.util.HashSet;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.enums.WeekDay;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class GroupCourseBE implements Serializable
{

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private WeekDay day;

   @ManyToOne(fetch=FetchType.EAGER)
   private GroupBE group;

   @Temporal(TemporalType.TIME)
   private Date startTime;
   
   @Column
   private int duration;// in minutes
   
   

   @XmlTransient
   @OneToMany(mappedBy = "groupcourse", cascade = CascadeType.ALL)
   private Set<AttendanceBE> userAttendances = new HashSet<AttendanceBE>();

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
         return id.equals(((GroupCourseBE) that).id);
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
   public WeekDay getDay()
   {
      return this.day;
   }

   public void setDay(final WeekDay day)
   {
      this.day = day;
   }

   public GroupBE getGroup()
   {
      return this.group;
   }

   public void setGroup(final GroupBE group)
   {
      this.group = group;
   }

   public Date getStartTime()
   {
      return this.startTime;
   }

   public void setStartTime(final Date startTime)
   {
      this.startTime = startTime;
   }

   public int getDuration() {
	return duration;
}

public void setDuration(int duration) {
	this.duration = duration;
}

@Override
   public String toString()
   {
      String result = "Group "; //getClass().getSimpleName() + " ";
      result += "day: " + day ;
      return result;
   }

   public Set<AttendanceBE> getUserAttendances()
   {
      return this.userAttendances;
   }

   public void setUserAttendances(final Set<AttendanceBE> userAttendances)
   {
      this.userAttendances = userAttendances;
   }
}