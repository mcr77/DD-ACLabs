package de.dialogdata.aclabs.entities;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import de.dialogdata.aclabs.entities.UserBE;
import javax.persistence.ManyToOne;
import de.dialogdata.aclabs.entities.GroupCourseBE;

@Entity
public class Attendance implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Temporal(TemporalType.TIME)
   private Date entryTime;

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
         return id.equals(((Attendance) that).id);
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