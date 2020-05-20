package dev.plotsky.scats.repositories

import java.util.UUID

import dev.plotsky.scats.events.Event
import dev.plotsky.scats.projections.MusicLoverProjection

import scala.collection.mutable
import scala.util.{Success, Failure}

object InMemoryMusicLoverRepository {
  def build(): MusicLoverRepository = {
    val initial: mutable.HashMap[UUID, List[Event]] = mutable.HashMap.empty

    new MusicLoverRepository {
      def musicLoverFetch(): FetchMusicLover = { (id: UUID) =>
        {
          MusicLoverProjection.buildMusicLover(id, getEvents(id))
        }
      }

      def musicLoverSave(): SaveMusicLover = {
        (id: UUID, events: List[Event], expectedVersion: Long) =>
          {
            val list = getEvents(id)
            if (list.size != expectedVersion) {
              Failure(IncorrectVersion())
            } else {
              initial.put(id, list ++ events)
              Success(true)
            }
          }
      }

      private def getEvents(id: UUID): List[Event] = {
        initial.getOrElseUpdate(id, List.empty)
      }
    }
  }
}
