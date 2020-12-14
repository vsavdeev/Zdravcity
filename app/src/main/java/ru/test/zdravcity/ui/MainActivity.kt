package ru.test.zdravcity.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.test.zdravcity.databinding.ActivityMainBinding
import ru.test.zdravcity.di.component.DaggerActivityComponent
import ru.test.zdravcity.di.module.ActivityModule
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var presenter: MainContract.Presenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()
        initChart()
        initButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.onChartShow()
    }

    override fun showChart(data: LineData) {
        binding.chart1.data = data
        binding.chart1.data.notifyDataChanged()
        binding.chart1.data.dataSets.forEach {
            getILineDataSet(it as LineDataSet)
        }
        binding.chart1.invalidate()

        binding.chart1.animateY(1500)
    }

    override fun showTable(mapBuy: MutableMap<Int, Float>, mapSale: MutableMap<Int, Float>) {

        binding.table.removeAllViews()

        for (i in 0..4){
            val row = TableRow(this)
            row.layoutParams = TableLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

            when(i){
                0 -> {
                    for (j in 0..14) {
                        if (j == 0) {
                            row.addView(createCell(" "))
                            continue
                        }
                        row.addView(createCell("$j.02.2020"))
                    }
                }
                1 -> {
                    for (j in 0..14) {

                        if (j == 0) {
                            row.addView(createCell("Buy"))
                            continue
                        }

                        var temp: Float = 0f

                        mapBuy.forEach loop@{ (key, value) ->

                            if (key == j - 1) {
                                temp = value
                                return@loop
                            }
                        }
                        row.addView(createCell(temp, true))

                    }
                }
                2 -> {
                    for (j in 0..14) {

                        if (j == 0) {
                            row.addView(createCell("Sale"))
                            continue
                        }

                        var temp = 0f

                        mapSale.forEach loop@{ (key, value) ->

                            if (key == j - 1) {
                                temp = value
                                return@loop
                            }
                        }
                        row.addView(createCell(temp, false))
                    }
                }
                3 -> {

                    var buy = 0f
                    var sale = 0f

                    for (j in 0..14) {

                        if (j == 0) {
                            row.addView(createCell("Sum"))
                            continue
                        }

                        mapBuy.forEach loop@{ (key, value) ->
                            if (j - 1 == key) {
                                buy += value
                                return@loop
                            }
                        }
                        mapSale.forEach loop@{ (key, value) ->
                            if (j - 1 == key) {
                                sale += value
                                return@loop
                            }
                        }
                        row.addView(createCell((sale - buy).toString()))
                    }
                }
                4 -> {
                    row.addView(createCell("Total"))

                    var buy = 0f
                    var sale = 0f

                    mapBuy.forEach { (key, value) ->
                        buy += value
                    }
                    mapSale.forEach { (key, value) ->
                        sale += value
                    }
                    row.addView(createCell((sale - buy).toString()))
                }
            }
            binding.table.addView(row)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun createCell(float: Float, buy: Boolean) : View{
        val text = TextView(this)
        text.layoutParams = TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        text.setPadding(50, 50, 50, 50)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        if (float == 0f){
            Log.d("TAG", "createCell: achieved")
            text.text = "-"
        }else{
            if (buy)
                text.text = "-$float"
            else
                text.text = "+$float"
        }

        return text
    }

    private fun createCell(string: String) : View{
        val text = TextView(this)
        text.layoutParams = TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        text.setPadding(50, 50, 50, 50)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        text.text = string
        return text
    }

    private fun initButton(){
        binding.start.setOnClickListener {
            presenter.onTableShow()
        }
    }

    private fun initChart(){


        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(300)
        )
        binding.chart1.layoutParams = layoutParams

        binding.chart1.description.isEnabled = false
        binding.chart1.setTouchEnabled(true)
        binding.chart1.setDrawGridBackground(false)
        binding.chart1.isDragEnabled = true
        binding.chart1.setScaleEnabled(true)
        binding.chart1.setPinchZoom(true)

        val xAxis = binding.chart1.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        
        val yAxis = binding.chart1.axisLeft
        binding.chart1.axisRight.isEnabled = false
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        yAxis.axisMaximum = 10f
        yAxis.axisMinimum = -1f
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    private fun getILineDataSet(set: LineDataSet){
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.textColor, typedValue, true)
        val color = typedValue.data
        set.valueTextColor = color
        set.valueTextColor = color
        set.valueTextSize = 15f
        set.lineWidth = 1f
        set.circleRadius = 3f
        set.setDrawCircleHole(true)
    }

    private fun injectDependency() {
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }
}