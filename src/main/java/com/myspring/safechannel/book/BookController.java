package com.myspring.safechannel.book;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myspring.safechannel.config.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

	final Logger log = LoggerFactory.getLogger(getClass());
	private final BookService service;
	private final JwtService jwtService;

	@PostMapping("/add")
	public ResponseEntity<?> save(@RequestBody Book request, @RequestHeader("Authorization") String bearerToken) {
		log.info("Token: {}", bearerToken);

		final String user = jwtService.extractUsername(bearerToken.substring(7));
		request.setCurrentUser(user);
		service.save(request);
		return ResponseEntity.accepted().build();
	}

	@GetMapping
	public ResponseEntity<Page<Book>> findAllBooks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = service.findAll(pageable);

		return ResponseEntity.ok(booksPage);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Integer id) {

		return service.findById(id).map(book -> new ResponseEntity<>(book, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

}