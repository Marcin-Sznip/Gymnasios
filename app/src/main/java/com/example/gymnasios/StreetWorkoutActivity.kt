package com.example.gymnasios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnasios.databinding.ActivityStreetWorkoutBinding

class StreetWorkoutActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<StreetWorkout>
    private lateinit var imageId: Array<Int>
    private lateinit var heading: Array<String>
    private lateinit var longitude: Array<Double>
    private lateinit var latitude: Array<Double>
    private lateinit var binding: ActivityStreetWorkoutBinding
    private lateinit var  gymTitle: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBackFromStreetWorkout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageId = arrayOf(
            R.drawable.wroclaw_boleslawa_prusa,
            R.drawable.wroclaw_inowrsoclawska,
            R.drawable.wroclaw_krajewskieg,
            R.drawable.wroclaw_aleja_jana_kasprowicza,
            R.drawable.wroclaw_jednosci_narodowej,
            R.drawable.sucha1
        )

        heading = arrayOf(
            "Wrocław, ul. Bolesława Prusa",
            "Wrocław, ul.Inowrocławska",
            "Wrocław, ul. Krajewskiego",
            "Wrocław, ul. Aleja Jana Kasprowicza",
            "Wrocław, ul. Jedności Narodowej",
            "Wrocław, ul. Sucha"
        )

        gymTitle = arrayOf(
            "Street Workout Park",
            "Silownia na powietrzu",
            "FLOWPARK",
            "Wtreet Workout Park",
            "Silownia na powietrzu",
            "City Fit Wroclavia"
            )

        longitude = arrayOf(
            17.049667074872676,
            17.014835750288132,
            17.094990214393654,
            17.044188288027648,
            17.04311472977295,
            17.035391938648733
        )

        latitude = arrayOf(
            51.12026989376685,
            51.116894348319164,
            51.109818930209165,
            51.13580727113578,
            51.1272830919755,
            51.09735462787943
        )

        newRecyclerView = binding.recyclerView
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = ArrayList()
        getUserData()
    }

    private fun getUserData() {
        for (i in imageId.indices) {
            val streetWorkout = StreetWorkout(imageId[i], heading[i], longitude[i], latitude[i], gymTitle[i])
            newArrayList.add(streetWorkout)
        }
        newRecyclerView.adapter = MyAdapter(newArrayList) { streetWorkout ->
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("latitude", streetWorkout.latitude)
            intent.putExtra("longitude", streetWorkout.longitude)
            intent.putExtra("street", streetWorkout.street)
            intent.putExtra("gymTitle", streetWorkout.gymTitle)
            startActivity(intent)
        }
    }
}
