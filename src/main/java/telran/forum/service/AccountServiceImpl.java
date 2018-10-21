package telran.forum.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegisterDto userRegDto, String auth) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserExistException();
		}
		UserAccount userAccount = UserAccount.builder()
				.id(credentials.getLogin())
				.password(credentials.getPassword())
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("User")
				.expDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return new UserProfileDto(credentials.getLogin(),
				userRegDto.getFirstName(), userRegDto.getLastName());
	}

	@Override
	public UserProfileDto editUser(UserRegisterDto userRegDto, String auth) {
		// TODO edit forum user
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		userAccount.setFirstName(userRegDto.getFirstName());
		userAccount.setLastName(userRegDto.getLastName());
		return new UserProfileDto(credentials.getLogin(),
				userRegDto.getFirstName(), userRegDto.getLastName());
	}

	@Override
	public UserProfileDto removeUser(String id, String auth) {
		// TODO remove forum user
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
		if(!credentials.getLogin().equals(id)) {
			throw new ForbiddenException();
		}
		UserAccount userAccount = userRepository.findById(id).get();
		userRepository.delete(userAccount);
		return new UserProfileDto(userAccount.getId(), userAccount.getFirstName(), userAccount.getLastName());
	}

}
