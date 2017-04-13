package example

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.TimeCharacteristic

// things to play with:
// windows
// triggers
// count of how many events for a user were a pageview followed by a login
// watermarks (out of order events)
// event time vs. processing time for job

object TestFlinkJob extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)

  // when using EventTime, need to use `assignTimestampsAndWatermarks` so that flink knows how to extract the event time from the record
  // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

  val (pageviews, logins) = {
    val input: DataStream[String] = env.socketTextStream("localhost", 9000, '\n')

    // pretend different types come from different sources
    val pageviewInput = input.filter(Event.isPageview(_))
    val loginInput = input.filter(Event.isUserLogin(_))

    val pageviews: DataStream[Pageview] = pageviewInput.map(Event.parsePageview(_))
    val logins: DataStream[UserLogin] = loginInput.map(Event.parseUserLogin(_))

    (pageviews, logins)
  }

  val events: ConnectedStreams[Pageview, UserLogin]  = pageviews.connect(logins)
  val aggregations: DataStream[Aggregation] = events.map(Aggregation(_), Aggregation(_))

  val userAggregationsEveryFiveSeconds = {
    aggregations
      .keyBy(_.userId)
      .timeWindow(Time.seconds(5))
      .reduce(_ ++ _)
  }

  userAggregationsEveryFiveSeconds.print()

  env.execute()
}
