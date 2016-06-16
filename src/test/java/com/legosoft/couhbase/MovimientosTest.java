package com.legosoft.couhbase;

import org.junit.Test;

import com.legosoft.couhbase.service.FileUpload;
import com.legosoft.couhbase.service.MovimientosService;
import com.legosoft.couhbase.service.impl.FileUploadImpl;
import com.legosoft.couhbase.service.impl.MovimientosServiceImpl;

import jdk.nashorn.internal.ir.annotations.Ignore;

public class MovimientosTest {

	private MovimientosService movimientosService = new MovimientosServiceImpl();
	private FileUpload fileUpload = new FileUploadImpl();
	
	@Test
	//@Ignore
	public void insertaDocumento(){
		//fileUpload.insertDocumetos();
		fileUpload.insertPrestamosDocumetos();
		fileUpload.closeAll();
		
	}
	@Test
	@Ignore
	public void consultN1qlQueryByMonto(){
		movimientosService.consultN1qlQueryByJoin();
		movimientosService.closeAll();
	}

}
