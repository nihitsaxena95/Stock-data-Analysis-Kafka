package com.kafkamax.stream;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import com.google.gson.Gson;
import com.kafkamax.object.StockData;

/**
 * Hello world!
 *
 */
public class KafkaStreamMax 
{
	/**
	 * @return
	 */
	private static Properties getProperties() {
		Properties p = new Properties();
		p.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-daily-max");
		p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return p;
	}
	
	/**
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static Float getMax(Float val1, Float val2) {
		if(val1>val2) {
			return val1;
		} else {
			return val2;
		}
	}
	
    /**
     * @param args
     */
    public static void main( String[] args )
    {
    	StreamsBuilder builder = new StreamsBuilder();
    	KStream<String, String> stream = builder.stream("daily-stock-data");
    	
    	Gson gson = new Gson();
    	KGroupedStream<String, Float> grpStream = stream.map((key, val) -> {
    		StockData data = gson.fromJson(val, StockData.class);
    		return KeyValue.pair(data.getTimestamp(), data.getTottrdval());
    	}).groupByKey(Grouped.with(Serdes.String(), Serdes.Float()));
    	
    	KStream<String, Float> outStream = grpStream.reduce((val1, val2)-> getMax(val1, val2)).toStream();
    	
    	outStream.to("daily-max-value", Produced.with(Serdes.String(), Serdes.Float()));
    	
    	@SuppressWarnings("resource")
		KafkaStreams out = new KafkaStreams(builder.build(), getProperties());
    	out.cleanUp();
    	out.start();
    }
}
