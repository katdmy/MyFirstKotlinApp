package com.katdmy.android.myfirstkotlinapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.viewmodel.MovieDetailsState
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory
import java.util.*

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireActivity()) }

    private var constraintLayout: ConstraintLayout? = null
    private var loadingMovieSpinner: ProgressBar? = null
    private var emptyMovieTv: TextView? = null
    private var loadingActorsSpinner: ProgressBar? = null
    private var emptyActorsTv: TextView? = null
    private var scrollView: NestedScrollView? = null

    private var backListener: BackClickListener? = null
    private var recycler: RecyclerView? = null
    private var adapter: ActorsAdapter? = null

    private var backdrop: ImageView? = null
    private var backButton: TextView? = null
    private var nameTextView: TextView? = null
    private var pgTextView: TextView? = null
    private var tagTextView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var reviewsTextView: TextView? = null
    private var overviewTextView: TextView? = null

    private var scheduleBtn: Button? = null
    private var isRationaleShown = false
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var currentMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        /*val transition = TransitionInflater.from(context).inflateTransition(R.transition.shared_element_transition)
        sharedElementEnterTransition = transition*/
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        initViews(view)
        setUpAdapter()
        setUpClickListener()

        val movieId = arguments?.getInt(MOVIE_ID, 0) ?: 0
        if (movieId > 0)
            viewModel.getMovieById(movieId).observe(
                viewLifecycleOwner,
                this::onMovieDetailsStateChange
            )
        else
            viewModel.onMovieSelected().observe(
                viewLifecycleOwner,
                this::onMovieDetailsStateChange
            )

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onCalendarWritePermissionGranted()
            } else {
                onCalendarWritePermissionNotGranted()
            }
        }
    }

    override fun onDestroyView() {
        scheduleBtn = null
        recycler?.adapter = null
        recycler = null
        backListener = null
        recycler = null
        backButton = null
        nameTextView = null
        pgTextView = null
        tagTextView = null
        ratingBar = null
        reviewsTextView = null
        overviewTextView = null
        backdrop = null
        loadingMovieSpinner = null
        emptyMovieTv = null
        loadingActorsSpinner = null
        emptyActorsTv = null
        scrollView = null
        constraintLayout = null
        super.onDestroyView()
    }


    private fun initViews(view: View) {
        scheduleBtn = view.findViewById(R.id.schedule_btn)
        recycler = view.findViewById(R.id.actors_list)
        backButton = view.findViewById(R.id.back)
        nameTextView = view.findViewById(R.id.name)
        pgTextView = view.findViewById(R.id.pg)
        tagTextView = view.findViewById(R.id.tag)
        ratingBar = view.findViewById(R.id.rating)
        reviewsTextView = view.findViewById(R.id.reviews_name)
        overviewTextView = view.findViewById(R.id.overview)
        backdrop = view.findViewById(R.id.orig)
        loadingMovieSpinner = view.findViewById(R.id.loading_movie_spinner)
        emptyMovieTv = view.findViewById(R.id.empty_movie_tv)
        loadingActorsSpinner = view.findViewById(R.id.loading_actors_spinner)
        emptyActorsTv = view.findViewById(R.id.empty_actors_tv)
        scrollView = view.findViewById(R.id.scroll_view)
        constraintLayout = view.findViewById(R.id.constraint_layout)
    }

    private fun setUpAdapter() {
        recycler?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = ActorsAdapter { adapter?.shuffleData() }
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        backListener = context as? BackClickListener
        backButton?.setOnClickListener { backListener?.detailsBack() }
        scheduleBtn?.setOnClickListener {
            activity?.let {
                when {
                    ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_CALENDAR)
                            == PackageManager.PERMISSION_GRANTED -> onCalendarWritePermissionGranted()
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR) ->
                        showLocationPermissionExplanationDialog()
                    isRationaleShown -> showLocationPermissionDeniedDialog()
                    else -> requestLocationPermission()
                }
            }
        }
    }

    private fun requestLocationPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    private fun onCalendarWritePermissionGranted() {
        context?.let {
            pickDate()
        }
    }

    private fun pickDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, p1, p2, p3 ->
                val datePicked = Calendar.getInstance().run {
                    set(p1, p2, p3, 0, 0)
                    timeInMillis
                }
                pickTime(datePicked)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun pickTime(datePicked: Long) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, p1, p2 ->
                val timePicked = (p1 * 60 + p2) * 60 * 1000L
                createCalendarEvent(datePicked + timePicked)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun createCalendarEvent(startMillis: Long) {
        val runtime = currentMovie?.runtime?.times(60 * 1000)?.toLong()
        val endMillis = runtime?.plus(startMillis) ?: startMillis.plus(2 * 60 * 1000)
        val scheduleIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, "Movie evening with ${currentMovie?.title}")
            .putExtra(
                CalendarContract.Events.DESCRIPTION,
                "You want to watch this movie on a weekend: ${currentMovie?.title} with rating ${currentMovie?.ratings}."
            )
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Cinema")
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(scheduleIntent)
    }

    private fun onCalendarWritePermissionNotGranted() {
        context?.let {
            Toast.makeText(context, R.string.permission_not_granted_text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLocationPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_dialog_explanation_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    isRationaleShown = true
                    requestLocationPermission()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showLocationPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_dialog_denied_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + it.packageName)
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }


    private fun onMovieDetailsStateChange(movieDetails: MovieDetailsState) {
        when (movieDetails) {
            is MovieDetailsState.LoadingMovie -> {
                constraintLayout?.visibility = View.GONE
                loadingMovieSpinner?.visibility = View.VISIBLE
                emptyMovieTv?.visibility = View.GONE
            }
            is MovieDetailsState.EmptyMovie -> {
                constraintLayout?.visibility = View.GONE
                loadingMovieSpinner?.visibility = View.GONE
                emptyMovieTv?.visibility = View.VISIBLE
            }
            is MovieDetailsState.Data -> {
                constraintLayout?.visibility = View.VISIBLE
                loadingMovieSpinner?.visibility = View.GONE
                emptyMovieTv?.visibility = View.GONE
                scrollView?.visibility = View.VISIBLE
                loadingActorsSpinner?.visibility = View.GONE
                emptyActorsTv?.visibility = View.GONE
                adapter?.setData(movieDetails.actors)
                fillMovieDetails(movieDetails.movie)
                startPostponedEnterTransition()
            }
            is MovieDetailsState.LoadingActors -> {
                constraintLayout?.visibility = View.VISIBLE
                loadingMovieSpinner?.visibility = View.GONE
                emptyMovieTv?.visibility = View.GONE
                scrollView?.visibility = View.GONE
                loadingActorsSpinner?.visibility = View.VISIBLE
                emptyActorsTv?.visibility = View.GONE
                adapter?.setData(emptyList())
                fillMovieDetails(movieDetails.movie)
            }
            is MovieDetailsState.EmptyActors -> {
                constraintLayout?.visibility = View.VISIBLE
                loadingMovieSpinner?.visibility = View.GONE
                emptyMovieTv?.visibility = View.GONE
                scrollView?.visibility = View.GONE
                loadingActorsSpinner?.visibility = View.GONE
                emptyActorsTv?.visibility = View.VISIBLE
                adapter?.setData(emptyList())
                fillMovieDetails(movieDetails.movie)
                startPostponedEnterTransition()
            }
        }
    }

    private fun fillMovieDetails(movie: Movie) {
        currentMovie = movie
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.movie_placeholder)
            .error(R.drawable.movie_placeholder)
        backdrop?.let {
            Glide.with(this)
                .load(movie.backdrop)
                .apply(options)
                .into(it)
        }

        nameTextView?.text = movie.title
        pgTextView?.text = "${movie.minimumAge}+"
        tagTextView?.text = movie.genres.toString().replace("[", "").replace("]", "")
        ratingBar?.rating = movie.ratings.div(2)
        reviewsTextView?.text = "${movie.numberOfRatings} reviews"
        overviewTextView?.text = movie.overview
    }

    interface BackClickListener {
        fun detailsBack()
    }

    companion object {
        private const val MOVIE_ID = "id"

        fun newInstance(id: Int) =
            MovieDetailsFragment().apply {
                arguments = Bundle()
                arguments?.putInt(MOVIE_ID, id)
            }
    }
}
