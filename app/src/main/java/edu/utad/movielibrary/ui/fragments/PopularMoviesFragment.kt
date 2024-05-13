package edu.utad.movielibrary.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.utad.movielibrary.adapters.MovieAdapter
import edu.utad.movielibrary.model.MovieModel
import edu.utad.movielibrary.R
import edu.utad.movielibrary.databinding.FragmentPopularMoviesBinding
import edu.utad.movielibrary.env.EnvironmentVariables.API_KEY
import edu.utad.movielibrary.env.EnvironmentVariables.BASE_URL
import edu.utad.movielibrary.env.EnvironmentVariables.DB_REF
import org.json.JSONArray


class PopularMoviesFragment : Fragment() {

    private lateinit var binding: FragmentPopularMoviesBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var movieList: ArrayList<MovieModel>
    private lateinit var favList: ArrayList<MovieModel>
    private var uid: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uid = arguments?.getString("uid")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val aas = activity as MainActivity
//        aas.toolBarShow()

        database =
            FirebaseDatabase.getInstance(
                DB_REF
            )

        movieList = arrayListOf()
        favList = arrayListOf()


        initRecyclerView()
        callApi()
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = binding.popularMovieRecycler
        recyclerView.layoutManager = manager
        recyclerView.adapter = MovieAdapter(
            movieList,
            database, uid!!
        ) { movie -> onItemClicked(movie) }
    }

    private fun callApi() {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $API_KEY"
        headers["Content-Type"] = "application/json"

        val request: JsonObjectRequest = @SuppressLint("NotifyDataSetChanged")
        object : JsonObjectRequest(
            Method.GET, "$BASE_URL/movie/popular", null,
            Response.Listener { response ->

                val movies: JSONArray = response.getJSONArray("results")
                val moviesList = mutableListOf<Map<String, Any>>()


                for (i in 0 until movies.length()) {
                    val movieJSON = movies.getJSONObject(i)
                    val movie: MovieModel = Gson().fromJson(movieJSON.toString(), MovieModel::class.java)
                    movieList.add(movie)
                    val movieMapType = object : TypeToken<Map<String, Any>>() {}.type
                    val movieMap: Map<String, Any> = Gson().fromJson(movieJSON.toString(), movieMapType)
                    moviesList.add(movieMap)
                }
                binding.popularMovieRecycler.adapter?.notifyDataSetChanged()
                database.reference.child("movies").setValue(moviesList)
            },
            Response.ErrorListener { error ->
                Log.e("Volley", "Error: $error")
            }) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }
        Volley.newRequestQueue(context).add(request)
    }

    private fun onItemClicked(movie: MovieModel) {

        val bundle = Bundle()
        bundle.putString("tittle", movie.title)
        bundle.putString("rating", movie.vote_average.toString())
        bundle.putString("overview", movie.overview)
        bundle.putString("date", movie.release_date)
        bundle.putString("poster", movie.poster_path)
        findNavController().navigate(
            R.id.action_popularMoviesFragment_to_movieDetailsFragment,
            bundle
        )

    }
}