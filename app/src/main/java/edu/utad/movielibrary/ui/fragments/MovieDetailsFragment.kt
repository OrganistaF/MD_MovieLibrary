package edu.utad.movielibrary.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import edu.utad.movielibrary.env.EnvironmentVariables.BASE_IMAGE_URL
import edu.utad.movielibrary.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private var title: String? = null
    private var rating: String? = null
    private var overview: String? = null
    private var date: String? = null
    private var poster: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        title = arguments?.getString("title")
        rating = arguments?.getString("rating")
        overview = arguments?.getString("overview")
        date = arguments?.getString("date")
        poster = arguments?.getString("poster")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.movieTitle.text = title
        binding.ratingBar.rating = rating!!.toFloat() / 2.0f
        binding.overview.text = overview
        binding.releaseDate.text = "Release date: $date"
        Glide.with(binding.imageViewMovie.context).load(BASE_IMAGE_URL + poster)
            .into(binding.imageViewMovie)
    }
}