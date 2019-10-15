# Kafka Stock Price UseCase


## Problem Statement 1 : Reading Data from File and Inserting into Kafka

1) Create Topic where data is inserted. As more data needs to be inserted inside the topic and number of consumers will also be huge. Hence more partitions are needed for best performance. As this is a single node single broker cluster hence replication factor is 1.

` kafka-topics --zookeeper 127.0.0.1:2181 --create --topic daily-stock-data --partitions 12 --replication-factor 1 `


## Problem Statement 2 : Streaming Data to get the Maximum value

1) Create Output Topic where data needs to be inserted. Topic created is compacted, the idea behind log compaction is selectively remove records where we have most recent update with the same primary key. Log Compaction strategy ensures that Kafka will always retain at least the last known value for each message key within the log for a single topic partition. Hence Only one and the Maximum value for each day will be inserted. Use Following Command to create topic.

` kafka-topics –-zookeeper 127.0.0.1:2181 — create –topic daily-max-value –partitions 3 –replication-factor 1 — config “cleanup.policy=compact” — config “delete.retention.ms=100” — config “segment.ms=100” — config “min.cleanable.dirty.ratio=0.01” `



	