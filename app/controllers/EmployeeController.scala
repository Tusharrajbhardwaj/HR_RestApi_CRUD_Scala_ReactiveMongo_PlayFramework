package controllers

import javax.inject._
import play.api.mvc._
import repositories.EmployeeRepository
import reactivemongo.bson.BSONObjectID
import play.api.libs.json.{Json, _}

import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future}
import models.Employee
import play.api.libs.json.JsValue

import scala.collection.mutable


@Singleton
class EmployeeController @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 val employeeRepository: EmployeeRepository,
                                 val controllerComponents: ControllerComponents)
  extends BaseController {



  def findAll():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    employeeRepository.findAll().map {
      employee => Ok(Json.toJson(employee))
    }
  }

  def findOne(id:String):Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => employeeRepository.findOne(objectId).map {
        employee => Ok("Employee Details : "+Json.toJson(employee))
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the employee id"))
    }
  }

  def create():Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[Employee].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      employee => employeeRepository.create(employee).map {
          _ => Created("New Employee inserted" + Json.toJson(employee))
        }
    )
  }}

  def update(id: String):Action[JsValue]  = Action.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[Employee].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      employee =>{
        val objectIdTryResult = BSONObjectID.parse(id)
        objectIdTryResult match {
          case Success(objectId) => employeeRepository.update(objectId, employee).map {
            _ => Ok("Successfully updated")
          }
          case Failure(_) => Future.successful(BadRequest("Cannot parse the employee id"))
        }
      }
    )
  }}

  def delete(id: String):Action[AnyContent]  = Action.async { implicit request => {
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => employeeRepository.delete(objectId).map {
        _ => Ok("Employee removed from the db")
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the employee id"))
    }
  }}
}