package com.legosoft.couhbase.vo;

import java.io.Serializable;

public class Prestamos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private String montoPrestamo;
	 private String mesPrestamo;
	 private String diaPrestamo;
	public String getMontoPrestamo() {
		return montoPrestamo;
	}
	public void setMontoPrestamo(String montoPrestamo) {
		this.montoPrestamo = montoPrestamo;
	}
	public String getMesPrestamo() {
		return mesPrestamo;
	}
	public void setMesPrestamo(String mesPrestamo) {
		this.mesPrestamo = mesPrestamo;
	}
	public String getDiaPrestamo() {
		return diaPrestamo;
	}
	public void setDiaPrestamo(String diaPrestamo) {
		this.diaPrestamo = diaPrestamo;
	}

}
