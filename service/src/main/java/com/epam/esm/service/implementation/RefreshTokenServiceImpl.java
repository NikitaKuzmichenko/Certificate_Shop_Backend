package com.epam.esm.service.implementation;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.dto.mapper.RefreshTokenDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired private final RefreshTokenRepository repository;

	@Autowired private final UserService userService;

	public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserService userService) {
		this.repository = repository;
		this.userService = userService;
	}

	/**
	* @param token element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	@Override
	public Long saveOrUpdate(@Valid RefreshTokenDto token) {
		Optional<RefreshToken> existingToken = repository.findByUserIdId(token.getUserId());
		RefreshToken newToken = RefreshTokenDtoMapper.getRefreshTokenFromDto(token);
		if (existingToken.isPresent()) {
			newToken.setId(existingToken.get().getId());
			newToken.setUserId(existingToken.get().getUserId());
		} else {
			User user = UserDtoMapper.mapDtoToUser(userService.getById(token.getUserId()));
			if (user == null) {
				return null;
			}
			newToken.setUserId(user);
		}

		return repository.save(newToken).getId();
	}

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this repository
	*/
	@Override
	public RefreshTokenDto getById(long id) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(repository.findById(id).orElse(null));
	}

	/**
	* @param email of the {@code User} class
	* @return the element with the specified PK in this repository
	*/
	@Override
	public RefreshTokenDto getByUserEmail(String email) {
		if (email == null) {
			return null;
		}
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(
				repository.findByUserIdEmail(email).orElse(null));
	}

	/**
	* @param id of the {@code User} class
	* @return the element with the specified PK in this repository
	*/
	@Override
	public RefreshTokenDto getByUserId(long id) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(repository.findByUserIdId(id).orElse(null));
	}

	/**
	* @param token of the element to return
	* @return the element with the specified PK in this repository
	*/
	@Override
	public RefreshTokenDto getByToken(String token) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(repository.findByToken(token).orElse(null));
	}

	/**
	* @param id if repository containing element with this id - it will be removed from this
	*     repository
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	@Override
	public boolean delete(long id) {
		RefreshTokenDto token = getById(id);
		if (token == null) {
			return false;
		}
		repository.delete(RefreshTokenDtoMapper.getRefreshTokenFromDto(token));
		return true;
	}
}
