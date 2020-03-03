package com.commov.Note.data

import java.util.*

class Note{
    var description: String = ""
    var startDate: Date? = null
    var endDate: Date? = null
    var weekDays: Int? = null

    constructor() {}

    constructor(description: String, startDate: Int?, endDate: Int?, weekDays: Int?){
        this.description = description
        if (startDate != null)
            this.startDate = Date(startDate.toLong())
        if(endDate != null)
            this.endDate = Date(endDate.toLong())
        this.weekDays = weekDays
    }


}