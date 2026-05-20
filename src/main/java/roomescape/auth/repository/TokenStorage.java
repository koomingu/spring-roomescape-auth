package roomescape.auth.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenStorage {
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public String createToken(Long memberId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, memberId);
        return token;
    }

    public Optional<Long> getMemberId(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}
