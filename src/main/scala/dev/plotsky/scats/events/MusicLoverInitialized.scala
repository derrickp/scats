package dev.plotsky.scats.events

import java.util.UUID

import org.joda.time.DateTime

case class MusicLoverInitialized(
  musicLoverId: UUID,
  initializedAt: DateTime
) extends Event
