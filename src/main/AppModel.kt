package main

data class AppModel(
  val numberOfServers: Int,
  val mutationRate: Double,
  val crossoverRate: Double,
  val initialServers: Array<Server>
) {


  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AppModel

    if (!initialServers.contentEquals(other.initialServers)) return false

    return true
  }

  override fun hashCode(): Int {
    return initialServers.contentHashCode()
  }
}