package controllers

import javax.inject._

import play.twirl.api.Html
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import play.api.libs._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SparkStreamController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index = Action {

    Ok(Html("<h1>Spark Streaming is running. Check Console.</h1><a href='#'>Stop Streaming</a>"))
  }



}
