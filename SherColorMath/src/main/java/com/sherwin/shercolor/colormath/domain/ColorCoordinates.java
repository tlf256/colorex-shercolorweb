package com.sherwin.shercolor.colormath.domain;

public class ColorCoordinates {
	private double cieX;
	private double cieY;
	private double cieZ;
	
	private double cieL;
	private double cieA;
	private double cieB;
	private double cieC;
	private double cieH;
	
	private int rgbRed;
	private int rgbGreen;
	private int rgbBlue;
	private String rgbHex;
	
	public double getCieX() {
		return cieX;
	}
	public double getCieY() {
		return cieY;
	}
	public double getCieZ() {
		return cieZ;
	}
	public double getCieL() {
		return cieL;
	}
	public double getCieA() {
		return cieA;
	}
	public double getCieB() {
		return cieB;
	}
	public double getCieC() {
		return cieC;
	}
	public double getCieH() {
		return cieH;
	}
	public int getRgbRed() {
		return rgbRed;
	}
	public int getRgbGreen() {
		return rgbGreen;
	}
	public int getRgbBlue() {
		return rgbBlue;
	}
	public String getRgbHex() {
		return rgbHex;
	}
	
	//////////////  SETTERS  ////////////////
	
	public void setCieX(double cieX) {
		this.cieX = cieX;
	}
	public void setCieY(double cieY) {
		this.cieY = cieY;
	}
	public void setCieZ(double cieZ) {
		this.cieZ = cieZ;
	}
	public void setCieL(double cieL) {
		this.cieL = cieL;
	}
	public void setCieA(double cieA) {
		this.cieA = cieA;
	}
	public void setCieB(double cieB) {
		this.cieB = cieB;
	}
	public void setCieC(double cieC) {
		this.cieC = cieC;
	}
	public void setCieH(double cieH) {
		this.cieH = cieH;
	}
	public void setRgbRed(int rgbRed) {
		this.rgbRed = rgbRed;
	}
	public void setRgbGreen(int rgbGreen) {
		this.rgbGreen = rgbGreen;
	}
	public void setRgbBlue(int rgbBlue) {
		this.rgbBlue = rgbBlue;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}
	
}
