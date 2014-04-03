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

import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.Level;
import java.util.Iterator;

/**
 * Backing bean for GroupBE entities.
 * <p>
 * This class provides CRUD functionality for all GroupBE entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class GroupBEBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving GroupBE entities
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

   private GroupBE groupBE;

   public GroupBE getGroupBE()
   {
      return this.groupBE;
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
         this.groupBE = this.example;
      }
      else
      {
         this.groupBE = findById(getId());
      }
   }

   public GroupBE findById(Long id)
   {

      return this.entityManager.find(GroupBE.class, id);
   }

   /*
    * Support updating and deleting GroupBE entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.groupBE);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.groupBE);
            return "view?faces-redirect=true&id=" + this.groupBE.getId();
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
         GroupBE deletableEntity = findById(getId());
         Iterator<UserBE> iterUsers = deletableEntity.getUsers().iterator();
         for (; iterUsers.hasNext();)
         {
            UserBE nextInUsers = iterUsers.next();
            nextInUsers.setGroup(null);
            iterUsers.remove();
            this.entityManager.merge(nextInUsers);
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
    * Support searching GroupBE entities with pagination
    */

   private int page;
   private long count;
   private List<GroupBE> pageItems;

   private GroupBE example = new GroupBE();

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

   public GroupBE getExample()
   {
      return this.example;
   }

   public void setExample(GroupBE example)
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
      Root<GroupBE> root = countCriteria.from(GroupBE.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<GroupBE> criteria = builder.createQuery(GroupBE.class);
      root = criteria.from(GroupBE.class);
      TypedQuery<GroupBE> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<GroupBE> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(root.<String> get("name"), '%' + name + '%'));
      }
      Level level = this.example.getLevel();
      if (level != null)
      {
         predicatesList.add(builder.equal(root.get("level"), level));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<GroupBE> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back GroupBE entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<GroupBE> getAll()
   {

      CriteriaQuery<GroupBE> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(GroupBE.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(GroupBE.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final GroupBEBean ejbProxy = this.sessionContext.getBusinessObject(GroupBEBean.class);

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

            return String.valueOf(((GroupBE) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private GroupBE add = new GroupBE();

   public GroupBE getAdd()
   {
      return this.add;
   }

   public GroupBE getAdded()
   {
      GroupBE added = this.add;
      this.add = new GroupBE();
      return added;
   }
}