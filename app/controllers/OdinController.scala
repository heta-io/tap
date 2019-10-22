package controllers

import javax.inject.Inject
import models.HealthStatus
import org.clulab.odin.ExtractorEngine
import org.clulab.processors.clu.CluProcessor
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, InjectedController}
import views.Test
import java.io._
//import views.Test.displayMentions

/**
  * Created by Carlos Agelvis on 10/10/2019.
  */

class OdinController @Inject()(assets: AssetsFinder) extends InjectedController {

  implicit val subWrites:Writes[HealthStatus] = Json.writes[HealthStatus]

  def test:Action[AnyContent] = Action {
    val processor = new CluProcessor()
    val source = scala.io.Source.fromResource("rules/master.yml")
    val rules = source.mkString
    source.close()
    val text = """She is happy because she got 7 in all her subjects.
                 |The next day Mary noticed her dog ran away, so she was crying.
                 |In a few hours, a neighbour found her dog so she was smiling a lot.
                 |Mary says to her neighbour that she trusts him.
                 |After that, she was upset because she missed the bus.
                 |""".stripMargin
    val ee = ExtractorEngine(rules)
    val doc = processor.annotate(text)
    val mentions = ee.extractFrom(doc).sortBy(m => (m.sentence, m.getClass.getSimpleName))

    Ok(Test.displayMentions(mentions, doc))
  }


}

