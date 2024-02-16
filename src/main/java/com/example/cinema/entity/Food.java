package com.example.cinema.entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "food")
public class Food extends RecursiveTreeObject<Food> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private int price;
    @ManyToMany(mappedBy = "foods")
    private List<Reservation> reservations;

    public Food(String name, String description, String thumbnail, int price) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
    }
    public void updateFood(Food other) {
        name = other.getName();
        description = other.getDescription();
        thumbnail = other.getThumbnail();
        price = other.getPrice();
    }
}
