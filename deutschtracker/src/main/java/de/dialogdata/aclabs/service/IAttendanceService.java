package de.dialogdata.aclabs.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import de.dialogdata.aclabs.entities.AttendanceBE;
import de.dialogdata.aclabs.enums.CrudOperation;

@Local
public interface IAttendanceService extends Serializable {

	public AttendanceBE findAttendance(Long id);

	public List<AttendanceBE> paginate(int page, AttendanceBE searchParameters);

	public List<AttendanceBE> findAll();

	public CrudOperation createOrUpdate(AttendanceBE attendance);

	public void deleteAttendance(Long id);
}
