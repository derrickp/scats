package dev.plotsky.scats.commands

import java.util.UUID

import dev.plotsky.scats.spotify.StreamingHistoryListen

case class AddSpotifyListen(
  loverId: UUID,
  spotifyListen: StreamingHistoryListen
) extends Command
