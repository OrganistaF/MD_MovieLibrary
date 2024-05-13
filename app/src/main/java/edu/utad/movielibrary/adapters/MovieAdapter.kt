package edu.utad.movielibrary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import edu.utad.movielibrary.R
import edu.utad.movielibrary.model.MovieModel

class MovieAdapter(
    private var movieList: ArrayList<MovieModel>,
    private var database: FirebaseDatabase,
    private var uid: String,
    private var onClickListener: (MovieModel) -> Unit
) :
    RecyclerView.Adapter<MovieViewHolder>() {

    /**
     * Template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_recycler, parent, false)
        return MovieViewHolder(view, database, uid)
    }

    /**
     * Match data with view
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.render(movie, onClickListener)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}
