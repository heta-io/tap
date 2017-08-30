/**
  * Created by andrew@andrewresearch.net on 30/8/17.
  */

import au.edu.utscic.tap.nlp.factorie.{Annotation}
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  def configure() = {
    Annotation.tokenise("test")
  }
}
