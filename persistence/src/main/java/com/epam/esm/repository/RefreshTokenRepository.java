package com.epam.esm.repository;

import com.epam.esm.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public interface RefreshTokenRepository extends PagingAndSortingRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUserIdEmail(String email);

	Optional<RefreshToken> findByUserIdId(long id);

	void deleteByUserIdId(long id);
}
