/**
 * @author gramcha
 * 12-Feb-2018 5:33:50 PM
 * 
 */
package com.gramcha.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.gramcha.config.ConfigProvider;

@Service
public class ConsistentHashingService<T> {

	@Autowired
	ConfigProvider config;
	
	private  HashFunction hashFunction = Hashing.murmur3_128(0);
	private  int numberOfReplicas;
	private  SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	@PostConstruct
	public void init() throws IOException {
		this.numberOfReplicas = config.getNumberOfReplicas();
		List<String> redisNodes = config.getRedisInstances().stream().map(item->item.getHost()+":"+item.getPort()).collect(Collectors.toList());
		System.out.println("no of replicas -"+numberOfReplicas);
		System.out.println("redisNodes -"+redisNodes);
		addNodes((Collection<T>) redisNodes);
	}

	public void addNodes(Collection<T> nodes) throws IOException {
		for (T node : nodes) {
			add(node);
		}
		// Using iterator in SortedMap
		Set<Entry<Integer, T>> s = circle.entrySet();
		Iterator i = s.iterator();

		// Traversing map. Note that the traversal
		// produced sorted (by keys) output .
		while (i.hasNext()) {
			Map.Entry m = (Map.Entry) i.next();

			int key = (Integer) m.getKey();
			String value = (String) m.getValue();

			System.out.println("Key : " + key + "  value : " + value);
		}
	}

	public void add(T node) throws IOException {
		for (int i = 0; i < numberOfReplicas; i++) {
			// hashFunction.hashBytes(toByteArray(node.toString() + i)).asInt()
			circle.put(calculateHash(node.toString() + i), node);
		}
	}

	public void remove(T node) throws IOException {
		for (int i = 0; i < numberOfReplicas; i++) {
			// hashFunction.hashBytes(toByteArray(node.toString() + i)).asInt()
			circle.remove(calculateHash(node.toString() + i));
		}
	}

	public T get(Object key) throws IOException {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = calculateHash(key);
		// System.out.println("item - " + key + ", hash - " + hash);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		System.out.println(key + "," + hash + "," + circle.get(hash));
		return circle.get(hash);
	}

	private int calculateHash(Object key) throws IOException {
		return hashFunction.hashBytes(toByteArray(key)).asInt();
	}

	private byte[] toByteArray(Object obj) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} finally {
			if (oos != null) {
				oos.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return bytes;
	}
}
