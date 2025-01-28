package com.github.group06.chelas_reversi.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.group06.chelas_reversi.ChelasReversiTestApplication

class ChelasReversiInstrumentationTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ChelasReversiTestApplication::class.java.name, context)
    }

}