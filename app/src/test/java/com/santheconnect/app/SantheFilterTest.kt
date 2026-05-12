package com.santheconnect.app

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek

class SantheFilterTest {
    @Test
    fun marketFilterOnlyShowsMarketsOpenToday() {
        val mondayMarket = SampleData.places.first { it.id == "mandya-santhe" }
        val fridayMarket = SampleData.places.first { it.id == "ramanagara-santhe" }

        assertTrue(mondayMarket.isVisibleFor(PlaceFilter.Market, DayOfWeek.MONDAY))
        assertFalse(fridayMarket.isVisibleFor(PlaceFilter.Market, DayOfWeek.MONDAY))
    }
}
