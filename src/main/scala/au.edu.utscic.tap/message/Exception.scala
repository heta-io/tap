package au.edu.utscic.tap.message

/**
  * Created by andrew@andrewresearch.net on 1/3/17.
  */
object Exception {

  case class UnknownAnalysisType(message: String) extends Exception(message)
}
