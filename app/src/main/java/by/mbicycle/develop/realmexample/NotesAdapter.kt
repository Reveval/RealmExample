package by.mbicycle.develop.realmexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.realmexample.databinding.NoteRowBinding
import by.mbicycle.develop.realmexample.models.Note

class NotesAdapter(
    private val onCLickListener: OnClickListener,
    private val onSwipe: OnSwiper):
    ListAdapter<Note, NotesAdapter.MyViewHolder>(MyDiffUtil){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NoteRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)

        onSwipe.onCLick(note)

        holder.itemView.setOnClickListener {
            onCLickListener.onClick(note)
        }
    }

    object MyDiffUtil: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

    }

    class OnClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

    class OnSwiper(val clickListener: (note: Note) -> Unit) {
        fun onCLick(note: Note) = clickListener(note)
    }

    inner class MyViewHolder(private val binding: NoteRowBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note?) {
            binding.titleTextView.text = note?.title
            binding.descriptionTextView.text = note?.description
        }
    }
}
