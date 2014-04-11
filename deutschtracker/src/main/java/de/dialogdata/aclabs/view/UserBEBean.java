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

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.utils.SecurityUtils;

/**
 * Backing bean for UserBE entities.
 * <p>
 * This class provides CRUD functionality for all UserBE entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class UserBEBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving UserBE entities
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

   private UserBE userBE;

   public UserBE getUserBE()
   {
      return this.userBE;
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
         this.userBE = this.example;
      }
      else
      {
         this.userBE = findById(getId());
      }
   }

   public UserBE findById(Long id)
   {

      return this.entityManager.find(UserBE.class, id);
   }

   /*
    * Support updating and deleting UserBE entities
    */

   public String update()
   {
	   userBE.setPassword(SecurityUtils.encryptString(userBE.getPassword()));
	   
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.userBE);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.userBE);
            return "view?faces-redirect=true&id=" + this.userBE.getId();
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
         UserBE deletableEntity = findById(getId());
         GroupBE group = deletableEntity.getGroup();
         group.getUsers().remove(deletableEntity);
         deletableEntity.setGroup(null);
         this.entityManager.merge(group);
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
    * Support searching UserBE entities with pagination
    */

   private int page;
   private long count;
   private List<UserBE> pageItems;

   private UserBE example = new UserBE();

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

   public UserBE getExample()
   {
      return this.example;
   }

   public void setExample(UserBE example)
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
      Root<UserBE> root = countCriteria.from(UserBE.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<UserBE> criteria = builder.createQuery(UserBE.class);
      root = criteria.from(UserBE.class);
      TypedQuery<UserBE> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<UserBE> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String firstName = this.example.getFirstName();
      if (firstName != null && !"".equals(firstName))
      {
         predicatesList.add(builder.like(root.<String> get("firstName"), '%' + firstName + '%'));
      }
      String lastName = this.example.getLastName();
      if (lastName != null && !"".equals(lastName))
      {
         predicatesList.add(builder.like(root.<String> get("lastName"), '%' + lastName + '%'));
      }
      String userName = this.example.getUserName();
      if (userName != null && !"".equals(userName))
      {
         predicatesList.add(builder.like(root.<String> get("userName"), '%' + userName + '%'));
      }
      String password = this.example.getPassword();
      if (password != null && !"".equals(password))
      {
         predicatesList.add(builder.like(root.<String> get("password"), '%' + password + '%'));
      }
      GroupBE group = this.example.getGroup();
      if (group != null)
      {
         predicatesList.add(builder.equal(root.get("group"), group));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<UserBE> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back UserBE entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<UserBE> getAll()
   {

      CriteriaQuery<UserBE> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(UserBE.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(UserBE.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final UserBEBean ejbProxy = this.sessionContext.getBusinessObject(UserBEBean.class);

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

            return String.valueOf(((UserBE) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private UserBE add = new UserBE();

   public UserBE getAdd()
   {
      return this.add;
   }

   public UserBE getAdded()
   {
      UserBE added = this.add;
      this.add = new UserBE();
      return added;
   }
}