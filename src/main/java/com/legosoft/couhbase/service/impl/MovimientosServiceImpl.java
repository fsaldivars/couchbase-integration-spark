package com.legosoft.couhbase.service.impl;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataRetrievalFailureException;

import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Sort;
import com.legosoft.couhbase.service.MovimientosService;

public class MovimientosServiceImpl extends ConexionBase implements MovimientosService {
	

	@Override
	public void consultN1qlQueryByMonto() {
		Statement fluentStatement =
	            Select.select("monto", "anio", "cliente", "mes")
	                //Expression.i escapes an expression with backticks
	                .from(i("default"))
	                //Expression.x creates an expression token that can be manipulated
	                //Expression.s creates a string literal
	                .where(x("monto").gt("0"));
		
		//flatMap() to send those events against the Couchbase Java SDK and merge the results asynchronously.
		
		getBucket()
	    	.async()
	    	.query(N1qlQuery.simple(fluentStatement))
	    	.flatMap(AsyncN1qlQueryResult::rows)
	    	.toBlocking()
	    	.forEach(row -> System.out.println(row.value()));
	    	System.out.println("End consultN1qlQuery");
	    	System.out.println(getBucket().name());
	    	
	}
	
	@Override
	public void consultN1qlQueryByJoin() {
		String param = "2015";
		Statement joinQuery = select("cliente.*")
	            .from(i(getBucket().name()).as("prestamos"))
	            .join(i(getBucket().name()).as("cliente") + " ON KEYS prestamos.idCliente")
	            .where(x("cliente.anio").eq(s(param)))
	            .orderBy(Sort.asc("cliente.anio"));
		
		System.out.println(joinQuery.toString());
		N1qlQueryResult otherResult = getBucket().query(joinQuery);
		List<Map<String, Object>> list = extractResultOrThrow(otherResult);
		list.forEach(it -> {System.out.println(it);});
	}

	 /**
     * Extract a N1Ql result or throw if there is an issue.
     */
    private List<Map<String, Object>> extractResultOrThrow(N1qlQueryResult result) {
        if (!result.finalSuccess()) {
        	System.out.println("Query returned with errors: " + result.errors());
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        }

        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        for (N1qlQueryRow row : result) {
            content.add(row.value().toMap());
        }
        return content;
    }

	@Override
	public void closeAll() {
		closeConexion();
		
	}

	

}
