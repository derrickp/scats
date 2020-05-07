package dev.plotsky.scats.projections

import java.util.UUID

import dev.plotsky.scats.{Listen, MusicLover, Song}
import dev.plotsky.scats.events.{
  Event,
  ListenAdded,
  ListenRemoved,
  MusicLoverInitialized
}

object MusicLoverProjection {
  def buildMusicLover(id: UUID, events: List[Event]): MusicLover = {
    val initial = emptyLover(id)
    events.foldLeft(initial)((collector, event) => applyEvent(collector, event))
  }

  def applyEvent(lover: MusicLover, event: Event): MusicLover =
    event match {
      case init: MusicLoverInitialized => applyEvent(lover, init)
      case listenAdded: ListenAdded    => applyEvent(lover, listenAdded)
      case removed: ListenRemoved      => applyEvent(lover, removed)
      case _                           => applyUnknown(lover, event)
    }

  private def emptyLover(id: UUID): MusicLover = {
    MusicLover(
      id = id,
      initializedAt = None,
      version = 0,
      songCounts = Map.empty,
      listens = Map.empty
    )
  }

  private def applyEvent(
    lover: MusicLover,
    event: ListenRemoved
  ): MusicLover = {
    lover.copy(
      songCounts = subtractSongCount(lover, event.listen.song),
      listens = removeListen(lover, event.listen),
      version = increment(lover.version)
    )
  }

  private def removeListen(
    lover: MusicLover,
    listen: Listen
  ): Map[String, Listen] = {
    lover.listens - listen.id
  }

  private def applyEvent(
    lover: MusicLover,
    event: MusicLoverInitialized
  ): MusicLover = {
    lover.copy(
      initializedAt = Some(event.initializedAt),
      version = increment(lover.version)
    )
  }

  private def applyEvent(lover: MusicLover, event: ListenAdded): MusicLover = {
    lover.copy(
      songCounts = addSongCount(lover, event.listen.song),
      listens = addListen(lover, event.listen),
      version = increment(lover.version)
    )
  }

  private def applyUnknown(lover: MusicLover, event: Event): MusicLover = {
    lover.copy(
      version = increment(lover.version)
    )
  }

  private def addListen(
    lover: MusicLover,
    listen: Listen
  ): Map[String, Listen] = {
    lover.listens + (listen.id -> listen)
  }

  private def increment(version: Int): Int = {
    version + 1
  }

  private def subtractSongCount(
    lover: MusicLover,
    song: Song
  ): Map[Song, Int] = {
    val newCount = lover.songCounts(song) - 1
    if (newCount == 0) {
      removeSong(lover, song)
    } else {
      replaceSong(lover, song, newCount)
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

  private def replaceSong(
    lover: MusicLover,
    song: Song,
    count: Int
  ): Map[Song, Int] = {
    lover.songCounts + (song -> count)
  }

  private def removeSong(lover: MusicLover, song: Song): Map[Song, Int] = {
    lover.songCounts - song
  }
}
