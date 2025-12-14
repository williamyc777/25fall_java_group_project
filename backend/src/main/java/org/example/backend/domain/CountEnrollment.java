package org.example.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class CountEnrollment extends AbstractEnrollment {

    private long capacity;
    private long count = 0;

    public long getCapacity() {
        return capacity;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}
