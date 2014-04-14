package de.dialogdata.aclabs.common;

import java.io.Serializable;
import java.util.List;

public class PaginationResult<T> implements Serializable {

	private static final long serialVersionUID = -3239348935655344165L;

	private final long count;
	private final List<T> page;

	public PaginationResult(long count, List<T> page) {
		super();
		this.count = count;
		this.page = page;
	}

	public long getCount() {
		return count;
	}

	public List<T> getPage() {
		return page;
	}
}
