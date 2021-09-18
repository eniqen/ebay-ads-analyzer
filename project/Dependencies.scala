object Dependencies {

  import sbt._

  sealed trait Module {
    def groupId: String
    def live: List[ModuleID]
    def withVersion(version: String): String => ModuleID = groupId %% _ % version
    def withVersionSimple(version: String): String => ModuleID = groupId % _ % version
    def withTest(module: ModuleID): ModuleID = module % Test

  }

  object tests extends Module {
    override def groupId: String = "org.scalatest"

    override def live: List[sbt.ModuleID] = List("scalatest").map(withVersion("3.1.0")).map(withTest)
  }

  val live: List[ModuleID] = List(tests).flatMap(_.live)
}