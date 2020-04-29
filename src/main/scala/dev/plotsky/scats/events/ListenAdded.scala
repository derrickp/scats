package dev.plotsky.scats.events

import java.util.UUID

import dev.plotsky.scats.Listen

case class ListenAdded(
  musicLoverId: UUID,
  listen: Listen
) extends Event
