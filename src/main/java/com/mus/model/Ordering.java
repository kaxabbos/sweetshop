package com.mus.model;

import com.mus.model.enums.StatusOrdering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ordering {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Places place;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users client;
    @ManyToOne(fetch = FetchType.LAZY)
    private Products product;
    private String date;
    private String time;
    @Enumerated(EnumType.STRING)
    private StatusOrdering statusOrdering;

    public Ordering(Places place, Users client, Products product, String date, String time) {
        this.place = place;
        this.client = client;
        this.product = product;
        this.date = date;
        this.time = time;
        this.statusOrdering = StatusOrdering.WAITING;
    }
}
