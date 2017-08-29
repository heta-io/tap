package au.edu.utscic.tap

/**
  * Created by andrew@andrewresearch.net on 14/11/16.
  */

import org.scalatest._

abstract class UnitSpec extends FlatSpec with Matchers with
  OptionValues with Inside with Inspectors
