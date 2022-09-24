package com.jsblanco.springbanking.models.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("student_checking_account")
public class StudentCheckingAccount extends Account {
}