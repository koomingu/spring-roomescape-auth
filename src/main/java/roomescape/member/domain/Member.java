package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;
    private final Long storeId;

    public Member(Long id, String email, String password, String name, Role role, Long storeId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role != null ? role : Role.USER;
        this.storeId = storeId;
    }

    public Member(String email, String password, String name) {
        this(null, email, password, name, Role.USER, null);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isManagerOf(Long targetStoreId) {
        return this.role == Role.MANAGER && Objects.equals(this.storeId, targetStoreId);
    }

    public boolean isManager() {
        return this.role == Role.MANAGER;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Long getStoreId() {
        return storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
