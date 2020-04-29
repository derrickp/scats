package dev.plotsky.scats.projections

import java.util.UUID

import dev.plotsky.scats.{MusicLover, Song}
import dev.plotsky.scats.events.{
  Event,
  ListenAdded,
  ListenRemoved,
  MusicLoverInitialized
}

object MusicLoverProjection {
  def buildMusicLover(id: UUID, events: List[Event]): MusicLover = {
    val initial = MusicLover(
      id = id,
      initializedAt = None,
      version = 0,
      songCounts = Map.empty,
      listens = Map.empty
    )
    events.foldLeft(initial)((collector, event) => applyEvent(collector, event))
  }

  def applyEvent(lover: MusicLover, event: Event): MusicLover =
    event match {
      case init: MusicLoverInitialized => applyEvent(lover, init)
      case listenAdded: ListenAdded    => applyEvent(lover, listenAdded)
      case removed: ListenRemoved      => applyEvent(lover, removed)
      case _                           => lover
    }

  private def applyEvent(
    lover: MusicLover,
    event: ListenRemoved
  ): MusicLover = {
    lover.copy(
      songCounts = subtractSongCount(lover, event.listen.song),
      listens = lover.listens - event.listen.id,
      version = lover.version + 1
    )
  }

  private def applyEvent(
    lover: MusicLover,
    event: MusicLoverInitialized
  ): MusicLover = {
    lover.copy(
      initializedAt = Some(event.initializedAt),
      version = lover.version + 1
    )
  }

  private def applyEvent(lover: MusicLover, event: ListenAdded): MusicLover = {
    lover.copy(
      songCounts = addSongCount(lover, event.listen.song),
      listens = lover.listens + (event.listen.id -> event.listen),
      version = lover.version + 1
    )
  }

  private def subtractSongCount(
    lover: MusicLover,
    song: Song
  ): Map[Song, Int] = {
    val newCount = lover.songCounts(song) - 1
    if (newCount == 0) {
      lover.songCounts - song
    } else {
      lover.songCounts + (song -> newCount)
    }
  }

  private def addSongCount(lover: MusicLover, song: Song): Map[Song, Int] = {
    if (lover.songCounts.contains(song)) {
      val newCount = lover.songCounts(song) + 1
      lover.songCounts + (song -> newCount)
    } else {
      lover.songCounts + (song -> 1)
    }
  }
}
