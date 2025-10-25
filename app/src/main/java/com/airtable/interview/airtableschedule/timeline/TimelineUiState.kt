package com.airtable.interview.airtableschedule.timeline

import com.airtable.interview.airtableschedule.models.Event
import java.util.Date

/**
 * UI state for the timeline screen.
 */
data class TimelineUiState(
    val events: List<Event> = emptyList(),
    val dates: List<Date> = emptyList(),
    val allocations: List<EventLaneAllocation> = emptyList()
)

data class EventLaneAllocation(
    val eventId: Int,
    val laneIndex: Int,
    val startDate: Date,
    val endDate: Date
)