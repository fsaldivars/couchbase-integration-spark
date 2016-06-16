package com.legosoft.couhbase.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.EqualTo;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.spark.japi.CouchbaseSparkContext;
import com.couchbase.spark.rdd.CouchbaseQueryRow;
import com.couchbase.spark.sql.DataFrameWriterFunctions;
import com.legosoft.couhbase.vo.Prestamos;


import static com.couchbase.spark.japi.CouchbaseDocumentRDD.couchbaseDocumentRDD;
import static com.couchbase.spark.japi.CouchbaseSparkContext.couchbaseContext;
import static com.couchbase.spark.japi.CouchbaseDataFrameReader.couchbaseReader;
import scala.collection.immutable.Map;

public class SparkMovimientosImpl {
	
	
	public void loadCsv(){
		SparkConf conf = new SparkConf()
		        .setAppName("SF Salaries")
		        .setMaster("local[*]")
		        .set("com.couchbase.bucket.default", "");
		
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(javaSparkContext);
		
		DataFrame dataFrame = sqlContext.read()
			    .format("com.databricks.spark.csv")
			    .option("inferSchema", "true")
			    .option("header", "true")
			    .load("SalesJan2009.csv");
		// Taking only 0.1% for test
		dataFrame = dataFrame.sample(false, 0.01);
		dataFrame = dataFrame.withColumn("Id", dataFrame.col("Id").cast("string"));
		DataFrameWriterFunctions dataFrameWriterFunctions = new DataFrameWriterFunctions(dataFrame.write());
		// this option ensure the Id field will be used as key awesome
		 Map<String, String> options = new Map.Map1<String, String>("idField", "Id");
		dataFrameWriterFunctions.couchbase(options);
	
	}
	
	public void readKeyValueRDD(){
		SparkConf conf = new SparkConf()
		        .setAppName("javaSample")
		        .setMaster("local[*]")
		        .set("com.couchbase.bucket.default", "");
		JavaSparkContext sc = new JavaSparkContext(conf);
		CouchbaseSparkContext csc = couchbaseContext(sc);
		
		List<JsonDocument> docs = csc
			    .couchbaseGet(Arrays.asList("prestamo-1", "cliente-2"))
			    .collect();

			System.out.println(docs);
	
	}
	
	public void readN1qlRDD(){
		SparkConf conf = new SparkConf()
		        .setAppName("javaSample")
		        .setMaster("local[*]")
		        .set("com.couchbase.bucket.default", "");
		JavaSparkContext sc = new JavaSparkContext(conf);
		CouchbaseSparkContext csc = couchbaseContext(sc);
		
		List<CouchbaseQueryRow> results = csc
			    .couchbaseQuery(N1qlQuery.simple("SELECT * FROM `default` LIMIT 2"))
			    .collect();

			System.out.println(results);
	
	}
	
	
	public void save(){
		SparkConf conf = new SparkConf()
		        .setAppName("javaSample")
		        .setMaster("local[*]")
		        .set("com.couchbase.bucket.default", "");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JsonObject movimiento = JsonObject.empty()
				.put("id", "123")
				.put("version", "v2")
				.put("coto", Double.MIN_VALUE);
		couchbaseDocumentRDD(
			    sc.parallelize(Arrays.asList(JsonDocument.create("documentoSpark", movimiento)))
			).saveToCouchbase();

	
	}
	
	 public void findWithataSet() {
		 SparkConf conf = new SparkConf()
			        .setAppName("javaSample")
			        .setMaster("local[*]")
			        .set("com.couchbase.bucket.default", "");
			
		 JavaSparkContext sc = new JavaSparkContext(conf);
			SQLContext sql = new SQLContext(sc);
			
	        Dataset<Prestamos> datset = couchbaseReader(sql.read())
	            .couchbase(new EqualTo("diaPrestamo", "'10'"))
	            .select(new Column("montoPrestamo").as("montoPrestamo"))
	            .as(Encoders.bean(Prestamos.class));

	        List<Prestamos> allAirports = datset.collectAsList();
	    }
	

}
