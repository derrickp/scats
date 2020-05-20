package dev.plotsky.scats.repositories

trait MusicLoverRepository {
  def musicLoverFetch(): FetchMusicLover
  def musicLoverSave(): SaveMusicLover
}
