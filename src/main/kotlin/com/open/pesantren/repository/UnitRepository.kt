package com.open.pesantren.repository

import com.open.pesantren.entity.Unit
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UnitRepository : ReactiveMongoRepository<Unit, String> {

}