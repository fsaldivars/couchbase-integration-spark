package com.legosoft.couhbase.service.impl;

import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.Statement;
import com.legosoft.couhbase.service.FileUpload;

public class FileUploadImpl  extends ConexionBase implements FileUpload {
	
	
	@Override
	public void insertDocumetos() {
		try (
				Stream<String> stream = Files.lines(Paths.get("cliente.txt"))) {
				String t1 = "", t2 = "",t3 = "",t4 = "",t5 = "",t6 = "";
				Iterator<String > element = stream.iterator();
				int header = 0;
				while(element.hasNext()){
					String it = element.next();
					if(header == 0) {
						t1 = it.split(",")[0].trim();
						t2 = it.split(",")[1].trim();
						t3 = it.split(",")[2].trim();
						t4 = it.split(",")[3].trim();
						t5 = it.split(",")[4].trim();
						t6 = it.split(",")[5].trim();
					}else {
						JsonObject movimiento = JsonObject.empty()
								.put("ENERO", it.split(",")[6].toString().trim())
								.put("FEBRERO", it.split(",")[7].toString().trim())
								.put("MARZO", it.split(",")[8].toString().trim());
						if(it.split(",").length > 8)
							movimiento.put("ABRIL", it.split(",")[8].toString().trim());
								
						JsonObject user = JsonObject.empty()
							    .put(t1, it.split(",")[0].toString().trim())
							    .put(t2, it.split(",")[1].toString().trim())
							    .put(t3, it.split(",")[2].toString().trim())
							    .put(t4, it.split(",")[3].toString().trim())
							    .put(t5, it.split(",")[4].toString().trim())
							    .put(t6, it.split(",")[5].toString().trim())
							    .put("movimiento", movimiento);
						
						JsonDocument doc = JsonDocument.create("cliente-" +it.split(",")[4].toString().trim(),  user);
						JsonDocument response = getBucket().upsert(doc);
					}
					header++;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("End insertDocumetns");
			//closeConexion();
		}
	@Override
	public void insertPrestamosDocumetos() {
		try (
				Stream<String> stream = Files.lines(Paths.get("prestamos.txt"))) {
				String t1 = "", t2 = "",t3 = "",t4 = "",t5 = "";
				Iterator<String > element = stream.iterator();
				int header = 0;
				while(element.hasNext()){
					String it = element.next();
					if(header == 0) {
						t1 = it.split(",")[0].trim();
						t2 = it.split(",")[1].trim();
						t3 = it.split(",")[2].trim();
						t4 = it.split(",")[3].trim();
						t5 = it.split(",")[4].trim();
					}else {
						JsonObject movimiento = JsonObject.empty()
								.put("PAGO-ENERO", it.split(",")[5].toString().trim())
								.put("PAGO-FEBRERO", it.split(",")[6].toString().trim())
								.put("PAGO-MARZO", it.split(",")[7].toString().trim());
						if(it.split(",").length > 8)
							movimiento.put("PAGO-ABRIL", it.split(",")[8].toString().trim());
								
						JsonObject user = JsonObject.empty()
							    .put(t1, it.split(",")[0].toString().trim())
							    .put(t2, it.split(",")[1].toString().trim())
							    .put(t3, it.split(",")[2].toString().trim())
							    .put(t4, it.split(",")[3].toString().trim())
							    .put(t5, findClienteN1qlQuery(it.split(",")[4].toString().trim()))
							    .put("movimiento", movimiento);
						
						JsonDocument doc = JsonDocument.create("prestamo-" + it.split(",")[4].toString().trim(),  user);
						JsonDocument response = getBucket().upsert(doc);
					}
					header++;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		System.out.println("End insertDocumetns");
		//closeConexion();
		}
	
	/**
	 * Expression.i escapes an expression with backticks
	 * Expression.x creates an expression token that can be manipulated
	 * Expression.s creates a string literal
	 */
	private String findClienteN1qlQuery(String idCliente) {
		String keyForClient = idCliente;
		Statement fluentStatement =
	            Select.select("meta(b) AS meta")
	                .from(i(getBucket().name()).as("b"))
	                .where(x("idClienteBanco").eq("'" + idCliente + "'"));

		N1qlQueryResult result = getBucket().query(N1qlQuery.simple(fluentStatement));
		for (N1qlQueryRow row : result) {
			if(row.value().containsKey("meta"))
				keyForClient = row.value().getObject("meta").getString("id");
        }

		System.out.println(fluentStatement.toString() + " Result [" + keyForClient + "]");
		return keyForClient;
	}
	@Override
	public void closeAll() {
		closeConexion();
		
	}

}
