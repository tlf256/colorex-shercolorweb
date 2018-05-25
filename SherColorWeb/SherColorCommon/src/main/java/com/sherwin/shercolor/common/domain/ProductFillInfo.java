package com.sherwin.shercolor.common.domain;

public class ProductFillInfo {

	private double perGallonMaxLoad;
	private double perGallonMaxOverLoad;
	private double estimatePerGallonMaxLoad;
	private double productMaxLoad;
	private double productMaxOverLoad;
	private double estimateMaxLoad;
	private double sizeRatio;
	
	public ProductFillInfo() {
		perGallonMaxLoad = 0D;
		perGallonMaxOverLoad = 0D;
		estimatePerGallonMaxLoad = 15D;
		productMaxLoad = 0D;
		productMaxOverLoad = 0D;
		estimateMaxLoad = 0D;
		sizeRatio = 0D;
		
	}

	public double getPerGallonMaxLoad() {
		return perGallonMaxLoad;
	}

	public double getPerGallonMaxOverLoad() {
		return perGallonMaxOverLoad;
	}

	public double getEstimatePerGallonMaxLoad() {
		return estimatePerGallonMaxLoad;
	}

	public double getProductMaxLoad() {
		return productMaxLoad;
	}

	public double getProductMaxOverLoad() {
		return productMaxOverLoad;
	}

	public double getEstimateMaxLoad() {
		return estimateMaxLoad;
	}

	public double getSizeRatio() {
		return sizeRatio;
	}

	public void setPerGallonMaxLoad(double perGallonMaxLoad) {
		this.perGallonMaxLoad = perGallonMaxLoad;
	}

	public void setPerGallonMaxOverLoad(double perGallonMaxOverLoad) {
		this.perGallonMaxOverLoad = perGallonMaxOverLoad;
	}

	public void setEstimatePerGallonMaxLoad(double estimatePerGallonMaxLoad) {
		this.estimatePerGallonMaxLoad = estimatePerGallonMaxLoad;
	}

	public void setProductMaxLoad(double productMaxLoad) {
		this.productMaxLoad = productMaxLoad;
	}

	public void setProductMaxOverLoad(double productMaxOverLoad) {
		this.productMaxOverLoad = productMaxOverLoad;
	}

	public void setEstimateMaxLoad(double estimateMaxLoad) {
		this.estimateMaxLoad = estimateMaxLoad;
	}

	public void setSizeRatio(double sizeRatio) {
		this.sizeRatio = sizeRatio;
	}
	
	

}
