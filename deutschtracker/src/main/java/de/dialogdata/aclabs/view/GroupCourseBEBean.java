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

import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupBE;
import java.util.Iterator;

/**
 * Backing bean for GroupCourseBE entities.
 * <p>
 * This class provides CRUD functionality for all GroupCourseBE entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class GroupCourseBEBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving GroupCourseBE entities
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

   private GroupCourseBE groupCourseBE;

   public GroupCourseBE getGroupCourseBE()
   {
      return this.groupCourseBE;
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
         this.groupCourseBE = this.example;
      }
      else
      {
         this.groupCourseBE = findById(getId());
      }
   }

   public GroupCourseBE findById(Long id)
   {

      return this.entityManager.find(GroupCourseBE.class, id);
   }

   /*
    * Support updating and deleting GroupCourseBE entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.groupCourseBE);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.groupCourseBE);
            return "view?faces-redirect=true&id=" + this.groupCourseBE.getId();
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
         GroupCourseBE deletableEntity = findById(getId());
         GroupBE group = deletableEntity.getGroup();
         group.getCourses().remove(deletableEntity);
         deletableEntity.setGroup(null);
         this.entityManager.merge(group);
         Iterator<AttendanceBE> iterUserAttendances = deletableEntity.getUserAttendances().iterator();
         for (; iterUserAttendances.hasNext();)
         {
            AttendanceBE nextInUserAttendances = iterUserAttendances.next();
            nextInUserAttendances.setGroupcourse(null);
            iterUserAttendances.remove();
            this.entityManager.merge(nextInUserAttendances);
         }
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
    * Support searching GroupCourseBE entities with pagination
    */

   private int page;
   private long count;
   private List<GroupCourseBE> pageItems;

   private GroupCourseBE example = new GroupCourseBE();

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

   public GroupCourseBE getExample()
   {
      return this.example;
   }

   public void setExample(GroupCourseBE example)
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
      Root<GroupCourseBE> root = countCriteria.from(GroupCourseBE.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<GroupCourseBE> criteria = builder.createQuery(GroupCourseBE.class);
      root = criteria.from(GroupCourseBE.class);
      TypedQuery<GroupCourseBE> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<GroupCourseBE> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int day = this.example.getDay();
      if (day != 0)
      {
         predicatesList.add(builder.equal(root.get("day"), day));
      }
      GroupBE group = this.example.getGroup();
      if (group != null)
      {
         predicatesList.add(builder.equal(root.get("group"), group));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<GroupCourseBE> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back GroupCourseBE entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<GroupCourseBE> getAll()
   {

      CriteriaQuery<GroupCourseBE> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(GroupCourseBE.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(GroupCourseBE.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final GroupCourseBEBean ejbProxy = this.sessionContext.getBusinessObject(GroupCourseBEBean.class);

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

            return String.valueOf(((GroupCourseBE) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private GroupCourseBE add = new GroupCourseBE();

   public GroupCourseBE getAdd()
   {
      return this.add;
   }

   public GroupCourseBE getAdded()
   {
      GroupCourseBE added = this.add;
      this.add = new GroupCourseBE();
      return added;
   }
}