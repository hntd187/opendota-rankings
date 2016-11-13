package io.opendota
import scalikejdbc._

package object sql {


  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'debug,
    warningEnabled = true
  )
  implicit class RichInsertSQLBuilder(val self: InsertSQLBuilder) extends AnyVal {
    def onConflictUpdate(constraint: String)(columnsAndValues: (SQLSyntax, Any)*): InsertSQLBuilder = {
      val cvs = columnsAndValues.map(s => sqls"${s._1} = ${s._2}")
      self.append(sqls"ON CONFLICT ON CONSTRAINT ${SQLSyntax.createUnsafely(constraint)} DO UPDATE SET ${sqls.csv(cvs: _*)}")
    }
    def onConflictDoNothing(): InsertSQLBuilder = {
      self.append(sqls"ON CONFLICT DO NOTHING")
    }
  }
  implicit class RichSQLSyntax(val self: sqls.type) extends AnyVal {
    def values(column: SQLSyntax): SQLSyntax = sqls"values($column)"
  }
}
