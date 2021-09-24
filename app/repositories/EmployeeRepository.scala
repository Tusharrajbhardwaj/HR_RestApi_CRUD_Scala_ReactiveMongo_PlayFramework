package repositories

import models.Employee

import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID, _}
import org.joda.time.DateTime
import reactivemongo.api.bson.compat.toDocumentWriter
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.bson.compat._


@Singleton
class EmployeeRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 val reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(_.collection("Employee2"))

  def findAll(limit: Int = 100 ): Future[Seq[Employee]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Employee])
        .cursor[Employee](ReadPreference.primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Employee]]())
    )
  }

  def findOne(id: BSONObjectID): Future[Option[Employee]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[Employee]).one[Employee])
  }

  def create(employee: Employee): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(employee.copy(_creationDate = Some(new DateTime()))))
  }

  def update(id: BSONObjectID, employee: Employee):Future[WriteResult] = {

    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("_id" -> id),
        employee.copy(
          _creationDate = Some(new DateTime())
        )
      ))
  }

  def delete(id: BSONObjectID):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
  }
}