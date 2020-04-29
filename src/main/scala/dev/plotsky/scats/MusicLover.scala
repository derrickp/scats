package dev.plotsky.scats

import java.util.UUID

import org.joda.time.DateTime

case class MusicLover(
  id: UUID,
  initializedAt: Option[DateTime],
  version: Int,
  songCounts: Map[Song, Int],
  listens: Map[String, Listen]
)
