package com.open.pesantren.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Kenda on 02 Dec 2020
 * Email soekenda09@gmail.com
 **/
@Document(collection = "pendaftars")
data class Pendaftar(

    @Id
    private val id: String? = null,

    @Indexed
    private val noPendaftar: String?,

    @Indexed
    private val namaLengkap: String,

    private val tempatLahir: String,

    private val tanggalLahir: String,

    private val jenisKelamin: String,

    private val nomorNISN: String,

    private val sekolahAsal: String?,

    private val alamatSekolah: String?,

    private val telepon: String,

    private val nomorKK: String,

    private val ayahKandung: String,

    private val ktpAyah: String,

    private val ibuKandung: String,

    private val ktpIbu: String,

    private val alamatLengkap: String,

    private val nomorKIP: String,

    private val namaKIP: String,

    )
