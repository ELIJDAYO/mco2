package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCalendarAndTimeBinding
import java.util.*


class CalendarAndTime : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarAndTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarAndTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var calendarMsg = ""
        var timeMsg = ""

        val datePicker = binding.datePicker
        val calendarToday = Calendar.getInstance()

        var calendarTodayYear = calendarToday.get(Calendar.YEAR)
        var calendarTodayMonth = calendarToday.get(Calendar.MONTH) + 1
        var calendarTodayDay = calendarToday.get(Calendar.DAY_OF_MONTH)

        calendarMsg = "You Selected: $calendarTodayYear/$calendarTodayMonth/$calendarTodayDay"
        datePicker.init(
            calendarToday.get(Calendar.YEAR), calendarToday.get(Calendar.MONTH),
            calendarToday.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            calendarMsg = "You Selected: $year/$month/$day"

        }

        val timePicker = binding.timePicker1


        var hour = timePicker.hour
        var am_pm = ""
        var minute = timePicker.minute
        // AM_PM decider logic
        when {
            hour == 0 -> {
                hour += 12
                am_pm = "AM"
            }
            hour == 12 -> am_pm = "PM"
            hour > 12 -> {
                hour -= 12
                am_pm = "PM"
            }
            else -> am_pm = "AM"
        }

        val hour1 = if (hour < 10) "0" + hour else hour
        val min = if (minute < 10) "0" + minute else minute
        // display format of time
        timeMsg = "Time is: $hour1 : $min $am_pm"

        timePicker.setOnTimeChangedListener() { _, hour, minute ->

            var hour2 = hour
            var am_pm2 = ""
            var minute2 = minute
            // AM_PM decider logic
            when {
                hour2 == 0 -> {
                    hour2 += 12
                    am_pm2 = "AM"
                }
                hour2 == 12 -> am_pm2 = "PM"
                hour2 > 12 -> {
                    hour2 -= 12
                    am_pm2 = "PM"
                }
                else -> am_pm2 = "AM"
            }

            val hour1 = if (hour < 10) "0" + hour2 else hour2
            val min = if (minute < 10) "0" + minute2 else minute2
            // display format of time
            timeMsg = "Time is: $hour1 : $min $am_pm2"


        }


        binding.calendarAndTimeBtn.setOnClickListener() {
            Toast.makeText(applicationContext, calendarMsg, Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, timeMsg, Toast.LENGTH_SHORT).show()
        }


    }
}