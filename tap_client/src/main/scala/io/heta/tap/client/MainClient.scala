package io.heta.tap.client

import io.heta.tap.shared.SharedObject
import org.scalajs.dom

object MainClient {

  //As this is a main method it should run on client load
  def main(args: Array[String]): Unit = {
    dom.document.getElementById("sjs").textContent = "This text has been inserted by a scalaJs script"
    println("This is scalaJs printing to the console. The shared message is: "+SharedObject.sharedMessage)

    //D3 Test
//
//    val graphHeight = 450
//    //The width of each bar.
//    val barWidth = 80
//    //The distance between each bar.
//    val barSeparation = 10
//    //The maximum value of the data.
//    val maxData = 50
//    //The actual horizontal distance from drawing one bar rectangle to drawing the next.
//    val horizontalBarDistance = barWidth + barSeparation
//    //The value to multiply each bar's value by to get its height.
//    val barHeightMultiplier = graphHeight / maxData;
//    //Color for start
//    val c = d3.rgb("DarkSlateBlue")
//    val rectXFun = (d: Int, i: Int) => i * horizontalBarDistance
//    val rectYFun = (d: Int) => graphHeight - d * barHeightMultiplier
//    val rectHeightFun = (d: Int) => d * barHeightMultiplier
//    val rectColorFun = (d: Int, i: Int) => c.brighter(i * 0.5).toString
//    val svg = d3.select("#d3element").append("svg").attr("width", "100%").attr("height", "450px")
//    val sel = svg.selectAll("rect").data(js.Array(8, 22, 31, 36, 48, 17, 25))
//    sel.enter()
//      .append("rect")
//      .attr("x", rectXFun)
//      .attr("y", rectYFun)
//      .attr("width", barWidth)
//      .attr("height", rectHeightFun)
//      .style("fill", rectColorFun)

  }

}
