package tap.views

import tap.views.GraphiqlPage.assetUrl

object Includes {
  import controllers.routes.Assets
  import scalatags.Text.all._

  //CSS
  val bootstrapCSS = link(rel:="stylesheet",
    href:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css",
    attr("integrity"):="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB",
    attr("crossorigin"):="anonymous"
  )

  val graphiqlCSS = link(rel:="stylesheet",href:=assetUrl("css/graphiql.css"))

//  val graphiqlCSS = link(rel:="stylesheet",
//    href:="https://cdnjs.cloudflare.com/ajax/libs/graphiql/0.11.11/graphiql.css"
//  )

  //Javascript
  val bootstrapJS = script(
    src:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js",
    attr("integrity"):="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T",
    attr("crossorigin"):="anonymous"
  )

  val fontAwesomeJS = script(
    src:="https://use.fontawesome.com/releases/v5.0.10/js/all.js",
    attr("integrity"):="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+",
    attr("crossorigin"):="anonymous"
  )

  val reactJS = script(
    attr("crossorigin"):="anonymous",
    src:="https://unpkg.com/react@16/umd/react.production.min.js"
  )

  val reactDomJS = script(
    attr("crossorigin"):="anonymous",
    src:="https://unpkg.com/react-dom@16/umd/react-dom.production.min.js"
  )

  val graphiqlJS = script(
    src:="https://cdnjs.cloudflare.com/ajax/libs/graphiql/0.11.11/graphiql.min.js"
  )

}
