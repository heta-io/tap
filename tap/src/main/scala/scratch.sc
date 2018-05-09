import play.api.libs.json.{JsDefined, JsObject, JsString, Json}

import scala.util.Try

val as = List.range(1,10)
val bs = List.range(1,10)
val cs = List.range(1,10)

for {
  ((a,b),c) <- as zip bs zip cs
} yield (a,b,c)

val parameters = Option("""{
    |"cleanType": "visible"
    |}""".stripMargin)

val jsonResult = Try(Json.parse(parameters.getOrElse("{}")))

val jparam = jsonResult.getOrElse(JsObject(Seq()))

  val result = (jparam \ "cleanType").getOrElse(JsString("")) match {
    case JsString("visible") => "VIS"
    case JsString("ascii") => "ASCII"
    case _ => "No Value"
  }

println(s"RESULT: $result")



