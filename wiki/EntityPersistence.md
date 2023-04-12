#summary Server Entity Persistence
#labels Phase-Design

= Introduction =

Entities are persisted and retrieved using the an implementation of starcorp.server.entitystore.IEntityStore.  The default implementation (HibernateStore) uses the [http://www.hibernate.org/ hibernate] framework to map entities to a relational database.  
