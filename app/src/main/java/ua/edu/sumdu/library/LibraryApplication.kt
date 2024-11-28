package ua.edu.sumdu.library

import android.app.Application
import ua.edu.sumdu.library.data.AppContainer
import ua.edu.sumdu.library.data.AppDataContainer


class LibraryApplication : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}