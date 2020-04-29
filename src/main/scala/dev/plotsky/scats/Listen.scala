package dev.plotsky.scats

import org.joda.time.DateTime

case class Listen(id: String, song: Song, endTime: DateTime, msPlayed: Int)
