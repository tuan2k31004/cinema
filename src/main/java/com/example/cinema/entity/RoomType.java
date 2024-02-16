package com.example.cinema.entity;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoomType {
    VIP("VIP", 20, 4),
    THREE_D("3D", 40, 4),
    TWO_D("2D", 100, 10),
    IMAX("IMAX", 210, 15);
    private String title;
    private int capacity;
    private int row;

    @Override
    public String toString() {
        return title;
    }
}
