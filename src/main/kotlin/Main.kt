package com.habr.codeconverter

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.InputStream

val HTML_FLAVOR = DataFlavor("text/html", "Rich Formatted Text")

val pre = Regex("<pre .*?>(.*?)</pre>")
val span = Regex("<span (.*?)>(.*?)</span>")
val color = Regex("[\";]color:#(.*?)[\";]")
val styleItalic = Regex("(font-style:italic)")
val styleBold = Regex("(font-weight:bold)")
val emptyFont = Regex("<font[^>]*?></font>")
val uselessFont = Regex("<font[^>]*?>((&#32;)+)</font>")

val clipboard = Toolkit.getDefaultToolkit().systemClipboard!!
val clipboardHtml
  get() =
    try {
      clipboard.getData(HTML_FLAVOR)
    } catch (e: UnsupportedFlavorException) {
      null
    }
      ?.let { it as? InputStream }
      ?.reader()
      ?.readText()

fun main(vararg args: String) =
  if (args.isNotEmpty()) {
    println("Running as daemon")
    clipboard.addFlavorListener {
      convertClipboard()
    }.also {
      Thread.currentThread().suspend()
    }
  } else
    convertClipboard()

private fun convertClipboard() {
  clipboardHtml
    ?.also {
      // debug only
      println("Source clipboard data as html:\n$it")
    }
    ?.convertCode()
    ?.putIntoClipboard()
}

private fun String.convertCode() =
  substring(pre)
    ?.replace(span) { span -> span.groups[2]!!.value.styledWith(span.groups[1]!!.value) }
    ?.popupBr()
    ?.replaceTags()
    ?.wrapWithCode()

private fun String.putIntoClipboard() =
  StringSelection(this)
    .let { clipboard.setContents(it, it) }

private fun String.wrapWithCode() =
  "<code>$this</code>"

private fun String.popupBr() =
  // popup <br> from any style tags
  replace("<br></b>", "</b><br>")
    .replace("<br></i>", "</i><br>")
    .replace("<br></font>", "</font><br>")
    // useless style removing (minification)
    .replace("<b></b>", "")
    .replace("<i></i>", "")
    .replace(emptyFont, "")
    .replace(uselessFont, "$1")

private fun String.replaceTags() =
    replace("<br>", "\n")
    .replace("&#32;", "&nbsp;")

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


