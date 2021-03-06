import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Setup { // Object file name.
  def main(args: Array[String]): Unit = {             //Need hadoop, scala2.13, hive and jdk8 installed.
    // create a spark session
    // for Windows
    //val data = request.get("https://data.cdc.gov/api/views/r5pw-bk5t/rows.json?accessType=DOWNLOAD")
    //val text = data.text()
    //val json = ujson.read(text)
    //os.write(os.pwd/"tmp.json",json)

    val spark = SparkSession
      .builder
      .appName("hello hive")
      .config("spark.master", "local[*]")
      .enableHiveSupport()
      .getOrCreate()
    Logger.getLogger("org").setLevel(Level.ERROR)
    println("Created spark session.")
    /*Anything above this has been mostly unchanged (generalized)*/

    val df1 = spark.read.format("csv").option("sep",",").load("hdfs://localhost:9000/user/hive/warehouse/MortalityDatabase.csv") // File location in hdfs
    df1.createOrReplaceTempView("mortalitydata")   //Temp table name given     //Type didn't seem to matter, just loaded straight .txt that looked like tables.

    spark.sql("SELECT * FROM mortalitydata;").show()                     // Show() displays current tables.
    /*-------Comment Start
    spark.sql("SELECT * FROM mortalitydata WHERE _c4=2020;").show() //Shows all tables that have 2020
    spark.sql("SELECT * FROM mortalitydata WHERE _c7 LIKE '%55%';").show() // All rows that show 55 years in column c7_
    spark.sql("SELECT * FROM mortalitydata WHERE _c6 LIKE '%Black%'  AND _c5=2;").show() //Shows all black values in month 2.
    spark.sql("SELECT * FROM mortalitydata WHERE _c6 LIKE '%Black%'  AND _c5=2 ORDER BY _c10 + 0 DESC LIMIT 10;").show() //Same but limits to 10 and orders by total septicemia
                                                           //Can also convert to integer with ORDER_BY cast(_c10 as int) ASC/DESC
    spark.sql("SELECT _c6, SUM(_c8) AS Msum FROM mortalitydata WHERE _c4=2020 GROUP BY _c6;").show() //Sums all deaths for 2020 and group by race.
                        //_c6 = Race, _c8 = Total Deaths, _c4 = Year
    //https://www.eversql.com/sql-syntax-check-validator/
    Comment End-------*/

    var Mdf1 = spark.sql("SELECT * FROM mortalitydata WHERE _c6 LIKE '%Black%';")// Creates a sub-table to be viewed.
    Mdf1.distinct().count()                                                                     // Can't have .show() if you want to save as var
    Mdf1.createOrReplaceTempView(viewName="Mmortalitydata")
    spark.sql("SELECT _c7, SUM(_c8) AS Msum FROM Mmortalitydata GROUP BY _c7 ORDER BY CAST(Msum as int);").show() //Shows sum of all black deaths
    //Orders from least to most and groups by ages
    //Print results with println() to see if you can use a specific row.

    spark.sql("SELECT _c1 FROM mortalitydata WHERE _c4 = 2020 LIMIT 1;").show  //Prints column 1 (date) where c4 is 2021
    spark.sql("SELECT * FROM mortalitydata LIMIT 1;").show() //Shows table names


  }
}













