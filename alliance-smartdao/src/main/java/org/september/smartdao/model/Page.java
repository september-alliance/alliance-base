package org.september.smartdao.model;

import java.util.List;


/**
 * 分页类,默认每页15条
 */
public class Page<T> {
	
	private int pageSize; 			//每页显示记录数
	private int totalResult;			//总记录数
	private int currentPage=1;
	
	private int startRow=1;
	
	private int endRow;
	
	/**
	 * 分页查询返回的数据
	 */
	private List<T> result;
	
	public Page(){
		if(this.pageSize==0){
			this.pageSize = 15;
		}
	}
	
	public Page(int pageSize){
		this.pageSize = pageSize;
	}
	
	public int getTotalPage() {
		if(totalResult==0) {
			return 1;
		}
		return  totalResult/pageSize +(totalResult%pageSize==0? 0:1);
	}
	
	public int getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize(){
		return this.pageSize;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	
}
