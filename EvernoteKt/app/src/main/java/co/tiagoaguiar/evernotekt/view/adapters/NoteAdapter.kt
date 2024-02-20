package co.tiagoaguiar.evernotekt.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.databinding.ListItemNoteBinding
import kotlinx.android.synthetic.main.list_item_note.view.*

class NoteAdapter(private val notes: List<Note>, val onClickListener: (Note) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteView =
        NoteView(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_note,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: NoteView, position: Int) {
        val note = notes[position]
        holder.binding.note = note // essa var note eh a do XML na tag <data>
        // root nesse caso será a tag LinearLayout que eh a tag PRINCIPAL
        holder.binding.root.setOnClickListener { onClickListener.invoke(note) }
    }

    override fun getItemCount(): Int = notes.size

    // ListItemNoteBinding eh criada automaticamente
    inner class NoteView(val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}
