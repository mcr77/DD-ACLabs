package de.dialogdata.aclabs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlTransient;

import java.util.Set;
import java.util.HashSet;

import de.dialogdata.aclabs.entities.AttendanceBE;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = UserBE.FIND_BY_GROUP, query = "Select e from UserBE e where e.group.id = :" + UserBE.FIND_BY_GROUP_GROUP_ID_PARAM) })
public class UserBE implements Serializable
{

   private static final long serialVersionUID = 2518182912578595495L;

   public final static String FIND_BY_GROUP = "UserBE.FIND_BY_GROUP";
   public final static String FIND_BY_GROUP_GROUP_ID_PARAM = "groupid";

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String firstName;

   @Column
   private String lastName;

   @Column
   private String userName;

   @Column
   private String password;

   @Temporal(TemporalType.DATE)
   private Date lastLogin;

   @ManyToOne(fetch = FetchType.EAGER)
   private GroupBE group;

   @XmlTransient
   @OneToMany( mappedBy = "user", cascade = CascadeType.ALL)
   private Set<AttendanceBE> attendances = new HashSet<AttendanceBE>();

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
         return id.equals(((UserBE) that).id);
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

   public String getFirstName()
   {
      return this.firstName;
   }

   public void setFirstName(final String firstName)
   {
      this.firstName = firstName;
   }

   public String getLastName()
   {
      return this.lastName;
   }

   public void setLastName(final String lastName)
   {
      this.lastName = lastName;
   }

   public String getUserName()
   {
      return this.userName;
   }

   public void setUserName(final String userName)
   {
      this.userName = userName;
   }

   public String getPassword()
   {
      return this.password;
   }

   public void setPassword(final String password)
   {
      this.password = password;
   }

   public Date getLastLogin()
   {
      return this.lastLogin;
   }

   public void setLastLogin(final Date lastLogin)
   {
      this.lastLogin = lastLogin;
   }

   @Override
   public String toString()
   {
      String result = "";//getClass().getSimpleName() + " ";
      if (firstName != null && !firstName.trim().isEmpty())
         result += "FirstName: " + firstName;
      if (lastName != null && !lastName.trim().isEmpty())
         result += ", LastName: " + lastName;
      if (userName != null && !userName.trim().isEmpty())
         result += ", UserName: " + userName;
      if (password != null && !password.trim().isEmpty())
         result += ", password: " + password;
      return result;
   }

   public GroupBE getGroup()
   {
      return this.group;
   }

   public void setGroup(final GroupBE group)
   {
      this.group = group;
   }

   public Set<AttendanceBE> getAttendances()
   {
      return this.attendances;
   }

   public void setAttendances(final Set<AttendanceBE> attendances)
   {
      this.attendances = attendances;
   }
}