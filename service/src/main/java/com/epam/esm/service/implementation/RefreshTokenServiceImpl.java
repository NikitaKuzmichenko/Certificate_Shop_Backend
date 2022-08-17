package com.epam.esm.service.implementation;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.dto.mapper.RefreshTokenDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.exception.InvalidTokenException;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired private final RefreshTokenRepository repository;

	@Autowired private final UserService userService;

	public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserService userService) {
		this.repository = repository;
		this.userService = userService;
	}

	@Override
	public Long saveOrUpdate(@Valid RefreshTokenDto token) {
		Optional<RefreshToken> existingToken = repository.findByUserIdId(token.getUserId());
		RefreshToken newToken = RefreshTokenDtoMapper.getRefreshTokenFromDto(token);
		if (existingToken.isPresent()) {
			newToken.setId(existingToken.get().getId());
			newToken.setUserId(existingToken.get().getUserId());
		} else {
			User user = UserDtoMapper.mapDtoToUser(userService.getById(token.getUserId()));
			newToken.setUserId(user);
		}
		try {
			return repository.save(newToken).getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public RefreshTokenDto getById(long id) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(
				repository.findById(id).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public RefreshTokenDto getByUserEmail(String email) {
		if (email == null) {
			throw new BadInputException();
		}
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(
				repository.findByUserIdEmail(email).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public RefreshTokenDto getByUserId(long id) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(
				repository.findByUserIdId(id).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public RefreshTokenDto getByToken(String token) {
		return RefreshTokenDtoMapper.mapRefreshTokenToDto(
				repository.findByToken(token).orElseThrow(InvalidTokenException::new));
	}

	@Override
	public void delete(long id) {
		RefreshTokenDto token = getById(id);
		repository.delete(RefreshTokenDtoMapper.getRefreshTokenFromDto(token));
	}
}
