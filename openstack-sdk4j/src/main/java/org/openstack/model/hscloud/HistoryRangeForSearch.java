package org.openstack.model.hscloud;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("range")
public class HistoryRangeForSearch {

	private long from;

	private long to;

	public HistoryRangeForSearch(long from, long to){
		this.from = from;
		this.to = to;
	}
	
	/**
	 * @return the from
	 */
	public long getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(long from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public long getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(long to) {
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HistoryRangeForSearch [ " + this.from + " " + this.to +" ]";
	}
	
	

}
