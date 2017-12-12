package com.solar.entity;

public class SoPage<C, R> extends SoAbt {
	private static final long serialVersionUID = -2709969219830031835L;
	private Integer start = 0;
	private Integer pageNum = 1;
	private Integer count = 10;
	private Integer totel;

	private C c;// 条件
	private R t;// 结果

	public SoPage() {
		super();
	}

	public SoPage(C c) {
		super();
		this.c = c;
	}

	public SoPage(int pageNum, int count) {
		super();
		this.pageNum = pageNum;
		this.count = count;
		this.start = count * (pageNum - 1);
	}

	public Integer getStart() {
		this.start = count * (pageNum - 1);
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getTotel() {
		return totel;
	}

	public void setTotel(Integer totel) {
		this.totel = totel;
	}

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}

	public R getT() {
		return t;
	}

	public void setT(R t) {
		this.t = t;
	}

}
