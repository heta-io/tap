/**
  * Created by andrew@andrewresearch.net on 30/8/17.
  */

import au.edu.utscic.tap.nlp.factorie.{Annotator}
import com.google.inject.AbstractModule
import play.api.Logger.logger

class Module extends AbstractModule {
  def configure() = {
    val result = Annotator.init
    if (result==2) logger.info("Factorie Initialised")
    else logger.error(s"There was a problem initialising Factorie - $result")
  }
}
