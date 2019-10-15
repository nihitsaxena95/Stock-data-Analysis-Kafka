package com.thread.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.prod.object.StockData;

public class ReadFileToKafka extends Thread {
	
	String fileName;
	KafkaProducer<String, String> producer;
	final String topic = "daily-stock-data-s"; //Name of the Topic
	public static final Logger logger = LoggerFactory.getLogger(ReadFileToKafka.class);
	
	/**
	 * @param fileName
	 * @param producer
	 */
	public ReadFileToKafka(String fileName, KafkaProducer<String, String> producer) {
		this.fileName = fileName;
		this.producer = producer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		List<String> jsonData = readFileFromCSV();
		if(jsonData != null) {
			for(String data :jsonData) {
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, data);
				producer.send(record, new Callback() {
					@Override
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if(e == null) {
							logger.info("Record recieved \n Topic : "+metadata.topic()+"\n Partition : "+metadata.partition()+" \n Offset : "+metadata.offset()+";");
						} else {
							logger.error(e.toString());
						}
					}
				});
			}
		}
	}

	/**
	 * Reading file from csv and converting to json
	 */
	@SuppressWarnings("resource")
	private List<String> readFileFromCSV() {
		BufferedReader allData = null;
		List<String> allLines = null;
		Gson gson = new Gson();
		try {
			allData = new BufferedReader(new FileReader(fileName));
			allLines = allData.lines().skip(1).map(line -> {
				String[] field = line.split(",");
				return gson.toJson(new StockData(field[0],field[1],field[2],field[3],field[4],field[5],field[6],field[7],field[8],field[9],field[10],field[11],field[12]));
			}).collect(Collectors.toList());
			allData.close();
			return allLines;
		} catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
