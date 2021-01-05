package com.open.pesantren.service.impl

import com.open.pesantren.model.FileData
import com.open.pesantren.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.io.File
import java.nio.channels.ByteChannel
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*

/**
 * Created by Kenda on 05 Jan 2021
 * Email soekenda09@gmail.com
 **/
@Service
class FileServiceImpl : FileService {

    @Value("\${file.path.upload}")
    private val path: String? = null

    override fun uploadFile(userId: String, fileHeaders: HttpHeaders, file: Flux<DataBuffer>): Mono<FileData> {
        val fileName = UUID.randomUUID().toString()
        val pathFile = "$path/$userId/$fileName"

        return DataBufferUtils.write(file, createFile(pathFile))
            .map { DataBufferUtils.release(it) }
            .then(fileName.toMono())
            .map { FileData(it, pathFile) }
    }

    override fun find(userId: String): Flux<FileData> {
        val parentFile = File("$path/$userId")
        return parentFile.list()?.toFlux()?.map { FileData(it, "$path/$userId/$it") } ?: Flux.empty()
    }

    override fun getFile(userId: String, fileName: String): Flux<DataBuffer> {
        val filePath = File("$path/$userId/$fileName").toPath()
        return DataBufferUtils.readInputStream(
            { Files.newInputStream(filePath) },
            DefaultDataBufferFactory(),
            3
        )
    }

    override fun deleteFile(userId: String, fileName: String): Mono<Boolean> {
        val deleted = File("$path/$userId/$fileName").delete()
        return deleted.toMono()
    }

    private fun createFile(path: String): ByteChannel {
        val tempFile = File(path)
        if (!Files.exists(tempFile.parentFile.toPath())) {
            Files.createDirectories(tempFile.parentFile.toPath())
        }

        tempFile.createNewFile()
        val filePath = tempFile.toPath()
        return Files.newByteChannel(filePath, StandardOpenOption.WRITE)
    }

}