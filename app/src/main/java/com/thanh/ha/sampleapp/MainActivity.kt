package com.thanh.ha.sampleapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.thanh.ha.piechart.PieChartView
import com.thanh.ha.piechart.PieItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pieChartView = findViewById<PieChartView>(R.id.pieChart)
        val fakeList = listOf(
            PieItem(20f, getColor(R.color.purple_500), "用"),
            PieItem(40f, getColor(R.color.red), "日用"),
            PieItem(80f, getColor(R.color.purple_700), "日用品"),
            PieItem(100f, getColor(R.color.teal_700), "交際費"),
            PieItem(
                120f, getColor(R.color.purple_200), "衣類"
            ),
        )
        pieChartView.submitList(fakeList)
        pieChartView.animateProgress(0, 360)

        findViewById<Button>(R.id.animateBtn).setOnClickListener {
            pieChartView.animateProgress(0, 180)
        }
        findViewById<Button>(R.id.animateBtn2).setOnClickListener {
            pieChartView.animateProgress(0, 360)
        }
    }
}