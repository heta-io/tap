package au.edu.utscic.tap.util

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object StringUtil {
  def shorten(text:String,num:Int=30) = text.replace("\n"," ").take(num).concat("\u2026")
}
