package de.dialogdata.aclabs.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;

@Stateless
public class AttendanceService implements IAttendanceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int PAGE_SIZE = 10;

	@EJB
	private IUserService userService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AttendanceBE findAttendance(Long id) {

		return this.entityManager.find(AttendanceBE.class, id);
	}

	@Override
	public List<AttendanceBE> paginate(int page,
			AttendanceBE searchParametersAttendanceBE) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<AttendanceBE> root = countCriteria.from(AttendanceBE.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(searchParametersAttendanceBE, root));

		// Populate pageItems

		CriteriaQuery<AttendanceBE> criteria = builder
				.createQuery(AttendanceBE.class);
		root = criteria.from(AttendanceBE.class);
		TypedQuery<AttendanceBE> query = entityManager
				.createQuery(criteria.select(root)
						.where(getSearchPredicates(
								searchParametersAttendanceBE, root)));
		query.setFirstResult(page * PAGE_SIZE).setMaxResults(PAGE_SIZE);
		List<AttendanceBE> items = query.getResultList();

		return items;
	}

	private Predicate[] getSearchPredicates(

	AttendanceBE searchParametersAttendanceBE, Root<AttendanceBE> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		UserBE user = searchParametersAttendanceBE.getUser();
		if (user != null) {
			predicatesList.add(builder.equal(root.get("user"), user));
		}
		GroupCourseBE groupcourse = searchParametersAttendanceBE
				.getGroupcourse();
		if (groupcourse != null) {
			predicatesList.add(builder.equal(root.get("groupcourse"),
					groupcourse));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public CrudOperation createOrUpdate(AttendanceBE attendance) {
		CrudOperation operation;
		if (attendance.getId() != null) {
			entityManager.merge(attendance);
			operation = CrudOperation.UPDATE;
		} else {
			entityManager.persist(attendance);
			operation = CrudOperation.CREATE;
		}
		return operation;
	}

	@Override
	public void deleteAttendance(Long id) {

		AttendanceBE deletableEntity = findAttendance(id);
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

	}
	
	public List<AttendanceBE> findAll() {

		CriteriaQuery<AttendanceBE> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(AttendanceBE.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(AttendanceBE.class)))
				.getResultList();

	}

	@Override
	public List<AttendanceBE> findUserForGroupCourse(Long groupCourseId) {
		
		TypedQuery<AttendanceBE> query = entityManager.createNamedQuery(AttendanceBE.FIND_BY_GROUPCOURSE,AttendanceBE.class);
		query.setParameter(AttendanceBE.FIND_BY_GROUPCOURSE_ID_PARAM, groupCourseId);
		return query.getResultList();
	}	
}
