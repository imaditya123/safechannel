package com.myspring.safechannel.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
	List<Book> findAll();
	Page<Book> findAll(Pageable pageable);

}
