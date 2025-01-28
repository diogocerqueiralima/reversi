package com.github.group06.chelas_reversi.domain

data class Timer(val value: Int){

    init {
        require(value>0){"Value must be higher than 0"}
    }

    operator fun dec() = copy(value = value-1)

}