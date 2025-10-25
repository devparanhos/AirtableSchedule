package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.Date

/**
 * ViewModel responsible for managing the state of the timeline screen.
 */
class TimelineViewModel : ViewModel() {
    private val eventDataRepository: EventDataRepository = EventDataRepositoryImpl()

    val uiState: StateFlow<TimelineUiState> = eventDataRepository
        .getTimelineItems()
        .map { events ->
            val sortedEvents = events.sortedBy { it.startDate }
            val allocations = buildAllEventLaneAllocations(sortedEvents)

            TimelineUiState(
                events = sortedEvents,
                dates = createDaysRange(sortedEvents),
                allocations = allocations
            )
        }
        .stateIn(
            viewModelScope,
            initialValue = TimelineUiState(),
            started = SharingStarted.WhileSubscribed()
        )

    private fun createDaysRange(events: List<Event>): List<Date> {
        val minStartDate = events.minByOrNull { it.startDate }?.startDate
        val maxEndDate = events.maxByOrNull { it.endDate }?.endDate

        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = minStartDate!!

        while (!calendar.time.after(maxEndDate!!)) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    private fun buildAllEventLaneAllocations(events: List<Event>): List<EventLaneAllocation> {
        val allocations = mutableListOf<EventLaneAllocation>()
        for (event in events.sortedBy { it.startDate }) {
            assignEventToLane(event, allocations)
        }
        return allocations
    }

    private fun assignEventToLane(
        newEvent: Event,
        currentAllocations: MutableList<EventLaneAllocation>
    ): EventLaneAllocation {
        val newStart = newEvent.startDate.stripTime()
        val newEnd = newEvent.endDate.stripTime()

        val overlappingAllocations = currentAllocations.filter {
            val overlap = !(newEnd.before(it.startDate) || newStart.after(it.endDate))
            overlap
        }

        val usedLanes = overlappingAllocations.map { it.laneIndex }.toSet()

        var laneIndex = 0
        while (laneIndex in usedLanes) {
            laneIndex++
        }

        val allocation = EventLaneAllocation(
            eventId = newEvent.id,
            laneIndex = laneIndex,
            startDate = newStart,
            endDate = newEnd
        )

        currentAllocations.add(allocation)
        return allocation
    }
}