package com.kafkamax.object;

public class StockData {
	
	private String symbol;
	private String series;
	private Float open;
	private Float high;
	private Float low;
	private Float close;
	private Float last;
	private Float prevclose;
	private Float tottrdqty;
	private Float tottrdval;
	private String timestamp;
	private Float totaltrades;
	private String isin;
	
	public StockData(String symbol, String series, String open, String high,
			String low, String close, String last, String prevclose,
			String tottrdqty, String tottrdval, String timestamp,
			String totaltrades, String isin) {
		super();
		this.symbol = symbol;
		this.series = series;
		this.open = Float.valueOf(open);
		this.high = Float.valueOf(high);
		this.low = Float.valueOf(low);
		this.close = Float.valueOf(close);
		this.last = Float.valueOf(last);
		this.prevclose = Float.valueOf(prevclose);
		this.tottrdqty = Float.valueOf(tottrdqty);
		this.tottrdval = Float.valueOf(tottrdval);
		this.timestamp = timestamp;
		this.totaltrades = Float.valueOf(totaltrades);
		this.isin = isin;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Float getOpen() {
		return open;
	}
	public void setOpen(Float open) {
		this.open = open;
	}
	public Float getHigh() {
		return high;
	}
	public void setHigh(Float high) {
		this.high = high;
	}
	public Float getLow() {
		return low;
	}
	public void setLow(Float low) {
		this.low = low;
	}
	public Float getClose() {
		return close;
	}
	public void setClose(Float close) {
		this.close = close;
	}
	public Float getLast() {
		return last;
	}
	public void setLast(Float last) {
		this.last = last;
	}
	public Float getPrevclose() {
		return prevclose;
	}
	public void setPrevclose(Float prevclose) {
		this.prevclose = prevclose;
	}
	public Float getTottrdqty() {
		return tottrdqty;
	}
	public void setTottrdqty(Float tottrdqty) {
		this.tottrdqty = tottrdqty;
	}
	public Float getTottrdval() {
		return tottrdval;
	}
	public void setTottrdval(Float tottrdval) {
		this.tottrdval = tottrdval;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Float getTotaltrades() {
		return totaltrades;
	}
	public void setTotaltrades(Float totaltrades) {
		this.totaltrades = totaltrades;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
}
