package com.legosoft.couhbase;

import org.junit.Test;

import com.legosoft.couhbase.service.impl.SparkMovimientosImpl;

import jdk.nashorn.internal.ir.annotations.Ignore;

public class SparkMovimientosTest {
	
	private SparkMovimientosImpl impl = new SparkMovimientosImpl();
	
	
	@Test
	@Ignore
	public void loadCsv(){
		impl.loadCsv();
	}
	
	@Test
	@Ignore
	public void readKeyValueRDD(){
		impl.readKeyValueRDD();
	}
	
	@Test
	@Ignore
	public void readN1qlRDD(){
		impl.readN1qlRDD();
	}
	@Test
	@Ignore
	public void findWithataSet(){
		impl.findWithataSet();
	}
	@Test
	@Ignore
	public void save(){
		impl.save();
	}
	

}
