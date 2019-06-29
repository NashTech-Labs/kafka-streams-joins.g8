A [Giter8][g8] template for showcasing join transformations using Kafka Streams API

Examples for Kafka Stream Joins
---

### Steps to install Zookeeper and Apache Kafka:

Step 1: Download Kafka

Download Kafka from [here](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.2.0/kafka-2.2.0-src.tgz)

Step 2: Extract downloaded file

```bash
tar -xzvf kafka-2.2.0.tgz
cd kafka-2.2.0
```
### Steps to start Zookeeper and Kafka server :

Start Zookeeper:

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka server:

```bash
bin/kafka-server-start.sh config/server.properties
```


---
### Clone Project

```bash
git clone git@github.com:knoldus/kafka-streams-joins.g8.git
cd kafka-streams-joins.g8
sbt clean compile
```
---

Execute the following command,

```bash
sbt "runMain com.knoldus.demo.SampleDataGenerator"
```
This starts producing random messages in the input topics Customer and Address.

---
### Join a Global KTable with an a real-time KStream

Step 1:
Execute the following command,

```bash
sbt "runMain com.knoldus.demo.KStreamGlobalKTableJoinDemo"
```

This begins generting output events based on the inner join of records between the Global KTable and the KStream. The
produced events are the ones with matching keys.


### Join a KStream with another KStream using Join windows

Step 1:
Execute the following command,

```bash
sbt "runMain com.knoldus.demo.KStreamKStreamJoinDemo"
```

This begins generating output events based on the inner join of records between the incoming KStreams within the
specified join window. The produced events are the ones with matching keys.

---
For any issue please raise a ticket @ [Github Issue](https://github.com/knoldus/kafka-streams-joins.g8/issues)

Template license
----------------
Written in 2019 by Himani Arora

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/