import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}

object testing {
  def main(args: Array[String]): Unit = {
    // create a spark session
    // for Windows

  }
  System.setProperty("hadoop.home.dir", "C:\\hadoop")
  val spark = SparkSession
    .builder
    .appName("hello hive")
    .config("spark.master", "local[*]")
    .enableHiveSupport()
    .getOrCreate()
  Logger.getLogger("org").setLevel(Level.ERROR)
  println("created spark session")
  spark.sparkContext.setLogLevel("ERROR")

  spark.sql("DROP table IF EXISTS MortalityDatabase")
  spark.sql("create table IF NOT EXISTS MortalityDatabase(Date String, DateStart String, DateEnd String, Jurisdiction String, " +
    "Year INT, MONTH INT, Race String, Age String, All_Deaths INT, Natural INT, Septicimeia INT, Malignant INT, Neoplasms INT, " +
    "Diabetes INT, Alzheimer INT, Influenza_Pneumonia INT, Chronic_Respiratory_Diseases	INT, Respiratory_System INT,	Nephritis INT, "+
    "UnClassified INT, Heart_Disease INT, Cerebrovascular_Diseases INT, COVID19_Others INT,	COVID19 INT) " +
    "row format delimited fields terminated by ','") //TBLPROPERTIES('skip.header.line.count'='1')
  spark.sql("LOAD DATA LOCAL INPATH 'MortalityDatabase.csv' INTO TABLE MortalityDatabase")

  def all2021column(currentselect: String)= {
    var selection = currentselect
    spark.sql(f"SELECT $selection FROM MortalityDatabase WHERE MortalityDatabase.Year='2021'").show() //Shows all select column for year 2021
  }

  //println("What column do you wish to see?")
  //all2021column(scala.io.StdIn.readLine())


  spark.sql("DROP table IF EXISTS UserInfo")
  spark.sql("create table IF NOT EXISTS UserInfo(Username String, Password String) row format delimited fields terminated by ','")
  spark.sql("LOAD DATA LOCAL INPATH 'userinfo.csv' INTO TABLE UserInfo")
  //spark.sql("SELECT * FROM UserInfo").show()

  //spark.sql("SELECT * FROM MortalityDatabase").show()

  var extractpass = spark.sql("SELECT Password FROM UserInfo WHERE UserInfo.Username = 'person2'").first()

  var currentpass = extractpass(0)
  println(extractpass)
  println(currentpass)

  //spark.sql()
  spark.sql("SELECT * FROM UserInfo").show()

  //spark.sql("ALTER TABLE UserInfo.Password WHERE UserInfo.Username = 'person2' RENAME TO 'passwords2'").show()

  //spark.sql("SELECT * FROM UserInfo").show()

  val x = 10

  if( currentpass == "word2" ){
    println("Pass1 Selected")
  } else if( currentpass == "pass1" ){
    println("Pass2 Selected")
  }
  else{
    println("Pass3 Selected")}

  if(x == 10){
    println("Value of X is 10")
  }

  spark.close()
}