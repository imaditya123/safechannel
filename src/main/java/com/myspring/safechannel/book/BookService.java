package com.myspring.safechannel.book;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public void save(Book request) {
//        var book = Book.builder()
//                .id(request.getId())
//                .author(request.getAuthor())
//                .isbn(request.getIsbn())
//                .build();
        repository.save(request);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }
    
    public Page<Book> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public Optional<Book> findById(Integer id) {
        return repository.findById(id);
    }
}
