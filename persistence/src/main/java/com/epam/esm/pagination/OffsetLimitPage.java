package com.epam.esm.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class OffsetLimitPage implements Pageable {

	private int limit;
	private int offset;
	private Sort sort;

	public OffsetLimitPage(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		sort = Sort.unsorted();
	}

	public OffsetLimitPage(int limit, int offset, String column, boolean asc) {
		this.limit = limit;
		this.offset = offset;
		sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column);
	}

	@Override
	public int getPageNumber() {
		return offset / limit;
	}

	@Override
	public int getPageSize() {
		return limit;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public Pageable withPage(int pageNumber) {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}
}
