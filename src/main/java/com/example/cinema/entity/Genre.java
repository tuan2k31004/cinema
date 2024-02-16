package com.example.cinema.entity;

public enum Genre {
    Action ("Hành Động"),
    Adventure ("Phiêu Lưu"),
    Animation ("Hoạt Hình"),
    Comedy ("Hài"),
    Crime ("Tội Phạm"),
    Family ("Gia Đình"),
    Horror ("Kinh Dị"),
    Romance ("Lãng Mạn"),
    Science_Fiction ("Khoa Học Viễn Tưởng"),
    Drama ("Tâm Lý"),
    War ("Chiến Tranh"),
    Superhero ("Siêu Anh Hùng"),
    Mystery ("Bí ẩn"),
    Mythology ("Thần Thoại"),
    Martial_Arts ("Võ Thuật"),
    Detective ("Trinh Thám");
    private String title;
    Genre(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
