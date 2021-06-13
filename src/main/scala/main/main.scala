package main

import com.typesafe.scalalogging.Logger

import java.nio.file.{Paths, Files}
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.io.{File => JFile}
import scala.xml.XML

object main {

  def main(args: Array[String]): Unit = {

    val logger = Logger("main")

    // Load, Parse and Deserialize Wordpress Article XML Data File
    val projectRootPath = new JFile(".").getCanonicalPath
    logger.info(s"Project Root Path : ${projectRootPath}")
    val sourceFileName = """startapp-devfrom35.WordPress.2021-06-13.xml"""
    logger.info(s"Source File Name : ${sourceFileName}")
    val wpXml = XML.loadFile(projectRootPath + """/src/main/res/""" + sourceFileName)

    // Step 1 : Get Article Item List
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


    })

  }


}
