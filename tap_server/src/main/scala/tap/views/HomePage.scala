package tap.views

import controllers.routes
import scalatags.Text.all._

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  override def page(titleStr:String) = html(
    head(
      scalatags.Text.tags2.title(titleStr),
      Includes.bootstrapCSS,
      Includes.graphiqlCSS
    ),
    body(
      div(`class`:="container-fluid",
        div(`class`:="row"),
        div(`class`:="row",
          div(`class`:="col-sm-3"),
          div(`class`:="col-sm-6",
            div(`class`:="card card-default",
              div(`class`:="card-header", b(titleStr)),
              div(`class`:="card-body",
                a(href:=routes.GraphQlController.graphiql().url)("Use the generic graphiql interface.")
              )
            )
          )
        )
      ),
      Includes.bootstrapJS,
      Includes.graphiqlJS
    )
  )

}