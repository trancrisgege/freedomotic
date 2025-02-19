/**
 *
 * Copyright (c) 2009-2022 Freedomotic Team http://www.freedomotic-platform.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Freedomotic; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.freedomotic.plugins.devices.persistence;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.freedomotic.plugins.devices.persistence.cassandra.CassandraCluster;

// TODO: Auto-generated Javadoc
/**
 * The Class CassandraTestSetup.
 */
public class CassandraTestSetup {
	
	/** The cluster. */
	public static CassandraCluster cluster;
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(CassandraTestSetup.class.getName());
	
	/**
	 * Cassandra setup.
	 */
	@BeforeClass
	public static void cassandraSetup() {
		logger.info("************** SET UP OF CASSANDRA FOR TEST *************");
		Properties props = new Properties();
		props.setProperty("cassandra.host", "127.0.0.1");
		props.setProperty("cassandra.port", "7000");
		
		cluster = new CassandraCluster(props);
		
		Assert.assertEquals(false, cluster.isKeyspaceCreated());
		Assert.assertEquals(true, cluster.init());
		
		Session session = cluster.getSession();
		Assert.assertEquals("3.9", assertCassandraStartup(session));
		session.close();
	}
	
	
	/**
	 * Cassandra tear down.
	 */
	@AfterClass
	public static void cassandraTearDown() {
		cluster.releaseResource();
		logger.info("***********CLUSTER RESOURCES RELEASED*************");
	}
	
	/**
	 * Root test to verify that the setup has occurred properly.
	 *
	 * @param session the session
	 * @return the string
	 */
	private static String assertCassandraStartup(Session session) {
		Row row = session.execute("select cluster_name, release_version from system.local").one();
		String cassandraVersion = row.getString("release_version");
		return cassandraVersion;
	}

}
