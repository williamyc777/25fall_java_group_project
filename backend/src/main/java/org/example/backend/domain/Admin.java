package org.example.backend.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "Admin")
public class Admin extends AbstractUser {
}
