package com.pactera.hscloud.hscloudhandler.bo;

import java.io.Serializable;

public class Event implements Serializable {

	private Long id;
	private String event_time;
	private String deal_time;
	private String finished_time;
	private String jobserver;
	private String event;

	public String getJobserver() {
		return jobserver;
	}

	public void setJobserver(String jobserver) {
		this.jobserver = jobserver;
	}

	public String getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}

	public String getFinished_time() {
		return finished_time;
	}

	public void setFinished_time(String finished_time) {
		this.finished_time = finished_time;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", event_time=" + event_time
				+ ", deal_time=" + deal_time + ", finished_time="
				+ finished_time + ", jobserver=" + jobserver + ", event="
				+ event + "]";
	}

}
