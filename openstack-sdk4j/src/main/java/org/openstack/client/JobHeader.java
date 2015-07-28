package org.openstack.client;

import java.io.IOException;

import javax.ws.rs.ext.FilterContext;
import javax.ws.rs.ext.RequestFilter;

public class JobHeader implements RequestFilter {

	private String jobId;
	private String jobType;
	private String jobExt;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobExt() {
		return jobExt;
	}

	public void setJobExt(String jobExt) {
		this.jobExt = jobExt;
	}

	@Override
	public void preFilter(FilterContext context) throws IOException {
		context.getRequestBuilder().header("job_id", this.jobId);
		context.getRequestBuilder().header("job_type", this.jobType);
		context.getRequestBuilder().header("job_ext", this.jobExt);
	}

}
