package com.open.pesantren.controller

import com.open.pesantren.exception.FileException
import com.open.pesantren.model.FileData
import com.open.pesantren.service.FileService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

/**
 * Created by Kenda on 05 Jan 2021
 * Email soekenda09@gmail.com
 **/
@RestController
@RequestMapping("/files")
class FileController(val fileService: FileService) {

    @PostMapping
    fun uploadFile(exchange: ServerWebExchange): Mono<FileData> {
        return exchange.multipartData.flatMap { parts ->
            val map = parts.toSingleValueMap()
            val file = map["file"]!!
            val userId = dataBufferToString(map["userId"]!!.content())
            userId.flatMap { fileService.uploadFile(it, file.headers(), file.content().cache()) }
        }.onErrorMap { FileException(it.message) }
    }

    @GetMapping
    fun find(@RequestParam(required = true) userId: String
    ): Flux<FileData> = fileService.find(userId)

    @GetMapping("/{fileName}")
    fun getFile(
        @PathVariable fileName: String,
        @RequestParam(required = true) userId: String
    ): Flux<DataBuffer> = fileService.getFile(userId, fileName)

    @DeleteMapping("/{fileName}")
    fun deleteFile(
        @RequestParam(required = true) userId: String,
        @PathVariable fileName: String,
        response: ServerHttpResponse
    ): Mono<Void> {
        return fileService.deleteFile(userId, fileName).flatMap {
            if (it) Mono.empty()
            else Mono.error(FileException("Could not remove file $fileName"))
        }
    }

    private fun dataBufferToString(dataBuffer: Flux<DataBuffer>): Mono<String> =
        DataBufferUtils.join(dataBuffer).map { buffer ->
            val inputStream = buffer.asInputStream()
            val scanner = Scanner(inputStream).useDelimiter("\\A")
            if (scanner.hasNext()) scanner.next() else ""
        }

}