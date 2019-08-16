package spplagiarise.cache

import org.mapdb.DB
import org.mapdb.DBMaker
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Disposes
import javax.enterprise.inject.Produces

@ApplicationScoped
class DBProducer {

    @Produces
    fun produceDB(): DB {
        return DBMaker.fileDB("db.blob")
                .make()
    }

    fun dispose(@Disposes db: DB) {
        db.commit()
        db.close()
    }
}