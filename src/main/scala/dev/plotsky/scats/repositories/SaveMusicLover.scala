package dev.plotsky.scats.repositories

import java.util.UUID

import dev.plotsky.scats.events.Event

import scala.util.Try

trait SaveMusicLover {
  def save(id: UUID, events: List[Event], expectedVersion: Long): Try[Boolean]
}
