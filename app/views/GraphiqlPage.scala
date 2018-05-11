package views

import controllers.{Assets, AssetsFinder}
import scalatags.Text.all._
import scalatags.Text.tags

object GraphiqlPage extends GenericPage {

  override def page(titleStr:String) = tags.html(
    head(
      scalatags.Text.tags2.title(titleStr),
      link(rel:="stylesheet",href:="assets/stylesheets/graphiql.css") //Includes.graphiqlCSS
    ),
    body(
      div(id:="graphiql")("Loading graphiql..."),
      Includes.reactJS,
      Includes.reactDomJS,
      Includes.graphiqlJS,
      script(src:="assets/javascripts/app-graphiql.js")

    )
  )


}
