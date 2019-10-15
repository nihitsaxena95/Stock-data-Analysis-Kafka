# Kafka Stock Price UseCase

Based on the use-case specified, I have created two projects - `kafkaprod` and `kafkamax` for kafka producers to insert records and kafka stream to get maximum value respectively.

## Problem Statement 1 : Reading Data from File and Inserting into Kafka

1) Create Topic where data is inserted. As more data needs to be inserted inside the topic and number of consumers will also be huge. Hence more partitions are needed for best performance. As this is a single node single broker cluster hence replication factor is 1.

` kafka-topics --zookeeper 127.0.0.1:2181 --create --topic daily-stock-data --partitions 12 --replication-factor 1 `

2) Created java maven project with kafka-client as dependency.

3) For the sake of simplicity I have developed a wizard wherein people can enter the number for threads as command line input. As there are three files to read minimum 3 threads should be there, one thread to read each file.

4) Each thread is reading the file from csv and converting it to java object called StockData, wherein property is defined and stored for each record.

5) These java object StockData is then converted into json string format using Gson library and stored as a list of string.

6) A new Producer record with String as key and value is created and StockData json string is stored as value of the record.

7) This producer record is inserted into kafka producer which is declared only once and passed around as argument because the producer object is thread safe.

8) Producer is defined with following properties :
	-	BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092" -> Local kafka server host and port
	-	KEY_SERIALIZER_CLASS_CONFIG, StringSerializer -> Serialize key with as string for kafka topic
	-	VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer -> Serialize value with as string for kafka topic
	-	ENABLE_IDEMPOTENCE_CONFIG, "true" -> Create idempotent producer to ensures exactly once message delivery per partition
	-	ACKS_CONFIG, "all" -> To receive acknowledgement from both leader and replica
	-	RETRIES_CONFIG, Integer.MAX_VALUE -> To have maximum retries while sending a record
	-	MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5" ->  to have maximum but safe throughput

9) All the records are then inserted into kafka topic `daily-stock-data` in json string format.

10) Once all the records are inserted and thread finished its execution then file is copied to archive directory.


## Problem Statement 2 : Streaming Data to get the Maximum value

1) Create Output Topic where data needs to be inserted. Topic created is compacted, the idea behind log compaction is selectively remove records where we have most recent update with the same primary key. Log Compaction strategy ensures that Kafka will always retain at least the last known value for each message key within the log for a single topic partition. Hence Only one and the Maximum value for each day will be inserted. Use Following Command to create topic.

` kafka-topics –-zookeeper 127.0.0.1:2181 — create –topic daily-max-value –partitions 3 –replication-factor 1 — config “cleanup.policy=compact” — config “delete.retention.ms=100” — config “segment.ms=100” — config “min.cleanable.dirty.ratio=0.01” `

2) Created java maven project with kafka-stream as dependency.

3) New kafka stream is created with following configuration :
	-	APPLICATION_ID_CONFIG, "stream-daily-max" -> Unique application id is assign to find daily maximum
	-	BOOTSTRAP_SERVERS_CONFIG, "localhost:9092" -> Local kafka server host and port
	-	DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String() -> Key deserialization type as String
	-	DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String() -> Value deserialization type as String
	-	AUTO_OFFSET_RESET_CONFIG, "earliest" -> To read record from starting

4) New Stream Builder is created and configured to read data from topic `daily-stock-data`.

5) Data read is then parsed from json string to java object StockData.

6) Stream data is then mapped into new key value pair with key as date and value as Tottrdval into String and Float respectively.

7) Above mapped data is then grouped by key and then reduced to maximum value also with serialization as String and Float Serdes.

8) This reduced data with date as key String and value as Float maximum value is inserted into kafka topic with respective serialization.


Code is properly commented and followed directory structure as per ideal conventions. 

Above configurations of producer and stream application is highly scalable and have potential of sustaining over real time streaming.

Thankyou!