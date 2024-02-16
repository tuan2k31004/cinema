package com.example.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seat")
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int indexRow;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_id",nullable = false)
    private Auditorium auditorium;
    @OneToMany(mappedBy = "seat",cascade = CascadeType.ALL)
    private List<SeatReserved> seasReserved = new ArrayList<>();
}
