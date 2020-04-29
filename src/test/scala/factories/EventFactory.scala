package factories

import java.util.UUID

import dev.plotsky.scats.Listen
import dev.plotsky.scats.events.{
  ListenAdded,
  ListenRemoved,
  MusicLoverInitialized
}
import org.joda.time.DateTime

object EventFactory {
  def buildInitialized(id: UUID, init: DateTime): MusicLoverInitialized = {
    MusicLoverInitialized(
      musicLoverId = id,
      initializedAt = init
    )
  }

  def buildListenAdded(id: UUID, listen: Listen): ListenAdded = {
    ListenAdded(
      musicLoverId = id,
      listen = listen
    )
  }

  def buildListenRemoved(id: UUID, listen: Listen): ListenRemoved = {
    ListenRemoved(
      musicLoverId = id,
      listen = listen
    )
  }
}
