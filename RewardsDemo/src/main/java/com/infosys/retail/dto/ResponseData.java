package com.infosys.retail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseData {
	
	private String msg;
	
	@JsonIgnore
	private boolean status;
	
	private Object data;
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ResponseData(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}	
}
