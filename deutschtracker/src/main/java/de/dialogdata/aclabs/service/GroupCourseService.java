package de.dialogdata.aclabs.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.enums.CrudOperation;
import de.dialogdata.aclabs.enums.WeekDay;

@Stateless
public class GroupCourseService implements IGroupCourseService {

	public final static int PAGE_SIZE = 10;

	@EJB
	private IGroupService groupService;

	@PersistenceContext
	private EntityManager entityManager;

	public GroupCourseBE findGroupCourse(Long id) {

		return this.entityManager.find(GroupCourseBE.class, id);
	}

	public List<GroupCourseBE> paginate(int page,
			GroupCourseBE searchParametersGroupCourseBE) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<GroupCourseBE> root = countCriteria.from(GroupCourseBE.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(searchParametersGroupCourseBE, root));

		// Populate pageItems

		CriteriaQuery<GroupCourseBE> criteria = builder
				.createQuery(GroupCourseBE.class);
		root = criteria.from(GroupCourseBE.class);
		TypedQuery<GroupCourseBE> query = entityManager
				.createQuery(criteria.select(root)
						.where(getSearchPredicates(
								searchParametersGroupCourseBE, root)));
		query.setFirstResult(page * PAGE_SIZE).setMaxResults(PAGE_SIZE);
		List<GroupCourseBE> items = query.getResultList();

		return items;

	}

	private Predicate[] getSearchPredicates(
			GroupCourseBE searchParametersGroupCourseBE,
			Root<GroupCourseBE> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		WeekDay day = searchParametersGroupCourseBE.getDay();
		if (day != null) {
			predicatesList.add(builder.equal(root.get("day"), day));
		}
		GroupBE group = searchParametersGroupCourseBE.getGroup();
		if (group != null) {
			predicatesList.add(builder.equal(root.get("group"), group));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);

	}

	public List<GroupCourseBE> findAll() {

		CriteriaQuery<GroupCourseBE> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(GroupCourseBE.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(GroupCourseBE.class)))
				.getResultList();
	}

	public CrudOperation createOrUpdate(GroupCourseBE groupCourse) {
		CrudOperation operation;

		if (groupCourse.getId() != null) {
			entityManager.merge(groupCourse);
			operation = CrudOperation.UPDATE;
		}

		else {
			entityManager.persist(groupCourse);
			operation = CrudOperation.CREATE;
		}

		return operation;
	}

	public void deleteGroupCourse(Long id) {

		GroupCourseBE deletableEntity = findGroupCourse(id);
		GroupBE group = deletableEntity.getGroup();
		group.getCourses().remove(deletableEntity);
		deletableEntity.setGroup(null);
		this.entityManager.merge(group);
		Iterator<AttendanceBE> iterUserAttendances = deletableEntity
				.getUserAttendances().iterator();
		for (; iterUserAttendances.hasNext();) {
			AttendanceBE nextInUserAttendances = iterUserAttendances.next();
			nextInUserAttendances.setGroupcourse(null);
			iterUserAttendances.remove();
			this.entityManager.merge(nextInUserAttendances);
		}
		this.entityManager.remove(deletableEntity);
		this.entityManager.flush();

	}

}
