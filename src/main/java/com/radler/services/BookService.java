package com.radler.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.radler.entities.Book;

public interface BookService {
	
	List<Book> findAllBook();
	Book findBookById(Long id);
	Book save(Book book);
	Page<Book> findAllByPage(Pageable pageable);
	

}
