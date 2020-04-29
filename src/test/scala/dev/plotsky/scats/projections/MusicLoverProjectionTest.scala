package dev.plotsky.scats.projections

import java.util.UUID

import dev.plotsky.scats.{Listen, Song}
import factories.EventFactory
import org.joda.time.DateTime
import org.scalatest._

class MusicLoverProjectionTest extends WordSpec with Matchers {
  "MusicLoverProjection" when {
    val id = UUID.randomUUID()

    "given an empty list" should {
      "build MusicLover with id given" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List.empty)
        assert(lover.id == id)
      }

      "build MusicLover with empty listens" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List.empty)
        assert(lover.listens.isEmpty)
      }

      "build MusicLover with empty song counts" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List.empty)
        assert(lover.songCounts.isEmpty)
      }
    }

    "given a list with initialized event" should {
      val initEvent = EventFactory.buildInitialized(id, DateTime.now())

      "build MusicLover with initializedAt set" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List(initEvent))

        assert(lover.initializedAt.get == initEvent.initializedAt)
      }

      "increment the version of MusicLover" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List(initEvent))

        assert(lover.version == 1)
      }
    }

    "given a list with a ListenAdded event" should {
      val listen = Listen(
        id = "1",
        song = Song(trackName = "song", artistName = "artist"),
        endTime = DateTime.now(),
        msPlayed = 1000
      )
      val listenEvent = EventFactory.buildListenAdded(id, listen)

      "build MusicLover with song count added" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List(listenEvent))

        assert(lover.songCounts.size == 1)
      }

      "build MusicLover with listen added to Map" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List(listenEvent))

        assert(lover.listens == Map(listen.id -> listen))
      }

      "increment the version of MusicLover" in {
        val lover = MusicLoverProjection.buildMusicLover(id, List(listenEvent))

        assert(lover.version == 1)
      }
    }

    "given a list with a ListenRemoved event" should {
      val listen = Listen(
        id = "1",
        song = Song(trackName = "song", artistName = "artist"),
        endTime = DateTime.now(),
        msPlayed = 1000
      )
      val addedEvent   = EventFactory.buildListenAdded(id, listen)
      val removedEvent = EventFactory.buildListenRemoved(id, listen)

      "given a non-zero output song count" should {
        "build MusicLover with song count subtracted" in {
          val lover = MusicLoverProjection.buildMusicLover(
            id,
            List(addedEvent, addedEvent, removedEvent)
          )

          assert(lover.songCounts(addedEvent.listen.song) == 1)
        }
      }

      "given a zero output song count" should {
        "build MusicLover with song count removed" in {
          val lover = MusicLoverProjection.buildMusicLover(
            id,
            List(addedEvent, removedEvent)
          )

          assert(lover.songCounts.isEmpty)
        }
      }

      "removes the listen" in {
        val lover = MusicLoverProjection.buildMusicLover(
          id,
          List(addedEvent, removedEvent)
        )

        assert(lover.listens.isEmpty)
      }

      "increment the version of MusicLover" in {
        val lover = MusicLoverProjection.buildMusicLover(
          id,
          List(addedEvent, removedEvent)
        )

        assert(lover.version == 2)
      }
    }
  }
}
