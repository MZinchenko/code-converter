package com.habr.codeconverter

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.io.InputStream

val HTML_FLAVOR = DataFlavor("text/html", "Rich Formatted Text")

val pre = Regex("<pre .*?>(.*?)</pre>")
val span = Regex("<span (.*?)>(.*?)</span>")
val color = Regex("[\";]color:#(.*?)[\";]")
val styleItalic = Regex("(font-style:italic)")
val styleBold = Regex("(font-weight:bold)")

fun main() {
  val cb = Toolkit.getDefaultToolkit().systemClipboard
  val source = (cb.getData(HTML_FLAVOR) as InputStream).reader().readText()
  println(source)
  StringSelection(
    "<code>" +
      source.substring(pre)!!
        .replace(span) {
          it.groups[2]!!.value.styledWith(it.groups[1]!!.value)
        }
        .replace("<br>", "\n")
        .replace("&#32;", "&nbsp;")
      + "</code>"
  ).let { cb.setContents(it, it) }
}

private fun String.styledWith(style: String) =
  styledWith(
    style.substring(color),
    style.substring(styleItalic) != null,
    style.substring(styleBold) != null
  )

private fun String.styledWith(color: String?, italic: Boolean, bold: Boolean) =
  (color?.let { "<font color=\"$it\">" } ?: "") +
    (if (italic) "<i>" else "") +
    (if (bold) "<b>" else "") +
    this +
    (if (bold) "</b>" else "") +
    (if (italic) "</i>" else "") +
    (color?.let { "</font>" } ?: "")


private fun String.substring(pattern: Regex) =
  pattern.find(this)?.let { it.groups[1]?.value }


