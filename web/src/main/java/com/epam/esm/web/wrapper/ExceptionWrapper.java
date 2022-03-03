package com.epam.esm.web.wrapper;

import java.io.Serializable;

public class ExceptionWrapper implements Serializable {

	private long errorCode;
	private String errorMsg;

	public ExceptionWrapper(long errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
