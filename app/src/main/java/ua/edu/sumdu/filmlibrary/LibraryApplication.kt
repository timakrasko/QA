package ua.edu.sumdu.filmlibrary

import android.app.Application
import ua.edu.sumdu.filmlibrary.data.AppContainer
import ua.edu.sumdu.filmlibrary.data.AppDataContainer


class LibraryApplication : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}