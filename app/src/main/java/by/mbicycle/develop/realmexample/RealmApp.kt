package by.mbicycle.develop.realmexample

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmApp: Application() {

    override fun onCreate() {
        super.onCreate()

        //инициализация Realm
        Realm.init(this)

        //конфигурируем Realm
        val configuration = RealmConfiguration.Builder()
            .name("todo.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()

        //сетаем конфигурацию
        Realm.setDefaultConfiguration(configuration)
    }
}