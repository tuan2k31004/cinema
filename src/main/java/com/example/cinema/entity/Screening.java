package com.example.cinema.entity;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "screening")
public class Screening extends RecursiveTreeObject<Screening> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int price;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "auditorium_id")
    private Auditorium auditorium;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "screening",fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    public void updateScreen(Screening other){
        start = other.getStart();
        end = other.getEnd();
        price = other.getPrice();
        auditorium = other.getAuditorium();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Screening)) return false;
        Screening screening = (Screening) o;
        return id == screening.id;
    }
}
