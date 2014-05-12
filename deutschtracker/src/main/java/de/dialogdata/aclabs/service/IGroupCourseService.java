package de.dialogdata.aclabs.service;

import java.util.List;

import de.dialogdata.aclabs.entities.GroupCourseBE;
import de.dialogdata.aclabs.enums.CrudOperation;

public interface IGroupCourseService {

	public GroupCourseBE findGroupCourse(Long id);
	
	public List<GroupCourseBE> paginate(int page, GroupCourseBE searchParametersGroupCourseBE);
	
	public List<GroupCourseBE> findAll();
	
	public CrudOperation createOrUpdate(GroupCourseBE groupCourse);
	
	public void deleteGroupCourse(Long id);
	
}
