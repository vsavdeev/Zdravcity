package ru.test.zdravcity.ui

import android.util.Log
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.collections.ArrayList

class MainPresenter : MainContract.Presenter{

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MainContract.View

    private val mapBuy = mutableMapOf<Int,Float>()
    private val mapSale = mutableMapOf<Int,Float>()

    override fun onChartShow() {
        val subscribe = Observable.just(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val values = ArrayList<Entry>()
                val price = floatArrayOf(4f,3f,4f,4f,3f,5f,6f,4f,3f,4f,4f,6f,7f,7f)

                mapSale.clear()
                mapBuy.clear()

                for (i in price.indices){
                    values.add(Entry((i).toFloat(),price[i], null))

                    if (i!=price.size-1){
                        if (price[i] > price[i+1]){
                            if (i!=0){

                                if (price[i]==price[i-1])
                                    mapSale[i] = price[i]

                                if (price[i] > price[i-1]){
                                    mapSale[i] = price[i]
                                }
                            }else{
                                mapSale[i] = price[i]
                            }
                        }

                        if (price[i] < price[i+1]){
                            if (i!=0){

                                if (price[i]==price[i-1])
                                    continue

                                if (price[i] < price[i-1]){
                                    mapBuy[i] = price[i]
                                }
                            }
                        }
                    }else{
                        if (price[i]>price[i-1]) {
                            mapSale[i] = price[i]
                            continue
                        }

                        if (price[i] == price[i-1]) {
                            if (price[i-1]>price[i-2])
                                mapSale[i] = price[i]
                        }
                    }
                }

//                for (i in 1..price.size){
//
//                    values.add(Entry((i-1).toFloat(),price[i-1], null))
//
//                    if (i==1){
//
//                        if (price[i-1] == price[i]){
//                            if (price[i] > price[i+1]){
//                                mapBuy[i-1] = price[i-1]
//                                mapBuy[i] = price[i]
//                            }else{
//                                mapSale[i-1] = price[i-1]
//                                mapSale[i] = price[i]
//                            }
//                        }
//
//                        if (price[i-1] > price[i]){
//                            mapSale[i-1] = price[i-1]
//                        }else{
//                            mapBuy[i-1] = price[i-1]
//                        }
//
//                        if (price[i] > price[i+1]){
//                            if (price[i] > price[i-1]) mapSale[i] = price[i]
//                        }else{
//                            if (price[i] < price[i-1]) mapBuy[i] = price[i]
//                        }
//
//                    }
//
//                    if (i==price.size-1) {
//                        if (price[i-1] == price[i]){
//                            mapSale[i] = price[i]
//                        }
//                        if (price[i-1] > price[i]) {
//                            mapSale[i] = price[i-1]
//                        }
//                        values.add(Entry((i).toFloat(),price[i], null))
//                        break
//                    }
//
//                    if (price[i] > price[i+1]){
//                        if (price[i] > price[i-1]) mapSale[i] = price[i]
//                    }else{
//                        if (price[i] < price[i-1]) mapBuy[i] = price[i]
//                    }
//
//                    if (price[i] == price[i-1] || price[i] == price[i+1]){
//
//                        if (price[i] < price[i+1]) {
//                            continue
//                        }
//                        else {
//                            mapSale[i] = price[i]
//                        }
//                    }
//                }

                val set = LineDataSet(values,"")
                set.notifyDataSetChanged()
                val dataSets = arrayListOf<ILineDataSet>(set)
                view.showChart(LineData(dataSets))
            }

        subscriptions.add(subscribe)
    }

    override fun onTableShow() {
        view.showTable(mapBuy,mapSale)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
    }
}