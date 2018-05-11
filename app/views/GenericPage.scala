package views

import controllers.{Assets, AssetsFinder}
import play.twirl.api.Html
import scalatags.Text.all._
import scalatags.Text.tags2
import scalatags.Text.tags

trait GenericPage {

  def render(title:String) = Html("<!DOCTYPE html>" +page(title).render)

  def page(titleStr:String) = tags.html(head(tags2.title(titleStr)))

  //def assetUrl(fileLoc:String):String = Assets.at(fileLoc).toString()
}
