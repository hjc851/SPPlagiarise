package spplagiarise.cache

import org.mapdb.DB
import org.mapdb.HTreeMap
import org.mapdb.Serializer
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Disposes
import javax.enterprise.inject.Produces
import javax.inject.Inject
import javax.inject.Singleton

@ApplicationScoped
class CacheProducer {

    @Inject
    private lateinit var db: DB

    @Produces
    @Singleton
    fun produceCache(): HTreeMap<String, List<String>> {
        return db.hashMap("synonyms")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen() as HTreeMap<String, List<String>>
    }

    fun dispose(@Disposes cache: HTreeMap<String, List<String>>) {
        cache.close()
    }
}