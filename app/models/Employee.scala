package models

import org.joda.time.DateTime
import play.api.libs.json.{OFormat,Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.compat._

case class Employee(
                  _id:Option[BSONObjectID],
                  _creationDate: Option[DateTime],
                  Employee_id : String,
                  Name: String,
                  Team:String
                )
object Employee{
  implicit val fmt : OFormat[Employee] = Json.format[Employee]
  implicit object EmployeeReader extends BSONDocumentReader[Employee] {
    def read(doc: BSONDocument): Employee = {
      Employee(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONDateTime]("_creationDate").map(dt => new DateTime(dt.value)),
        doc.getAs[String]("Employee_id").get,
        doc.getAs[String]("Name").get,
        doc.getAs[String]("Team").get)
    }
  }

  implicit object EmployeeBSONWriter extends BSONDocumentWriter[Employee] {
    def write(employee: Employee): BSONDocument = {
      BSONDocument(
        "_id" -> employee._id,
        "_creationDate" -> employee._creationDate.map(date => BSONDateTime(date.getMillis)),
        "Employee_id" -> employee.Employee_id,
        "Name" -> employee.Name,
        "Team" -> employee.Team
      )
    }
  }
}

