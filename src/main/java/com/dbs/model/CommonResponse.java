package com.dbs.controller.model;


public class CommonResponse {
	// Generate Getters and Setters...
	private String timeStamp;
	private String exceptionDesc;
	private String status;

	public CommonResponse( String timeStamp, String exceptionDesc, String status) {
		this.timeStamp = timeStamp;
		this.exceptionDesc = exceptionDesc;
		this.status = status;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getExceptionDesc() {
		return exceptionDesc;
	}

	public void setExceptionDesc(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Response{" +
				", timeStamp='" + timeStamp + '\'' +
				", exceptionDesc='" + exceptionDesc + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
