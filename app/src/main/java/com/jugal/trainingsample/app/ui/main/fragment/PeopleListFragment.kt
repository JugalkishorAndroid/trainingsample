package com.jugal.trainingsample.app.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.jugal.trainingsample.MyTrainingApplication
import com.jugal.trainingsample.R
import com.jugal.trainingsample.app.ui.main.adapter.PeoplesListAdapter
import com.jugal.trainingsample.app.viewmodel.PeopleViewModel
import com.jugal.trainingsample.data.event.NetworkEvent
import com.jugal.trainingsample.data.model.PeopleRemote
import kotlinx.android.synthetic.main.fragment_people_list.*
import kotlinx.android.synthetic.main.fragment_people_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PeopleListFragment : Fragment(), PeoplesListAdapter.OnItemClickListener {

    private val viewModel by viewModel<PeopleViewModel>()
    private lateinit var peopleListAdapter: PeoplesListAdapter
    private var peoplesList: MutableList<PeopleRemote>? = mutableListOf()
    private var pageSize = 10
    private var page: Int = 1
    private var loading: Boolean = false
    private var nextUrl: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_people_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)
        initSwipeToRefresh(view)
        initRecyclerView(view)
        subscribeToPeopleList()
        subscribeToNetworkEvent()
    }

    override fun onClick(view: View, people: PeopleRemote) {
        if (MyTrainingApplication.isNetworkConnected()) {
            val peopleUrl = "https://reqres.in/api/users/" + people.id
            findNavController().navigate(
                R.id.action_people_list_fragment_to_people_details_fragment,
                bundleOf(PeopleDetailsFragment.ARG_PEOPLE_URL to peopleUrl)
            )
        } else {
            showMessage(R.string.no_internet_connection)
        }
    }

    private fun initRecyclerView(view: View) {
        view.people_list_swipe_to_refresh.people_list_recycler_view.apply {
            this.adapter = PeoplesListAdapter(this@PeopleListFragment).also {
                peopleListAdapter = it
            }
            if (this.itemDecorationCount == 0) this.addItemDecoration(PeoplesListAdapter.PeopleListDecoration())

            //Pagination
            paginationListener(this)
        }
    }

    private fun initToolbar(view: View) {
        view.people_list_tool_bar.apply {
        }
    }

    private fun initSwipeToRefresh(view: View) {
        view.people_list_swipe_to_refresh.apply {
            setOnRefreshListener {
                peoplesList?.clear()
                page = 0
                subscribeToPeopleList()
            }
        }
    }

    private fun paginationListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && nextUrl) {
                        loading = true
                        subscribeToPeopleList()
                    }
                }
            }
        })
    }

    private fun subscribeToPeopleList() {
        if (MyTrainingApplication.isNetworkConnected()) {
            viewModel.getPeoplesList(page, pageSize).observe(viewLifecycleOwner) {peopleListData ->
                loading = false
                if (peopleListData.peopleList.size == pageSize) {
                    page += 1
                    nextUrl = true
                } else
                    nextUrl = false

                Log.d("peopleListData","______"+peopleListData.toString())
                peopleListData.peopleList.forEach { people ->
                    if (!peoplesList?.contains(people)!!) {
                        peoplesList?.addAll(peopleListData.peopleList)
                    }
                }

                hideSwipeToRefresh()

                if (!peoplesList.isNullOrEmpty()) {
                    peopleListAdapter.peoplesList = peoplesList!!
                }
            }
        } else {
            nextUrl = false
            hideSwipeToRefresh()
            viewModel.getPeopleListOffline { peopleList ->
                Log.e("people", "size___" + peopleList.size)
                peopleList.forEach {
                    peoplesList?.add(
                        PeopleRemote(
                            it.id,
                            it.email,
                            it.firstName,
                            it.lastName,
                            it.avatar
                        )
                    )
                }
                if (!peoplesList.isNullOrEmpty()) {
                    peopleListAdapter.peoplesList = peoplesList!!
                }
            }
        }
    }

    private fun subscribeToNetworkEvent() {
        viewModel.networkEvents.observe(viewLifecycleOwner) {
            when (it) {
                NetworkEvent.Loading -> showLoading(!people_list_swipe_to_refresh.isRefreshing)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        showMessage(it.res)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun showMessage(message: Int) {
        context?.run {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(people_list_root_layout)
        people_list_progress_bar_2.visibility = if (isLoading) View.VISIBLE else View.GONE
        TransitionManager.endTransitions(people_list_root_layout)
    }

    private fun hideSwipeToRefresh() {
        people_list_swipe_to_refresh.isRefreshing = false
    }

}
