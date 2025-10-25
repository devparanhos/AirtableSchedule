package com.airtable.interview.airtableschedule.models

import androidx.compose.ui.graphics.Color
import java.util.Date

object SampleTimelineItems {
    private val year = 2020 - 1900

    private fun generateColorFromName(name: String): Color {
        val hash = name.hashCode()
        val r = (hash shr 16 and 0xFF).coerceIn(80, 200)
        val g = (hash shr 8 and 0xFF).coerceIn(80, 200)
        val b = (hash and 0xFF).coerceIn(80, 200)
        return Color(r, g, b)
    }

    var timelineItems: List<Event> = listOf(
        Event(
            1,
            Date(year, 1, 1),
            Date(year, 1, 5),
            "First item",
            generateColorFromName("First item")
        ),
        Event(
            2,
            Date(year, 1, 2),
            Date(year, 1, 8),
            "Second item",
            generateColorFromName("Second item")
        ),
        Event(
            3,
            Date(year, 1, 6),
            Date(year, 1, 13),
            "Another item",
            generateColorFromName("Another item")
        ),
        Event(
            4,
            Date(year, 1, 14),
            Date(year, 1, 14),
            "Another item", generateColorFromName("Another item")
        ),
        Event(
            5,
            Date(year, 2, 1),
            Date(year, 2, 15),
            "Third item",
            generateColorFromName("Third item")
        ),
        Event(
            6,
            Date(year, 1, 12),
            Date(year, 2, 16),
            "Fourth item with a super long name",
            generateColorFromName("Fourth item with a super long name")
        ),
        Event(
            7,
            Date(year, 2, 1),
            Date(year, 2, 2),
            "Fifth item with a super long name",
            generateColorFromName("Fifth item with a super long name")
        ),
        Event(
            8,
            Date(year, 1, 3),
            Date(year, 1, 5),
            "First item", generateColorFromName("First item")
        ),
        Event(
            9,
            Date(year, 1, 4),
            Date(year, 1, 8),
            "Second item", generateColorFromName("Second item")
        ),
        Event(
            10,
            Date(year, 1, 6),
            Date(year, 1, 13),
            "Another item", generateColorFromName("Another item")
        ),
        Event(
            11,
            Date(year, 1, 9),
            Date(year, 1, 9),
            "Another item",
            generateColorFromName("Another item")
        ),
        Event(
            12,
            Date(year, 2, 1),
            Date(year, 2, 15),
            "Third item", generateColorFromName("Third item")
        ),
        Event(
            13,
            Date(year, 1, 12),
            Date(year, 2, 16),
            "Fourth item with a super long name",
            generateColorFromName("Fourth item with a super long name")
        ),
        Event(
            14,
            Date(year, 2, 1),
            Date(year, 2, 1),
            "Fifth item with a super long name",
            generateColorFromName("Fifth item with a super long name")
        )
    )
}