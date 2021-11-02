package by.mbicycle.develop.realmexample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.mbicycle.develop.realmexample.MainViewModel
import by.mbicycle.develop.realmexample.R
import by.mbicycle.develop.realmexample.databinding.ActivityAddNoteBinding

/*
    Здесь реализуем добавление новых данных в Realm
 */
class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.let { bind ->
            bind.saveButton.setOnClickListener {
                if (bind.titleEditText.text.toString()
                        .isEmpty() || bind.descriptionEditText.text.toString().isEmpty()
                ) {
                    return@setOnClickListener
                } else {
                    viewModel.addNote(
                        bind.titleEditText.text.toString(),
                        bind.descriptionEditText.text.toString()
                    )

                    startActivity(Intent(this, MainActivity::class.java))
                }
            }

        }
    }
}