package tap.views

import scalatags.Text.all._

object GraphiqlPage extends GenericPage {

  override def page(titleStr:String) = html(
    head(
      scalatags.Text.tags2.title(titleStr),
      Includes.graphiqlCSS
    ),
    body(
      div(id:="graphiql")("Loading graphiql..."),
      Includes.reactJS,
      Includes.reactDomJS,
      Includes.graphiqlJS,
      script(src:=assetUrl("js/app-graphiql.js"))

    )
  )


}
