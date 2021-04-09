package com.example.githubuserapplication3

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.example.githubuserapplication3.databinding.ActivityMainBinding
import com.example.githubuserapplication3.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity(){

    private lateinit var binding: ActivityReminderBinding
    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        const val BOOLEAN_KEY = "boolean_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        binding.reminderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                alarmReceiver.setDailyReminder(this, AlarmReceiver.TYPE_REPEATING, "Open the app please")
            }else{
                alarmReceiver.cancelReminder(this, AlarmReceiver.TYPE_REPEATING)
            }
            saveData(isChecked)
        }

        alarmReceiver = AlarmReceiver()
    }

    private fun saveData(isChecked: Boolean) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("reminderPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(BOOLEAN_KEY, isChecked)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("reminderPref", Context.MODE_PRIVATE)
        val savedSwitch: Boolean = sharedPreferences.getBoolean(BOOLEAN_KEY, false)

        binding.reminderSwitch.isChecked = savedSwitch
    }

}