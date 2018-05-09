package tap.data

import io.nlytx.expressions.data.{Coded, Reflect, Summary}

case class TapReflectExpressions(counts:Reflect, summary:TapSummary,tags:Seq[Coded]) extends TapAnalytics
