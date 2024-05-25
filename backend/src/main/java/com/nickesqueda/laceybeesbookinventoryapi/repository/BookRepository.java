package com.nickesqueda.laceybeesbookinventoryapi.repository;

import com.nickesqueda.laceybeesbookinventoryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {}
