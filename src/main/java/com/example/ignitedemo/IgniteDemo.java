package com.example.ignitedemo;

import com.bimetri.products.bienport.commons.dto.DTORule;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class IgniteDemo
{
	public static void main ( String[] args )
	{
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

		igniteConfiguration.setClientMode( true );

		igniteConfiguration.setPeerClassLoadingEnabled( false );

		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setAddresses( Collections.singletonList( "127.0.0.1:11211" ) );

		igniteConfiguration.setDiscoverySpi( new TcpDiscoverySpi().setIpFinder( ipFinder ) );

		Ignite ignite = Ignition.start(igniteConfiguration);
		IgniteCache cache1 = ignite.getOrCreateCache("armDisarmCommandRegistry");
		System.out.println(cache1.get( 370 ));


		IgniteCache<String , DynamicData> cache2 = ignite.getOrCreateCache("resourceDynamicData");
		AtomicInteger i = new AtomicInteger( 1 );
		cache2.forEach( action -> {
			System.out.println(action.getKey() + " " + action.getValue());
			i.getAndIncrement();
		} );
		System.out.println(i.get());
		System.out.println( " Cache " + cache2.get( "EBS-CP-1057985" ).toString());

		IgniteCache<Long, DTORule> cache3 = ignite.getOrCreateCache("ruleCache");
		cache3.forEach( action -> {
			System.out.println(action.getKey() + " " + action.getValue());
		} );

//		ignite.close();
	}
}
