package edu.utad.movielibrary.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.gson.Gson
import edu.utad.movielibrary.R
import edu.utad.movielibrary.adapters.MovieAdapter
import edu.utad.movielibrary.databinding.FragmentFavoriteMoviesBinding
import edu.utad.movielibrary.env.EnvironmentVariables.DB_REF
import edu.utad.movielibrary.model.MovieModel
import edu.utad.movielibrary.ui.MainActivity

class FavoriteMoviesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteMoviesBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var favList: ArrayList<MovieModel>
    private lateinit var uid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database =
            FirebaseDatabase.getInstance(
                DB_REF
            )
        favList = arrayListOf()

        val myActivity = activity as? MainActivity
        uid = myActivity?.getUserID().toString()

        initRecyclerView()
        fetchData()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchData() {
        database.reference.child("users").child(uid).child("favMovies")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favList.clear()  // Clear the list if you are refreshing it each time data changes
                    snapshot.children.forEach { movieSnapshot ->
                        val movieMap = movieSnapshot.getValue<Map<String, Any>>()
                        movieMap?.let {
                            val movieJson = Gson().toJson(it)
                            val movie = Gson().fromJson(movieJson, MovieModel::class.java)
                            favList.add(movie)
                        }
                    }
                    binding.favoriteMoviesRecycler.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = binding.favoriteMoviesRecycler
        recyclerView.layoutManager = manager
        recyclerView.adapter = MovieAdapter(
            favList,
            database, uid
        ) { movie -> onItemClicked(movie) }
    }

    private fun onItemClicked(movie: MovieModel) {
        val bundle = Bundle()
        bundle.putString("tittle", movie.title)
        bundle.putString("rating", movie.vote_average.toString())
        bundle.putString("overview", movie.overview)
        bundle.putString("date", movie.release_date)
        bundle.putString("poster", movie.poster_path)
        findNavController().navigate(
            R.id.action_favoriteMoviesFragment_to_movieDetailsFragment,
            bundle
        )
    }

}