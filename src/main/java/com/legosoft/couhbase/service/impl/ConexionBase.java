package com.legosoft.couhbase.service.impl;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public abstract class ConexionBase {

	private Cluster cluster;
	private Bucket bucket;
	
	

	public Bucket getBucket() {
		if(bucket == null)
			getConexion();
		return bucket;
	}

	
	public void closeConexion() {
		Boolean closed = bucket.close();
		Boolean disconnected = cluster.disconnect();
		
		System.out.println("cluster disconnected: " + disconnected);
		System.out.println("bucket closed: " + closed);
		
	}
	
	public void getConexion() {
		cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
			    .builder()
			    .queryEnabled(true)
			    .build());
		
		bucket = cluster.openBucket("default");
		
	}
}
