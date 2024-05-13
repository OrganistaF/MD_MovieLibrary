package edu.utad.movielibrary.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieModel(
    var id: Int,
    @SerializedName("title", alternate = ["name"])
    var title: String,
    var vote_average: Float,
    var overview: String,
    var poster_path: String,
    @SerializedName("release_date", alternate = ["first_air_date"])
    var release_date: String,
) :
    Serializable