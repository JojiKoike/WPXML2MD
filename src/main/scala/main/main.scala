package main

import com.typesafe.scalalogging.Logger

import java.nio.file.{Files, Paths}
import java.io.{File => JFile, PrintWriter => JPrintWriter}
import java.text.SimpleDateFormat
import java.util.Locale
import scala.xml.XML

object main {

  def main(args: Array[String]): Unit = {

    val logger = Logger("main")

    // Load, Parse and Deserialize Wordpress Article XML Data File
    val projectRootPath = new JFile(".").getCanonicalPath
    logger.info(s"Project Root Path : ${projectRootPath}")
    val sourceFileName = """startapp-devfrom35.WordPress.2021-06-13.xml"""
    logger.info(s"Source File Name : ${sourceFileName}")
    val wpXml = XML.loadFile(projectRootPath + """/input/""" + sourceFileName)

    val items = wpXml \\ "channel" \\ "item"
    logger.info(s"Article Items : ${items.length}")
    items.foreach(item => {
      // Get Article Directory Name
      val dirName = (item \\ "link").text.split("/").last
      logger.info(s"Article Root Dir Name : $dirName")

      // Create Article Root Directory
      val articleRootDirPath = Paths.get(projectRootPath + "/output/" + dirName)
      logger.info(s"Article Root Dir Path : $articleRootDirPath")
      if (Files.notExists(articleRootDirPath)) Files.createDirectory(articleRootDirPath)

      // Create Markdown File
      val markDownFileName = "index.md"
      val markDownFilePath = Paths.get(projectRootPath + "/output/" + dirName + "/" + markDownFileName)
      if (Files.notExists(markDownFilePath)) Files.createFile(markDownFilePath)

      // Convert and Write
      val jpw = new JPrintWriter(markDownFilePath.toAbsolutePath.toString)
      jpw.println("---")

      // Article Meta Data
      // Article Title
      val title = (item \\ "title").text
      jpw.println(s"title: $title")
      // Date
      val pubDate = (item \\ "pubDate").text
      val sdfFrom = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss Z", Locale.ENGLISH)
      val sdfTo = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
      val date = sdfTo.format(sdfFrom.parse(pubDate))
      jpw.println(s"date: $date")
      // Category
      jpw.println(s"category : ")
      // Description
      val description = (item \\ "encoded").last.text
      jpw.println(s"description: $description")
      // Tags
      val tags = (item \\ "category")
        .filter(item => item.\@("domain") == "post_tag")
        .map(item => s"'${item.text}'")
        .mkString(", ")
      jpw.println(s"tags: [$tags]")
      // Thumbnail
      jpw.println("thumbnail:")
      // Hero
      jpw.println("hero:")
      jpw.println("---")

      jpw.println()

      // Content
      val content = (item \\ "encoded").head.text
        .replace("<h2>", "# ")
        .replace("</h2>", "")
        .replace("<h3>", "## ")
        .replace("</h3>", "")
        .replace("<p>", "")
        .replace("</p>", "")
        .replace("<li>", "")
        .replace("</li>", "")
        .replace("<pre>", "")
        .replace("</pre>", "")
        .replace("<br />", "")
        .replace("[bash]", """```""")
        .replace("[/bash]", """```"""")
        .replaceAll("""<!-- wp:heading -->""", "")
        .replaceAll("""<!-- /wp:heading -->""", "")
        .replaceAll("""<!-- wp:paragraph -->""", "")
        .replaceAll("""<!-- /wp:paragraph -->""", "")
        .replaceAll("""<!-- wp:list -->""", "")
        .replaceAll("""<!-- /wp:list -->""", "")
      jpw.println(content)

      jpw.close()
    })
  }
}
