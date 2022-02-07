package com.example.ignitedemo;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

public class IgniteDemo
{
	public static void main ( String[] args )
	{
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

		igniteConfiguration.setClientMode( true );

		igniteConfiguration.setPeerClassLoadingEnabled( false );

		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setAddresses( Collections.singletonList( "127.0.0.1:47500..47509" ) );

		igniteConfiguration.setDiscoverySpi( new TcpDiscoverySpi().setIpFinder( ipFinder ) );

		Ignite ignite = Ignition.start(igniteConfiguration);

		IgniteCache<String, String> cache = ignite.getOrCreateCache("testCache");
		cache.put( "key1", "value1");
		cache.put( "key2", "value2" );

		System.out.println("/////keş oluşturuldu");

		ignite.compute(ignite.cluster().forLocal()).broadcast( new RemoteTask() );

		System.out.println(">> Compute task is executed, check for output on the server nodes.");

		ignite.close();
	}
	private static class RemoteTask implements IgniteRunnable
	{
		@IgniteInstanceResource
		Ignite ignite;

		@Override
		public void run ()
		{
			System.out.println( ">> Executing the compute task" );

			System.out.println(
					"   Node ID: " + ignite.cluster().localNode().id() + "\n" + "   OS: " + System.getProperty( "os.name" ) + "   JRE: "
							+ System.getProperty( "java.runtime.name" ) );

			IgniteCache<String, String> cache = ignite.cache( "testCache" );

			System.out.println( ">> " + cache.get( "key1" ) + " " + cache.get( "key2" ) );
		}
	}
}
