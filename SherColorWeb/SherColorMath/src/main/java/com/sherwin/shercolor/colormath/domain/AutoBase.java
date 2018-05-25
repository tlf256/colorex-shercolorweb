package com.sherwin.shercolor.colormath.domain;

public class AutoBase {
	private double l;
	private double a;
	private double b;
	private double C;
	private char base1;
	private char base2;
	
	public AutoBase() {
		super();
	}

	public AutoBase(double l, double a, double b) {
		super();
		this.l = l;
		this.a = a;
		this.b = b;
	}

	public double getL() {
		return l;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return C;
	}

	public char getBase1() {
		return base1;
	}

	public char getBase2() {
		return base2;
	}

	public void setL(double l) {
		this.l = l;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void setC(double c) {
		C = c;
	}

	public void setBase1(char base1) {
		this.base1 = base1;
	}

	public void setBase2(char base2) {
		this.base2 = base2;
	}
	
	
	
}
