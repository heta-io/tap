package tap.views

import controllers.routes

import scalatags.Text.all._

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  override def page(titleStr:String) = html(
    head(
      scalatags.Text.tags2.title(titleStr) //,
    ),
    body(
      div(
        h1(id:="title", titleStr),
        p(a(href:=routes.GraphQlController.graphiql().url)("Use the generic graphiql interface.")) //,
//        p(a(href:=routes.AWAGraphQlController.graphiql().url)("Use the AWA graphiql interface.")),
      )
    )
  )
}
