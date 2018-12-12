package com.radler.services;

import com.radler.entities.Book;
import com.radler.repos.BookRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("bookService")
public class BookServiceImp implements BookService {

	private BookRepository bookRepository;

	@Transactional(readOnly = true)
	public List<Book> findAllBook() {
		return Lists.newArrayList(bookRepository.findAll());
	}

	public Book findBookById(Long id) {
		return bookRepository.findById(id).get();
	}


	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	@Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Transactional(readOnly = true)
	public Page<Book> findAllByPage(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}


}
