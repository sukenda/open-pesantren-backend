package com.open.pesantren.repository

import com.open.pesantren.entity.Kamar
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface KamarRepository : ReactiveMongoRepository<Kamar, String> {

}