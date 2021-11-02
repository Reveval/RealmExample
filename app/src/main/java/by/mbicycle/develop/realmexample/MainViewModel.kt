package by.mbicycle.develop.realmexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.mbicycle.develop.realmexample.models.Note
import io.realm.Realm
import io.realm.kotlin.deleteFromRealm
import java.util.*

class MainViewModel: ViewModel() {
    //создаем экземпляр Realm с дефолтной конфигурацией
    private var realm: Realm = Realm.getDefaultInstance()

    val allNotes: LiveData<List<Note>>
    get() = getAllNotes()

    //добавляем данные в Realm
    fun addNote(noteTitle: String, noteDescription: String) {
        //выполняем транзакцию для добавления данных в Realm
        realm.executeTransaction { realm ->
            //создаем объект, который должен быть помещен в БД
            val note = realm.createObject(Note::class.java, UUID.randomUUID().toString()).apply {
                title = noteTitle
                description = noteDescription
            }
            /*
                Вызываем метод realm.insertOrUpdate (object), чтобы
                зафиксировать новые или обновленные данные в базе данных.
             */
            realm.insertOrUpdate(note)
        }
    }

    private fun getAllNotes(): MutableLiveData<List<Note>> {
        val list = MutableLiveData<List<Note>>()

        /*
            Берем данные из БД и помещаем их в RealmResult. Этот
            результат может быть конвертирован в список
         */
        val notes = realm.where(Note::class.java).findAll()
        list.value = notes?.subList(0, notes.size)
        return list
    }

    /*
        обновление данных похоже на вставку данных, только на этот раз
        мы запрашиваем данные с соответствующим идентификатором данных,
        которые нам нужно обновить.
     */
    fun updateNote(id: String, noteTitle: String, noteDesc: String) {
        val target = realm.where(Note::class.java)
            .equalTo("id", id)
            /*
               В нашем запросе мы добавим метод findFirst(), чтобы
               найти первый соответствующий экземпляр данных,
               которые нужно обновить.
                */
            .findFirst()

        realm.executeTransaction {
            target?.title = noteTitle
            target?.description = noteDesc
            realm.insertOrUpdate(target)
        }
    }

    //удаление одиночного item из объекта realm
    fun deleteNote(id: String) {
        val notes = realm.where(Note::class.java)
            .equalTo("id", id)
            .findFirst()

        realm.executeTransaction {
            notes!!.deleteFromRealm()
        }
    }

    //удаление всех данных из объекта Realm
    fun deleteAllNotes() {
        realm.executeTransaction {
            it.delete(Note::class.java)
        }
    }
}