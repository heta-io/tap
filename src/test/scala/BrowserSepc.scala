//
//
//import org.scalatestplus.play._
//import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneServerPerTest}
//
///**
//  * Created by andrew@andrewresearch.net on 25/8/17.
//  */
//
///**
//  * Runs a browser test using Fluentium against a play application on a server port.
//  */
//class BrowserSpec extends PlaySpec with GuiceOneAppPerSuite
//  //with OneBrowserPerTest
//  //with GuiceOneServerPerTest
//  //with HtmlUnitFactory
//  //with ServerProvider
//{
//
//  "Application" should {
//
//    "work from within a browser" in {
//
//      go to ("http://localhost:" + port)
//
//      pageSource must include ("Testing GraphQL")
//    }
//  }
//}