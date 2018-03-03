package com.lszyhb.basicclass;

import java.util.List;

public class ShowPage<C> extends ShowAbt {
	private static final long serialVersionUID = -2709969219830031835L;
	private Integer start = 0;
	private Integer pageNum = 1;
	private Integer count = 1000;
	private Integer total = 0;

	private C c;// 条件
	private List<ShowAccount> t;// 结果

	public ShowPage() {
		super();
	}

	public ShowPage(C c) {
		super();
		this.c = c;
	}

	public ShowPage(int pageNum, int count) {
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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}

	public List<ShowAccount> getT() {
		return t;
	}

	public void setT(List<ShowAccount> t) {
		this.t = t;
	}

}
