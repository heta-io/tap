package tap.views

import scalatags.Text.all._

object GraphiqlPage extends GenericPage {

  override def page(titleStr:String) = html(
    head(
      scalatags.Text.tags2.title(titleStr),
      link(href:=assetUrl("css/graphiql.css"),rel:="stylesheet"),
      script(src:=assetUrl("js/react.min.js")),
      script(src:=assetUrl("js/react-dom.min.js")),
      script(src:=assetUrl("js/graphiql.min.js"))
    ),
    body(
      div(id:="graphiql")("Loading graphiql..."),
      script(src:=assetUrl("js/app-graphiql.js"))
    )
  )


}
