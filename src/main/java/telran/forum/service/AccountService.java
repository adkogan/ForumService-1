package telran.forum.service;

import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;
import telran.forum.dto.UserRolesDTO;

public interface AccountService {

	public UserProfileDto addUser(UserRegisterDto userRegDto, String auth);

	public UserProfileDto editUser(UserRegisterDto userRegDto, String auth);

	public UserProfileDto removeUser(String id, String auth);

	public UserProfileDto updateRoles(String id, UserRolesDTO userRoles, String auth);

}
