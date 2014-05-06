package de.dialogdata.aclabs.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;

@Stateless
public class UserService implements IUserService {

	private static final long serialVersionUID = 4733232389411427332L;

	public final static int PAGE_SIZE = 10;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserBE findUser(Long id) {
		UserBE found = entityManager.find(UserBE.class, id);
		return found;
	}

	@Override
	public CrudOperation createOrUpdate(UserBE user) {
		CrudOperation operation;
		if (user.getId() != null) {
			entityManager.merge(user);
			operation = CrudOperation.UPDATE;
		} else {
			entityManager.persist(user);
			operation = CrudOperation.CREATE;
		}
		return operation;
	}

	@Override
	public void deleteUser(Long id) {
		UserBE deletableEntity = findUser(id);
		entityManager.remove(deletableEntity);
		entityManager.flush();
	}

	@Override
	public List<UserBE> paginate(int page, UserBE searchParameters) {
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<UserBE> root = countCriteria.from(UserBE.class);
		countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(searchParameters, root));

		CriteriaQuery<UserBE> criteria = builder.createQuery(UserBE.class);
		root = criteria.from(UserBE.class);
		TypedQuery<UserBE> query = this.entityManager.createQuery(criteria.select(root).where(
				getSearchPredicates(searchParameters, root)));
		query.setFirstResult(page * PAGE_SIZE).setMaxResults(PAGE_SIZE);
		List<UserBE> items = query.getResultList();

		return items;
	}

	private Predicate[] getSearchPredicates(UserBE searchParameters, Root<UserBE> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String firstName = searchParameters.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicatesList.add(builder.like(root.<String> get("firstName"), '%' + firstName + '%'));
		}
		String lastName = searchParameters.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicatesList.add(builder.like(root.<String> get("lastName"), '%' + lastName + '%'));
		}
		String userName = searchParameters.getUserName();
		if (userName != null && !"".equals(userName)) {
			predicatesList.add(builder.like(root.<String> get("userName"), '%' + userName + '%'));
		}
		GroupBE group = searchParameters.getGroup();
		if (group != null) {
			predicatesList.add(builder.equal(root.get("group"), group));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	@Override
	public List<UserBE> findAll() {
		CriteriaQuery<UserBE> criteria = this.entityManager.getCriteriaBuilder().createQuery(UserBE.class);
		return this.entityManager.createQuery(criteria.select(criteria.from(UserBE.class))).getResultList();
	}

	@Override
	public List<UserBE> getUsersForGroup(Long groupId) {
		TypedQuery<UserBE> query =  entityManager.createNamedQuery(UserBE.FIND_BY_GROUP,UserBE.class);
		query.setParameter(UserBE.FIND_BY_GROUP_GROUP_ID_PARAM, groupId);
		return query.getResultList();
	}
	
	public boolean validateCredentials(String userName , String password){
		
		List<UserBE> allUsers = findAll();
		
		for(UserBE userBE : allUsers ){
			
			if(userBE.getUserName().equals(userName))
				if(userBE.getPassword().equals(password))
					return true;
		}
		
		return false;
	}

	@Override
	public UserBE getByUsername(String userName) {
		List<UserBE> allUsers = findAll();
		
		for(UserBE userBE : allUsers ){
			
			if(userBE.getUserName().equals(userName))
				return userBE;
			
		}
		
		return null;
		
	}

}
