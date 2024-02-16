package com.example.cinema.dao;



import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> {
    Optional<T> save(T value);
    List<T> findAll(Class<T> type);
    void deleteById(ID id, Class<T> type);
    boolean existsById(ID id, Class<T> type);
    Optional<T> findById(ID id, Class<T> type);
}



