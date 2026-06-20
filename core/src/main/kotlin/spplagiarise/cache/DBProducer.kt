package spplagiarise.cache

import org.mapdb.DB
import org.mapdb.DBMaker
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Disposes
import jakarta.enterprise.inject.Produces

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