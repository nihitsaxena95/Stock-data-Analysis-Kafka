package com.prod.thread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.thread.read.ReadFileToKafka;

public class ProducerThreadMain {

	public static Properties getProperties() {
		Properties p = new Properties();
		p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		p.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		p.put(ProducerConfig.ACKS_CONFIG, "all");
		p.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		p.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
		
		return p;
	}
	
	/**
	 * @param fileName
	 * File moved to Archive after read by Kafka
	 */
	public static void moveFile(String fileName) {
		try {
			File f = new File("..\\archive");
			f.mkdir();
			Path source = Paths.get(fileName);
			Path destination = Paths.get("..\\archive\\"+fileName);
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		//Initializing kafka producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(getProperties());
		
		//Taking input from cammand line
		Scanner inputObj = new Scanner(System.in);
		
		//Starting the wizard
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Welcome to Producer Wizard");
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("\n Enter number of Threads. (3 Recommended for 3 files)");
		
		String numofThreads = inputObj.nextLine().trim();
		String[] names = {"cm07JAN2019bhav.csv", "cm08JAN2019bhav.csv", "cm09JAN2019bhav.csv"};
		
		try {
			Thread[] threads = new Thread[Integer.valueOf(numofThreads)];
			if(Integer.valueOf(numofThreads) == 3 || Integer.valueOf(numofThreads) > 3) {
				for(int i=0;i<3;i++) {
					threads[i] = new ReadFileToKafka(names[i], producer);
					threads[i].start();
					threads[i].join();
					moveFile(names[i]);
				}
			} else {
				System.out.println("Please enter 3 or more than 3 threads to read 3 files.");
			}
		} catch(Exception e) {
			System.out.println("Please enter numeric value");
		}
	}
}
