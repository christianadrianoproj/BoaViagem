package com.example.boaviagem.util

import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {
        fun formatarValor(valor: Double): String {
            return String.format("%.2f", valor)
        }

        fun formatarData(data: Date) : String {
            val format = SimpleDateFormat("dd/MM/yyyy")
            return format.format(data)
        }

        fun DataStrToDate(data: String): Date {
            val date = SimpleDateFormat("dd/MM/yyyy").parse(data)
            return date
        }
    }
}