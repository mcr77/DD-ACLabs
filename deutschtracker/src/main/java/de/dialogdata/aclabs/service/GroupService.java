package de.dialogdata.aclabs.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.dialogdata.aclabs.common.PaginationResult;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.enums.Level;

@Stateless
public class GroupService implements IGroupService {

	private static final long serialVersionUID = -4161299389234314491L;

	public final static int PAGE_SIZE = 10;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public GroupBE findGroup(Long id) {
		GroupBE found = entityManager.find(GroupBE.class, id);
		found.getUsers().size();
		return found;
	}

	@Override
	public PaginationResult<GroupBE> paginate(int page, GroupBE searchParameters) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<GroupBE> root = countCriteria.from(GroupBE.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(searchParameters, root));
		long count = entityManager.createQuery(countCriteria).getSingleResult();

		// Populate pageItems

		CriteriaQuery<GroupBE> criteria = builder.createQuery(GroupBE.class);
		root = criteria.from(GroupBE.class);
		TypedQuery<GroupBE> query = entityManager.createQuery(criteria.select(
				root).where(getSearchPredicates(searchParameters, root)));
		query.setFirstResult(page * PAGE_SIZE).setMaxResults(PAGE_SIZE);
		List<GroupBE> items = query.getResultList();
		return new PaginationResult<GroupBE>(count, items);
	}

	@Override
	public List<GroupBE> findAll() {
		CriteriaQuery<GroupBE> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(GroupBE.class);
		List<GroupBE> result = entityManager.createQuery(
				criteria.select(criteria.from(GroupBE.class))).getResultList();
		for(GroupBE group : result){
			group.getUsers().size();
		}
		return result;
	}

	@Override
	public CrudOperation createOrUpdate(GroupBE group) {
		CrudOperation operation;
		if (group.getId() != null) {
			entityManager.merge(group);
			operation = CrudOperation.UPDATE;
		} else {
			entityManager.persist(group);
			operation = CrudOperation.CREATE;
		}
		return operation;
	}

	@Override
	public void deleteGroup(Long id) {
		GroupBE deletableEntity = findGroup(id);
		Iterator<UserBE> iterUsers = deletableEntity.getUsers().iterator();
		for (; iterUsers.hasNext();) {
			UserBE nextInUsers = iterUsers.next();
			nextInUsers.setGroup(null);
			iterUsers.remove();
			entityManager.merge(nextInUsers);
		}
		entityManager.remove(deletableEntity);
		entityManager.flush();

	}

	private Predicate[] getSearchPredicates(GroupBE searchParameters,
			Root<GroupBE> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = searchParameters.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(root.<String> get("name"),
					'%' + name + '%'));
		}
		Level level = searchParameters.getLevel();
		if (level != null) {
			predicatesList.add(builder.equal(root.get("level"), level));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

}
