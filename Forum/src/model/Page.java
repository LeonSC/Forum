package model;

import java.util.List;

public class Page<T> {

	private List<T> list;
	private int totalInPage;
	private int totalPages;
	private long total;//总条目数
	private int nowPage;//从1开始
	private int skip;
	
	public int getPage()
	{
		this.skip=(this.nowPage-1)*this.totalInPage;
		this.totalPages=(int)Math.ceil((double)this.total/this.totalInPage);
		
		return 0;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalInPage() {
		return totalInPage;
	}

	public void setTotalInPage(int totalInPage) {
		this.totalInPage = totalInPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		
		if(nowPage<1)
		{
			nowPage=1;
		}
		
		this.nowPage = nowPage;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}
	
	
	
}
