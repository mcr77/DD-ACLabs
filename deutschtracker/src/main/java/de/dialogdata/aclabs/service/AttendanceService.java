package de.dialogdata.aclabs.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
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

	@Override
	public Long countUserAttendances (Long userId, Date START_MONTH, Date END_MONTH) {
	
		TypedQuery<Long> query = entityManager.createNamedQuery(AttendanceBE.COUNT_BY_USER_ID, Long.class);
		query.setParameter(AttendanceBE.COUNT_BY_USER_ID_PARAM, userId);	
		query.setParameter("START_MONTH", START_MONTH , TemporalType.DATE);
		query.setParameter("END_MONTH", END_MONTH,  TemporalType.DATE);
		
		return query.getSingleResult();
	}
//
//	public List<Date> findDates(){
//		
//		TypedQuery<Date> query = entityManager.createNamedQuery(AttendanceBE.FIND_BY_MONTH_AND_YEAR, Date.class);
//		query.setParameter(AttendanceBE.FIND_BY_MONTH, userId);
//		return query.getResultList();
//	}
//	

	@Override
	public List<AttendanceBE> findUsersForYearAndMonth(String choisenDate) {
		Integer numberMonth;
		Integer numberYear;
		String monthString 	= choisenDate.substring(choisenDate.length()-2, choisenDate.length());
		String yearString	= choisenDate.substring(0, 4);
		
		
		System.out.println("choisenMonth " + monthString);
		System.out.println("choisenYear " + yearString);
		
			
		numberMonth = new Integer(Integer.parseInt(monthString));
		numberYear 	= new Integer(yearString); 	
		
		
		
		TypedQuery<AttendanceBE> query = entityManager.createNamedQuery(AttendanceBE.FIND_BY_MONTH_AND_YEAR, AttendanceBE.class);
		query.setParameter("choisenMonth", numberMonth);
		query.setParameter("choisenYear", numberYear);
		
		return query.getResultList();
	}
	
}
