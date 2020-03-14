package com.commov.Note.data

import java.util.*

class Note{
    var id: Int? = null
    var title: String = ""
    var description: String = ""
    var createdAt: Date = Date()
    lateinit var relevantAt: Date

    constructor(title: String, description: String, date: Date) {
        this.title = title
        this.description = description
        this.relevantAt = date
    }

    constructor(id: Int, title: String, description: String, createdAt: Date, date: Date) {
        this.id = id
        this.title = title
        this.description = description
        this.relevantAt = date
        this.createdAt = createdAt
    }


}