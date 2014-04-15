package de.dialogdata.aclabs.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.enums.CrudOperation;

@Local
public interface IUserService extends Serializable {

	public UserBE findUser(Long id);

	public List<UserBE> paginate(int page, UserBE searchParameters);

	public List<UserBE> findAll();

	public CrudOperation createOrUpdate(UserBE user);

	public void deleteUser(Long id);

	public List<UserBE> getUsersForGroup(Long groupId);

}
