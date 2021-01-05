package com.open.pesantren.service

import com.open.pesantren.model.FileData
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 05 Jan 2021
 * Email soekenda09@gmail.com
 **/
interface FileService {

    fun uploadFile(userId: String, fileHeaders: HttpHeaders, file: Flux<DataBuffer>): Mono<FileData>

    fun find(userId: String): Flux<FileData>

    fun getFile(userId: String, fileName: String): Flux<DataBuffer>

    fun deleteFile(userId: String, fileName: String): Mono<Boolean>

}