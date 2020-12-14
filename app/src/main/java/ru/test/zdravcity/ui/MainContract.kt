package ru.test.zdravcity.ui

import com.github.mikephil.charting.data.LineData
import ru.test.zdravcity.BaseContract

class MainContract {

    interface View: BaseContract.View {
        fun showChart(data: LineData)
        fun showTable(mapBuy: MutableMap<Int, Float>,mapSale: MutableMap<Int, Float>)
    }

    interface Presenter: BaseContract.Presenter<MainContract.View> {
        fun onChartShow()
        fun onTableShow()
    }
}