package com.project.apidbtester.clientdbinfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="ClientDBCredentials")
public class ClientDBCredentialsEntity {
    @Id
    private Long databaseId;
    private String databaseUrl;
    private String userName;
    private String password;
}
