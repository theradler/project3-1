package com.radler.web;


import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.radler.entities.Book;
import com.radler.services.BookService;
import com.radler.util.Message;
import com.radler.util.BookGrid;
import com.radler.util.UrlUtil;
import com.google.common.collect.Lists;

@RequestMapping("/books")
@Controller
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        logger.info("Listing books");

        List<Book> books = bookService.findAllBook();
        uiModel.addAttribute("books", books);

        logger.info("No. of books: " + books.size());

        return "books/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        Book book = bookService.findBookById(id);
        uiModel.addAttribute("book", book);

        return "books/show";
    }


    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Book book, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Updating book");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("book_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("book", book);
            return "books/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("book_save_success", new Object[]{}, locale)));
        bookService.save(book);
        return "redirect:/books/" + UrlUtil.encodeUrlPathSegment(book.getId().toString(),
                httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("book", bookService.findBookById(id));
        return "books/update";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(
    		@Valid Book book, 
    		BindingResult bindingResult, 
    		Model uiModel, 
    		HttpServletRequest httpServletRequest, 
    		RedirectAttributes redirectAttributes, 
    		Locale locale
    		)
	{
        logger.info("Creating book");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute(
            		"message", new Message(
            				"error",
            				messageSource.getMessage(
            						"book_save_fail", 
            						new Object[]{}, 
            						locale)));
            uiModel.addAttribute("book", book);
            return "books/create";
        }
        
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute(
        		"message", 
        		new Message(
        				"success",
        				messageSource.getMessage(
        						"book_save_success", 
        						new Object[]{}, 
        						locale)));

        logger.info("Book id: " + book.getId());       

        bookService.save(book);
        return "redirect:/books/";
    }

    

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
    	Book book = new Book();
        uiModel.addAttribute("book", book);

        return "books/create";
    }
    @ResponseBody
    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces="application/json")
    public BookGrid listGrid(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "rows", required = false) Integer rows,
                                @RequestParam(value = "sidx", required = false) String sortBy,
                                @RequestParam(value = "sord", required = false) String order) {

        logger.info("Listing books for grid with page: {}, rows: {}", page, rows);
        logger.info("Listing books for grid with sort: {}, order: {}", sortBy, order);

        // Process order by
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("birthDateString"))
            orderBy = "birthDate";

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = new Sort(Sort.Direction.DESC, orderBy);
            } else
                sort = new Sort(Sort.Direction.ASC, orderBy);
        }

        // Constructs page request for current page
        // Note: page number for Spring Data JPA starts with 0, while jqGrid starts with 1
        PageRequest pageRequest = null;

        if (sort != null) {
            pageRequest = PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }

        Page<Book> bookPage = bookService.findAllByPage(pageRequest);

        // Construct the grid data that will return as JSON data
        BookGrid bookGrid = new BookGrid();

        bookGrid.setCurrentPage(bookPage.getNumber() + 1);
        bookGrid.setTotalPages(bookPage.getTotalPages());
        bookGrid.setTotalRecords(bookPage.getTotalElements());

        bookGrid.setBookData(Lists.newArrayList(bookPage.iterator()));

        return bookGrid;
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
