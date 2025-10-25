package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.theme.ui.Blue200
import com.airtable.interview.airtableschedule.theme.ui.Blue300
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A screen that displays a timeline of events.
 */
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimelineView(events = uiState.events, dates = uiState.dates, allocations = uiState.allocations)
}

/**
 * A view that displays a list of events in swimlanes format.
 * TODO: Replace the exiting list with Swimlanes
 *
 * @param events The list of events to display.
 */
@Composable
private fun TimelineView(
    events: List<Event>,
    dates: List<Date>,
    allocations: List<EventLaneAllocation>
) {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    Column {
        LazyRow(
            modifier = Modifier
                .background(color = Blue300)
        ) {
            item {
                dates.forEach { calendarDate ->
                    Column(
                        modifier = Modifier
                            .widthIn(min = 80.dp, max = 80.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .background(Blue200, shape = RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .padding(8.dp),
                            text = formatter.format(calendarDate),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        val activeAllocations = allocations.filter { allocation ->
                            isDateWithinEvent(calendarDate, allocation.startDate, allocation.endDate)
                        }

                        val maxLane = activeAllocations.maxOfOrNull { it.laneIndex } ?: 0

                        for (laneIndex in 0..maxLane) {
                            val allocation = activeAllocations.firstOrNull { it.laneIndex == laneIndex }

                            if (allocation != null) {
                                val allocatedEvent = events.find { it.id == allocation.eventId }
                                val isStart = isStartOfEvent(calendarDate, allocatedEvent!!)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .background(allocatedEvent.color)
                                        .height(20.dp)
                                ) {
                                    if (isStart) {
                                        Text(
                                            text = allocatedEvent.name,
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp, vertical = 2.dp),
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .background(Color.Black)
                                        .height(20.dp)
                                ) {
                                    Text(
                                        text = "No event",
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun isDateWithinEvent(date: Date, start: Date, end: Date): Boolean {
    val normalizedDate = date.stripTime()
    val normalizedStart = start.stripTime()
    val normalizedEnd = end.stripTime()

    return !normalizedDate.before(normalizedStart) && !normalizedDate.after(normalizedEnd)
}
fun Date.stripTime(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@stripTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

fun isStartOfEvent(date: Date, event: Event): Boolean {
    return date.stripTime() == event.startDate.stripTime()
}