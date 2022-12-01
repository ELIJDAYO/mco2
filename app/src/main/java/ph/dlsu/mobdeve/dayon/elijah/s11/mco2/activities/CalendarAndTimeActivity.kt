package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.WorkRepoItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCalendarAndTimeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import java.text.SimpleDateFormat
import java.util.*


class CalendarAndTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarAndTimeBinding
    private var episodeId: String=""
    private var formattedDateTime=""
    private lateinit var episodeRef: DatabaseReference

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

        calendarMsg = "$calendarTodayYear-$calendarTodayMonth-$calendarTodayDay"
        datePicker.init(
            calendarToday.get(Calendar.YEAR), calendarToday.get(Calendar.MONTH),
            calendarToday.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            calendarMsg = "$year/$month/$day"

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
        timeMsg = "$hour1:$min $am_pm"

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

            var hour1 = if (hour < 10) "0" + hour2 else hour2
            val min = if (minute < 10) "0" + minute2 else minute2
            // display format of time
            timeMsg = " $hour1:$min $am_pm2"
            if(am_pm == "PM"){
                val hourInt = Integer.parseInt(min as String) + 12
                hour1 = hourInt.toString()
                timeMsg = " $hour1:$min $am_pm2"
            }
        }

        binding.calendarAndTimeBtn.setOnClickListener() {
//            Toast.makeText(applicationContext,"$calendarMsg   $timeMsg", Toast.LENGTH_SHORT).show()

            val datetime = calendarMsg+" "+timeMsg

            formattedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN).parse(datetime)!!.toString()
            Log.e(TAG,"output datetime $formattedDateTime")
            fetchEpisodeInfo()
            val intent = Intent(this, WorkRepoActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
    private fun fetchEpisodeInfo(){
            Toast.makeText(applicationContext,"Entered fetchFun", Toast.LENGTH_SHORT).show()

        episodeId = intent.getStringExtra("episodeId").toString()
        episodeRef = FirebaseDatabase.getInstance().getReference("Episodes")
        val episodeMap = HashMap<String,Any>()
        episodeMap["episodeId"] = episodeId
        episodeMap["releaseDateTime"] = formattedDateTime
        episodeRef.child(episodeId).updateChildren(episodeMap)
    }
}