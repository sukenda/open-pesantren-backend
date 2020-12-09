package com.open.pesantren.repository

import com.open.pesantren.entity.Pendaftar
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PendaftarRepository : ReactiveMongoRepository<Pendaftar, String> {

}