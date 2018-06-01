package com.sherwin.shercolor.colormath.domain;

public class LineSeg {
	public char	type;	// U -Ultra-deep, D - Deep, X - eXtra-white, L - Luminous
	public double begx;
	public double begy;
	public double endx;
	public double endy;
	
	public LineSeg(char type, double begx, double begy, double endx, double endy) {
		super();
		this.type = type;
		this.begx = begx;
		this.begy = begy;
		this.endx = endx;
		this.endy = endy;
	}
}
