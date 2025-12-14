package org.example.backend.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class EnrollForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;

    @ManyToOne
    private FormEnrollment formEnrollment;
    @ElementCollection
    private List<String> formValues;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FormEnrollment getFormEnrollment() {
        return formEnrollment;
    }

    public void setFormEnrollment(FormEnrollment formEnrollment) {
        this.formEnrollment = formEnrollment;
    }

    public List<String> getFormValues() {
        return formValues;
    }

    public void setFormValues(List<String> formValues) {
        this.formValues = formValues;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
