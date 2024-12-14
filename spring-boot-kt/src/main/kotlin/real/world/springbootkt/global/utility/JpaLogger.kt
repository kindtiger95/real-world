package real.world.springbootkt.global.utility

import jakarta.persistence.EntityManager
import org.hibernate.Session
import org.hibernate.internal.SessionImpl

object JpaLogger {
    fun showPersistenceContext(entityManager: EntityManager) {
        val session = entityManager.unwrap(Session::class.java)
        val delegate = session.javaClass.getMethod("getDelegate")
        val sessionImpl = (delegate.invoke(session) as SessionImpl)
        val persistenceContext = sessionImpl.persistenceContext
        println("============================PersistenceContext============================")
        persistenceContext.reentrantSafeEntityEntries().forEach { (key, value) ->
            println("Session: $sessionImpl, EntityName: ${value.entityName}, Status: ${value.status}, Instance: $key")
        }
        println("============================PersistenceContext============================")
    }
}