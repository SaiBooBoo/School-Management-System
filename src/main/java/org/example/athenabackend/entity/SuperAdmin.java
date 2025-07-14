package org.example.athenabackend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@DiscriminatorValue("SUPER_ADMIN")
@NoArgsConstructor
public class SuperAdmin extends User{

    private String email;
    private String phoneNumber;
    private BigDecimal earning;

    public SuperAdmin(String username, String password, String email, String phoneNumber, BigDecimal earning) {
        super(username, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.earning = earning;
    }
}
