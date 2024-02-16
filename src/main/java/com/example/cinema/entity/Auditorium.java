package com.example.cinema.entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.ExtensionMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auditorium")
public class Auditorium extends RecursiveTreeObject<Auditorium> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.REMOVE)
    private List<Screening> screens;
    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;


    public Auditorium(String name, RoomType type) {
        this.name = name;
        this.type = type;
        createSeat();
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auditorium)) return false;
        Auditorium that = (Auditorium) o;
        return id == that.id;
    }

    private void createSeat() {
        seats = new ArrayList<>();
        char a = 'A';
        for (int i = 1; i <= type.getRow(); i++) {
            for (int j = 1; j <= type.getCapacity() / type.getRow(); j++) {
                Seat seat = Seat.builder()
                        .auditorium(this)
                        .indexRow(i)
                        .title(String.format("%s%s", a, j))
                        .build();
                seats.add(seat);
            }
            a++;
        }
    }
}
