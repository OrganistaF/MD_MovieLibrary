package edu.utad.movielibrary.model

import java.io.Serializable

class User(
    var name: String?=null,
    var email: String?=null,
    var pass: String?=null,
) : Serializable