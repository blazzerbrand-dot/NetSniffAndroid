package com.example.netsniffandroid.network.parser

object PortClassifier {
    fun classify(port:Int) :String  {
        return when(port){
            80 -> "HTTP"
            443 -> "HTTPS"
            53 ->"DNS"
            22->"SSH"
            else -> "UNKNOWN"
        }
    }
}