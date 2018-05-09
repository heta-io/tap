package tap.views

object Includes {
  import controllers.routes.Assets
  import scalatags.Text.all._

  val bootstrapCSS = link(rel:="stylesheet",
    href:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css",
    attr("integrity"):="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB",
    attr("crossorigin"):="anonymous"
  )

//  val jqueryJS = script(
//    src:="https://code.jquery.com/jquery-3.3.1.slim.min.js",
//    attr("integrity"):="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val popperJS = script(
//    src:="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js",
//    attr("integrity"):="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49",
//    attr("crossorigin"):="anonymous"
//  )

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

  val graphiqlCSS = link(rel:="stylesheet",
    href:="https://cdnjs.cloudflare.com/ajax/libs/graphiql/0.11.11/graphiql.css"
  )

  //val d3JS = script(src:="https://d3js.org/d3.v5.min.js")

    //script(src:=Assets.at("js/d3.js").url)

}
