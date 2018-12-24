package org.september.smartdao.test.service;

import org.september.smartdao.CommonDao;
import org.september.smartdao.test.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	@Autowired
	private CommonDao commonDao;
	
	@Transactional
	public void saveBookError1(){
		Book book = new Book();
		book.setName("test tranc rollback");
		book.setWords(199);
		book.setAuthorId(2L);
		commonDao.save(book);
		throw new RuntimeException("test tranc rollback");
	}
	
	public void saveBookError2(){
		Book book = new Book();
		book.setName("test add with out tranc");
		book.setWords(199);
		book.setAuthorId(2L);
		commonDao.save(book);
		throw new RuntimeException("test tranc rollback");
	}
}
