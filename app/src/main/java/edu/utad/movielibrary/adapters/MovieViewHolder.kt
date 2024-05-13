package edu.utad.movielibrary.adapters

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.utad.movielibrary.model.MovieModel
import edu.utad.movielibrary.databinding.ItemRecyclerBinding
import edu.utad.movielibrary.env.EnvironmentVariables.BASE_IMAGE_URL

class MovieViewHolder(
    view: View,
    database: FirebaseDatabase,
    uid: String
) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemRecyclerBinding.bind(view)
    private val databaseRef = database.reference.child("users").child(uid).child("favMovies")

    fun render(movie: MovieModel, onClickListener: (MovieModel) -> Unit) {

        binding.titleRow.text = movie.title
        binding.ratingBar.rating = movie.vote_average / 2.0f
        Glide.with(binding.rowImage.context).load(BASE_IMAGE_URL + movie.poster_path)
            .into(binding.rowImage)

        checkIfFavorite(movie)

        binding.favoriteToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addMovieToFav(movie)
            } else {
                removeMovieFromFav(movie)
            }
        }

        binding.detailsButton.setOnClickListener {
            onClickListener(movie)
        }
    }

    private fun checkIfFavorite(movie: MovieModel) {
        databaseRef.child(movie.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.favoriteToggle.isChecked = snapshot.exists()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DatabaseError", "Failed to read value.", error.toException())
                }
            })
    }


    private fun removeMovieFromFav(movie: MovieModel) {
        databaseRef.child(movie.id.toString()).setValue(null)
    }

    private fun addMovieToFav(movie: MovieModel) {
        databaseRef.child(movie.id.toString()).setValue(movie)
    }

}