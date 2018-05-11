package views

import scalatags.Text.all._
import scalatags.Text.tags2
import scalatags.Text.tags

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  override def page(titleStr:String) = tags.html(
    head(
      tags2.title(titleStr),
      Includes.bootstrapCSS
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
                a(href:="")("Use the generic graphiql interface.")
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