package dev.plotsky.scats.repositories

import java.util.UUID

import dev.plotsky.scats.MusicLover

trait FetchMusicLover {
  def byId(id: UUID): MusicLover
}
