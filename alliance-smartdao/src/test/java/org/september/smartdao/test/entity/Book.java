package org.september.smartdao.test.entity;

import java.io.Serializable;
import java.util.Date;

import org.september.smartdao.anno.AutoIncrease;
import org.september.smartdao.anno.Column;
import org.september.smartdao.anno.Entity;
import org.september.smartdao.anno.Id;
import org.september.smartdao.anno.Sequence;
import org.september.smartdao.anno.Table;


@Entity
@Table(name="book")
public class Book implements Serializable{
 
	/**
     * 注意加transient，表示该字段不写入数据库
     */
    private static transient final long serialVersionUID = 984720158104498086L;

  @Id
//	@Sequence()
    @AutoIncrease
	private Long id;
	
	private String name;
	
	@Column(name="price")
	private Float price;

	@Column(name="print_date")
	private Date printDate;
	
	@Column(name="author_id")
	private Long authorId;
	
	private String isbn;
	
	private Integer words;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getWords() {
        return words;
    }

    public void setWords(Integer words) {
        this.words = words;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
