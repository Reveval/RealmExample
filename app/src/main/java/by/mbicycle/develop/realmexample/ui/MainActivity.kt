package by.mbicycle.develop.realmexample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.realmexample.MainViewModel
import by.mbicycle.develop.realmexample.NotesAdapter
import by.mbicycle.develop.realmexample.R
import by.mbicycle.develop.realmexample.databinding.ActivityMainBinding
import by.mbicycle.develop.realmexample.models.Note

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: MainViewModel

    private var id: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        notesAdapter = NotesAdapter(NotesAdapter.OnClickListener { note ->
            createUpdateDialog(note)
        }, NotesAdapter.OnSwiper {
            id = it.id
        })

        val itemTouchHelperCallback = object:
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteNote(id!!)
                Toast.makeText(this@MainActivity, "Note Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecyclerview)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        viewModel.allNotes.observe(this, { allNotes ->
            notesAdapter.submitList(allNotes)
            binding.notesRecyclerview.adapter = notesAdapter
        })
    }

    private fun createUpdateDialog(note: Note) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.update_dialog, viewGroup, false)

        val titleEditText = dialogView.findViewById<EditText>(R.id.titleEditText_update)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.titleEditText_update)

        titleEditText.setText(note.title)
        descriptionEditText.setText(note.description)

        AlertDialog.Builder(this).apply {
            setView(dialogView)
            title = "Update Note"
            setPositiveButton("Update") { _, _ ->
                viewModel.updateNote(
                    note.id,
                    titleEditText.text.toString(),
                    descriptionEditText.text.toString()
                )
                notesAdapter.notifyDataSetChanged()
            }
            setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this@MainActivity, "Canceled Update", Toast.LENGTH_SHORT).show()
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.delete_all, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll) {
            viewModel.deleteAllNotes()
            notesAdapter.notifyDataSetChanged()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}