    package com.domhub.api.model;

    import jakarta.persistence.*;
    import lombok.*;
    import com.domhub.api.dto.response.AccountDTO;

    @Entity
    @Table(name = "account")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class Account{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false, unique = true)
        private String userName;

        @Column(nullable = false)
        private String password;

        @ManyToOne
        @JoinColumn(name = "role_id", nullable = false)
        private Role role;

        public AccountDTO toAccountDTO() {
            return new AccountDTO(this.id, this.userName, this.role.getRoleName());
        }

    }