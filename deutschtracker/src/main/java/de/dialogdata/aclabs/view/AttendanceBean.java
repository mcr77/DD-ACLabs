package de.dialogdata.aclabs.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.entities.UserBE;

/**
 * Backing bean for Attendance entities.
 * <p>
 * This class provides CRUD functionality for all Attendance entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AttendanceBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Attendance entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private AttendanceBE attendance;

   public AttendanceBE getAttendance()
   {
      return this.attendance;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.attendance = this.example;
      }
      else
      {
         this.attendance = findById(getId());
      }
   }

   public AttendanceBE findById(Long id)
   {

      return this.entityManager.find(AttendanceBE.class, id);
   }

   /*
    * Support updating and deleting Attendance entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.attendance);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.attendance);
            return "view?faces-redirect=true&id=" + this.attendance.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         AttendanceBE deletableEntity = findById(getId());
         UserBE user = deletableEntity.getUser();
         user.getAttendances().remove(deletableEntity);
         deletableEntity.setUser(null);
         this.entityManager.merge(user);
         GroupCourseBE groupcourse = deletableEntity.getGroupcourse();
         groupcourse.getUserAttendances().remove(deletableEntity);
         deletableEntity.setGroupcourse(null);
         this.entityManager.merge(groupcourse);
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Attendance entities with pagination
    */

   private int page;
   private long count;
   private List<AttendanceBE> pageItems;

   private AttendanceBE example = new AttendanceBE();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public AttendanceBE getExample()
   {
      return this.example;
   }

   public void setExample(AttendanceBE example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<AttendanceBE> root = countCriteria.from(AttendanceBE.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<AttendanceBE> criteria = builder.createQuery(AttendanceBE.class);
      root = criteria.from(AttendanceBE.class);
      TypedQuery<AttendanceBE> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<AttendanceBE> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      UserBE user = this.example.getUser();
      if (user != null)
      {
         predicatesList.add(builder.equal(root.get("user"), user));
      }
      GroupCourseBE groupcourse = this.example.getGroupcourse();
      if (groupcourse != null)
      {
         predicatesList.add(builder.equal(root.get("groupcourse"), groupcourse));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<AttendanceBE> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Attendance entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<AttendanceBE> getAll()
   {

      CriteriaQuery<AttendanceBE> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(AttendanceBE.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(AttendanceBE.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AttendanceBean ejbProxy = this.sessionContext.getBusinessObject(AttendanceBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((AttendanceBE) value).getId());
         }
      };
   }

}