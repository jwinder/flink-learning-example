## flink-learning-example

Minimal example of using [Flink](https://flink.apache.org/).

## instructions

- In this repository, run `./bin/produce-test-events`, a server on port `9000` which produces an example event per second for each client connection.
- To see an example of the events produced, run `nc -l 9000`.
- To run the example flink job, use `sbt run`.

Note: Each client receives their own stream of events produced, which starts & stops when the client connects & disconnects. Therefore, the flink job does not consume any previous events when restarted.
