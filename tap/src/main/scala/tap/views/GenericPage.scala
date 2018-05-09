package tap.views

import scalatags.Text.all._
import play.twirl.api.Html

trait GenericPage {

  def render(title:String) = Html("<!DOCTYPE html>" +page(title).render)

  def page(titleStr:String) = html(head(scalatags.Text.tags2.title(titleStr)))

  def assetUrl(fileLoc:String) = controllers.routes.Assets.at(fileLoc).url
}
