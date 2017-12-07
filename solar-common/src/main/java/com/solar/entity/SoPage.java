package com.solar.entity;

public class SoPage<T> extends SoAbt{
	private int start;
	private int pageNum;
	private int pageCount;
	private int totel;
	
	private T t;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotel() {
		return totel;
	}

	public void setTotel(int totel) {
		this.totel = totel;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
	
	
	
}
