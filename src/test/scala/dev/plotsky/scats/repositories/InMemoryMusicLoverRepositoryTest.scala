package dev.plotsky.scats.repositories

import java.util.UUID
import dev.plotsky.scats.{Listen, Song}
import factories.EventFactory
import org.joda.time.DateTime
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class InMemoryMusicLoverRepositoryTest extends AnyWordSpec with Matchers {
  val id = UUID.randomUUID()

  "fetchMusicLover()" when {
    "called when MusicLover has not been saved" should {
      "return an empty MusicLover" in {
        val lover =
          InMemoryMusicLoverRepository.build().musicLoverFetch().byId(id)
        assert(lover.version == 0)
      }
    }

    "called when MusicLover has been saved" should {
      val initEvent = EventFactory.buildInitialized(id, DateTime.now())
      val events    = List(initEvent)

      "return MusicLover with events applied" in {
        val repository = InMemoryMusicLoverRepository.build()

        repository
          .musicLoverSave()
          .save(id, events, 0)

        val lover = repository.musicLoverFetch().byId(id)
        assert(lover.version == 1)
        assert(lover.initializedAt.get == initEvent.initializedAt)
      }
    }
  }

  "saveMusicLover()" when {
    "called with an existing MusicLover" should {
      val repository = InMemoryMusicLoverRepository.build()

      val initEvent = EventFactory.buildInitialized(id, DateTime.now())
      val events    = List(initEvent)

      repository.musicLoverSave().save(id, events, 0)
      val listen = Listen(
        id = "1",
        song = Song(trackName = "song", artistName = "artist"),
        endTime = DateTime.now(),
        msPlayed = 1000
      )
      val listenEvent = EventFactory.buildListenAdded(id, listen)

      "save the new events and the old events" in {
        repository
          .musicLoverSave()
          .save(id, List(listenEvent), 1)

        val lover = repository.musicLoverFetch().byId(id)

        assert(lover.version == 2)
      }
    }
  }
}
