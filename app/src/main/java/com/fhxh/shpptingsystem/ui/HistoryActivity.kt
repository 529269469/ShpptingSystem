package com.fhxh.shpptingsystem.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.brightek.thermallibrary.ImageCommand
import com.chad.library.adapter.base.entity.node.BaseNode
import com.fhxh.shpptingsystem.MyApp
import com.fhxh.shpptingsystem.R
import com.fhxh.shpptingsystem.databinding.ActivityHistoryBinding
import com.fhxh.shpptingsystem.room.AppDatabase
import com.fhxh.shpptingsystem.room.entity.BulletBean
import com.fhxh.shpptingsystem.room.entity.BureauBean
import com.fhxh.shpptingsystem.sdk.BrightekCommandM
import com.fhxh.shpptingsystem.ui.adapter.HistoryCountAdapter
import com.fhxh.shpptingsystem.ui.adapter.HistoryDataAdapter
import com.fhxh.shpptingsystem.ui.adapter.HistoryPersonAdapter
import com.fhxh.shpptingsystem.ui.adapter.tree.*
import com.fhxh.shpptingsystem.ui.bean.PaintXY
import com.fhxh.shpptingsystem.utils.AnalysisAchievementUtils
import com.fhxh.shpptingsystem.utils.AnalysisAchievementUtils2
import com.fhxh.shpptingsystem.utils.BItmaoUtils
import com.fhxh.shpptingsystem.utils.ExcelUtil
import com.fhxh.shpptingsystem.utils.usb.USBView
import com.fhxh.shpptingsystem.view.CreationChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.hjq.toast.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS", "UNUSED_EXPRESSION")
class HistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityHistoryBinding
    private val TAG = "HistoryActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setNavigationBarVisible(this, true)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        if (!EventBus.getDefault().isRegistered(this)) { //这里的取反别忘记了
            EventBus.getDefault().register(this)
        }


        //初始化点击事件
        initOnClickListener()

        initData()

        initLineChart()

        initCountRecycler()

        refresh()

    }

    /**
     * 点击日期条目时 刷新该 adapter
     */
    private var historyCountAdapter: HistoryCountAdapter? = null

    //子弹集合
    private val listBullet = mutableListOf<BulletBean>()

    //子弹序
    private var historyCountPosition = 0

    @SuppressLint("NotifyDataSetChanged")
    private fun initCountRecycler() {
        val layoutManager = GridLayoutManager(this, 5)
        binding.historyCount.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        historyCountAdapter = HistoryCountAdapter(mutableListOf())
        binding.historyCount.adapter = historyCountAdapter
        historyCountAdapter?.setList(listBullet)
        historyCountAdapter?.setOnItemClickListener { adapter, view, position ->
            if (historyCountAdapter?.getSelectIndex() !== position) {
                historyCountPosition = position
                historyCountAdapter?.setSelectIndex(historyCountPosition)
                historyCountAdapter?.notifyDataSetChanged()

                binding.historyCurrentRingNum.text =
                    "当前环数: " + listBullet[position].cylinder_number

            }
            if (!binding.historyImage.isPlayback) {
                if (historyCountPosition > 0 && isEvent) {
                    historyCountPosition = position
                    historyCountAdapter?.setSelectIndex(historyCountPosition)
                    historyCountAdapter?.notifyDataSetChanged()
                    binding.historyCurrentRingNum.text =
                        "当前环数: " + listBullet[historyCountPosition].cylinder_number
                    val db = Room.databaseBuilder(
                        MyApp.getApplication(),
                        AppDatabase::class.java, "users_dp"
                    ).build()

                    Thread {
                        val saveXY = mutableListOf<PaintXY>()
                        val findBulletById =
                            db.trackDao().findByTrack(listBullet[historyCountPosition].uid!!)
                        var uidtoString = "${listBullet[historyCountPosition].uid.toString()}2"
                        val findBulletList2 = db.trackDao().findByTrack(uidtoString.toLong())
                        binding.historyImage.reset()

                        setChart?.clear()
                        setChart2?.clear()
                        setChart3?.clear()
                        findBulletById.forEach{
                            var ringNumber = binding.historyImage.getViewRingNumber(
                                it.track_point_x!!,
                                it.track_point_y!!
                            )
                            setChart?.AddData(ringNumber[0].toFloat())
                            setChart2?.AddData(it.track_point_x!!)
                            setChart3?.AddData(it.track_point_y!!)
                        }
                        findBulletById.forEach {
                            Thread.sleep(50)
                            val paintXY = PaintXY(it.track_point_x!!, it.track_point_y!!)
                            saveXY.add(PaintXY(it.track_point_x!!, it.track_point_y!!))

                            runOnUiThread {
                                binding.historyImage.setListxy(paintXY, findBulletById.size)
                            }
                            if (findBulletById[findBulletById.size - 1].uid == it.uid) {
                                runOnUiThread {
                                    binding.historyImage.setPlayback(paintXY)
                                }
                                findBulletList2.forEach { findBulletList2 ->
                                    Thread.sleep(50)
                                    runOnUiThread {
                                        binding.historyImage.setListxy(
                                            PaintXY(
                                                findBulletList2.track_point_x!!,
                                                findBulletList2.track_point_y!!
                                            ), findBulletById.size
                                        )
                                    }
                                }
                            }
                        }
                        runOnUiThread {
                            RefushProgress(
                                initArray(
                                    saveXY,
                                    listBullet[historyCountPosition].cylinder_number!!
                                )
                            )
                        }
                    }.start()
                } else {
                    ToastUtils.show("已经是第一发了")
                }
            }



        }
    }


    private val historyDataAdapter: HistoryDataAdapter = HistoryDataAdapter()
    private val historyPersonAdapter: HistoryPersonAdapter = HistoryPersonAdapter()


    private fun initData() {
        binding.historyProgressHoldingGuntBar.setAnimProgress(0, "据枪\n")
        binding.historyProgressAimBar.setAnimProgress(0, "瞄准\n")
        binding.historyProgressFiringBar.setAnimProgress(0, "击发\n")
        binding.historyProgressAchievementGuntBar.setAnimProgress(0, "成绩\n")
        binding.historyProgressTotalityBar.setAnimProgress(0, "总体\n")


        inDateRecycler()


    }


    private var PersonName = ""
    private var Bureaus = ""
    private var isEvent = false


    //准备接受事件的订阅者
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(historyMessage: HistoryMessage) {
        if (!binding.historyImage.isPlayback) {
            isEvent = true
            val personname: String = historyMessage.person
            val bureausstr: String = historyMessage.bureau
            val bureausid: String = historyMessage.id
            PersonName = personname
            Bureaus = bureausstr
            val db = Room.databaseBuilder(
                MyApp.getApplication(),
                AppDatabase::class.java, "users_dp"
            ).build()

            Thread {
                bureauBean = db.bureauDao().findById(bureausid.toLong())
                runOnUiThread {
                    binding.historyPersonName.text = personname
                    binding.historyBureaus.text = bureauBean?.num.toString() + "局"
                    binding.historyHitTime.text = "打靶时间: " + bureauBean?.data_time
                    binding.historyTotalRingNum.text = "总环数: " + String.format(
                        "%.1f",
                        bureauBean?.total_ring_number
                    )
                }
                val findBulletList = db.bulletDao().findBulletList(bureauBean?.uid!!)
                if (findBulletList.isEmpty()) {
                    runOnUiThread {
                        ToastUtils.show("局错误！")
                    }
                    return@Thread
                }

                val findByTrack = db.trackDao().findByTrack(findBulletList[0].uid!!)

                var uidtoString = "${findBulletList[0].uid.toString()}2"
                val findBulletList2 = db.trackDao().findByTrack(uidtoString.toLong())

                val saveXY = mutableListOf<PaintXY>()
                binding.historyImage.reset()
                listBullet.clear()
                listBullet.addAll(findBulletList)
                listBullet.sortWith { u1, u2 ->
                    u1.number!!.compareTo(u2.number!!)
                }
                runOnUiThread {
                    historyCountAdapter?.setList(listBullet)
                    binding.historyCurrentRingNum.text =
                        "当前环数: " + listBullet[0].cylinder_number
                }
                setChart?.clear()
                setChart2?.clear()
                setChart3?.clear()
                findByTrack.forEach{
                    var ringNumber = binding.historyImage.getViewRingNumber(
                        it.track_point_x!!,
                        it.track_point_y!!
                    )
                    setChart?.AddData(ringNumber[0].toFloat())
                    setChart2?.AddData(it.track_point_x!!)
                    setChart3?.AddData(it.track_point_y!!)
                }
                findByTrack.forEach {
                    Thread.sleep(50)
                    saveXY.add(PaintXY(it.track_point_x!!, it.track_point_y!!))
                    val paintXY = PaintXY(it.track_point_x!!, it.track_point_y!!)

                    runOnUiThread {
                        binding.historyImage.setListxy(paintXY, findByTrack.size)



                    }
                    if (findByTrack[findByTrack.size - 1].uid == it.uid) {
                        runOnUiThread {
                            binding.historyImage.setPlayback(paintXY)
                        }
                        findBulletList2.forEach { findBulletList2 ->
                            Thread.sleep(50)
                            runOnUiThread {
                                binding.historyImage.setListxy(
                                    PaintXY(
                                        findBulletList2.track_point_x!!,
                                        findBulletList2.track_point_y!!
                                    ), findByTrack.size
                                )
                            }
                        }
                    }
                }

                runOnUiThread {
                    RefushProgress(initArray(saveXY, listBullet[0].cylinder_number!!))
                }

            }.start()
        }


    }

    /**
     * 根据轨迹获取成绩
     */
    private fun initArray(saveXY: MutableList<PaintXY>, ringNumber: Double): IntArray {
        val analysisAchieve = AnalysisAchievementUtils2.setEvement(
            saveXY,
            ringNumber,
            binding.historyImage
        )
        return analysisAchieve
    }

    /**
     * 测试成绩分析
     */
    private fun RefushProgress(source: IntArray) {
        binding.historyProgressHoldingGuntBar.setAnimProgress(source[0], "据枪\n")
        binding.historyProgressAimBar.setAnimProgress(source[1], "瞄准\n")
        binding.historyProgressFiringBar.setAnimProgress(source[2], "击发\n")
        binding.historyProgressAchievementGuntBar.setAnimProgress(source[3], "成绩\n")
        binding.historyProgressTotalityBar.setAnimProgress(source[4], "总体\n")
    }


    /**
     * 打靶日期记录 初始化
     */
    private fun inDateRecycler() {
        binding.historyData.layoutManager = LinearLayoutManager(this)
        binding.historyData.adapter = historyDataAdapter
        historyDataAdapter.setList(getDateEntity())
    }

    /**
     * 打靶人员记录 初始化
     */
    private fun inPersonRecycler() {
        binding.historyData.layoutManager = LinearLayoutManager(this)
        binding.historyData.adapter = historyPersonAdapter
        historyPersonAdapter.setList(getPersonEntity())
    }

    /**
     * 按照人员查询数据库生成2级列表
     *
     * @return 返回人员2级列表
     */
    private fun getPersonEntity(): MutableList<BaseNode>? {
        val listperson = mutableListOf<BaseNode>()
        val db = Room.databaseBuilder(
            MyApp.getApplication(),
            AppDatabase::class.java, "users_dp"
        ).build()

        Thread {
            //获取人员
            val bureauDao = db.bureauDao()
            val all = bureauDao.getAll()
            val personList = mutableListOf<Long>()
            all.forEach {
                personList.add(it.userId!!)
            }

            personList.distinct().forEach {
                val listbureaus = mutableListOf<BaseNode>()
                val findBureauId = bureauDao.findBureauId(it)
                val loadAllById = db.userDao().loadAllById(it)
                val sortedBy = findBureauId.sortedBy { it -> it.num }
                sortedBy.forEach { findBureauIdIt ->
                    val bureauNode = BureauNode(
                        findBureauIdIt.num.toString(),
                        loadAllById.user_name,
                        findBureauIdIt.uid.toString()
                    )
                    listbureaus.add(bureauNode)
                }
                val personNode = PersonNode(listbureaus, loadAllById.user_name)
                listperson.add(personNode)
            }
            runOnUiThread {
                historyPersonAdapter.setList(listperson)
                if (lastOrNext == 1) {
                    historyPersonAdapter.LastBureaus(PersonName, Bureaus)
                } else if (lastOrNext == 2) {
                    historyPersonAdapter.NextBureaus(PersonName, Bureaus)
                }
            }
        }.start()

        return listperson
    }


    /**
     * 按照日期查询数据库生成5级列表
     *
     * @return 返回日期5级列表
     */
    private fun getDateEntity(): MutableList<BaseNode>? {
        val listyears = mutableListOf<BaseNode>()
        val db = Room.databaseBuilder(
            MyApp.getApplication(),
            AppDatabase::class.java, "users_dp"
        ).build()

        Thread {
            //获取年
            val bureauDao = db.bureauDao()
            val all = bureauDao.getAll()
            val yearList = mutableListOf<String>()
            all.forEach {
                yearList.add(it.year!!)
            }

            //获取月
            yearList.distinct().forEach {
                val findBureauYear = bureauDao.findBureauYear(it)
                val monthList = mutableListOf<String>()
                val monthLists = mutableListOf<BaseNode>()
                findBureauYear.forEach { monthIt ->
                    monthList.add(monthIt.month!!)
                }

                //获取日
                monthList.distinct().forEach { monthsIt ->
                    val findBureauMonth = bureauDao.findBureauMonth(it, monthsIt)
                    val dayList = mutableListOf<String>()
                    val dayLists = mutableListOf<BaseNode>()
                    findBureauMonth.forEach { dayIt ->
                        dayList.add(dayIt.day!!)
                    }

                    dayList.distinct().forEach { daysIt ->
                        val findBureauDay = bureauDao.findBureauDay(it, monthsIt, daysIt)
                        val personList = mutableListOf<Long>()
                        val fourthperson = mutableListOf<BaseNode>()
                        findBureauDay.forEach { personIt ->
                            personList.add(personIt.userId!!)
                        }

                        personList.distinct().forEach { personIt ->
                            val fifthBureaus = mutableListOf<BaseNode>()
                            val findBureauId = bureauDao.findBureau(it, monthsIt, daysIt, personIt)
                            val loadAllById = db.userDao().loadAllById(personIt)
                            val sortedBy = findBureauId.sortedBy { it -> it.num }
                            sortedBy.forEach { fifth ->
                                val fifthbureaunode =
                                    FifthNode(
                                        fifth.num.toString(),
                                        loadAllById.user_name,
                                        fifth.uid.toString()
                                    )
                                fifthBureaus.add(fifthbureaunode)
                            }
                            val fourthpersonnode = FourthNode(fifthBureaus, loadAllById.user_name)
                            fourthperson.add(fourthpersonnode)

                        }
                        val thirdday = ThirdNode(fourthperson, daysIt + "日")
                        dayLists.add(thirdday)
                    }
                    val secondmonth = SecondNode(dayLists, monthsIt + "月")
                    monthLists.add(secondmonth)
                }
                val entity = FirstNode(monthLists, it + "年")
                listyears.add(entity)

            }
            runOnUiThread {
                historyDataAdapter.setList(listyears)
            }

        }.start()
        return listyears
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initOnClickListener() {
        binding.historyReturn.setOnClickListener { finish() }

        //轨迹
        binding.historyMove.setOnClickListener {
            binding.rlHistoryTarget.visibility = View.VISIBLE
            binding.historyRtLinear.visibility = View.GONE
            binding.historyRxyLinear.visibility = View.GONE
            binding.historyMove.background = resources.getDrawable(R.drawable.data)
            binding.historyRt.background =resources. getDrawable(R.drawable.charts)
            binding.historyRxy.background =resources. getDrawable(R.drawable.charts)
        }

        //RT
        binding.historyRt.setOnClickListener {
            binding.rlHistoryTarget.visibility = View.GONE
            binding.historyRtLinear.visibility = View.VISIBLE
            binding.historyRxyLinear.visibility = View.GONE
            binding.historyMove.background = resources.getDrawable(R.drawable.charts)
            binding.historyRt.background = resources.getDrawable(R.drawable.data)
            binding.historyRxy.background = resources.getDrawable(R.drawable.charts)
        }

        //TXY
        binding.historyRxy.setOnClickListener {
            binding.rlHistoryTarget.visibility = View.GONE
            binding.historyRtLinear.visibility = View.GONE
            binding.historyRxyLinear.visibility = View.VISIBLE
            binding.historyMove.background =resources. getDrawable(R.drawable.charts)
            binding.historyRt.background = resources.getDrawable(R.drawable.charts)
            binding.historyRxy.background = resources.getDrawable(R.drawable.data)
        }

        binding.historyDateText.setOnClickListener {
            binding.historyDateText.background = getDrawable(R.drawable.data)
            binding.historyPersonText.background = getDrawable(R.drawable.charts)
            inDateRecycler()
        }

        binding.historyPersonText.setOnClickListener {
            binding.historyDateText.background = getDrawable(R.drawable.charts)
            binding.historyPersonText.background = getDrawable(R.drawable.data)
            inPersonRecycler()
        }

        /**
         * 上一发
         */
        binding.historyLastShot.setOnClickListener {
            if (!binding.historyImage.isPlayback) {
                if (historyCountPosition > 0 && isEvent) {
                    historyCountPosition -= 1
                    historyCountAdapter?.setSelectIndex(historyCountPosition)
                    historyCountAdapter?.notifyDataSetChanged()
                    binding.historyCurrentRingNum.text =
                        "当前环数: " + listBullet[historyCountPosition].cylinder_number
                    val db = Room.databaseBuilder(
                        MyApp.getApplication(),
                        AppDatabase::class.java, "users_dp"
                    ).build()

                    Thread {
                        val saveXY = mutableListOf<PaintXY>()
                        val findBulletById =
                            db.trackDao().findByTrack(listBullet[historyCountPosition].uid!!)
                        var uidtoString = "${listBullet[historyCountPosition].uid.toString()}2"
                        val findBulletList2 = db.trackDao().findByTrack(uidtoString.toLong())
                        binding.historyImage.reset()

                        setChart?.clear()
                        setChart2?.clear()
                        setChart3?.clear()
                        findBulletById.forEach{
                            var ringNumber = binding.historyImage.getViewRingNumber(
                                it.track_point_x!!,
                                it.track_point_y!!
                            )
                            setChart?.AddData(ringNumber[0].toFloat())
                            setChart2?.AddData(it.track_point_x!!)
                            setChart3?.AddData(it.track_point_y!!)
                        }
                        findBulletById.forEach {
                            Thread.sleep(50)
                            val paintXY = PaintXY(it.track_point_x!!, it.track_point_y!!)
                            saveXY.add(PaintXY(it.track_point_x!!, it.track_point_y!!))
                            runOnUiThread {
                                binding.historyImage.setListxy(paintXY, findBulletById.size)

                            }
                            if (findBulletById[findBulletById.size - 1].uid == it.uid) {
                                runOnUiThread {
                                    binding.historyImage.setPlayback(paintXY)
                                }
                                findBulletList2.forEach { findBulletList2 ->
                                    Thread.sleep(50)
                                    runOnUiThread {
                                        binding.historyImage.setListxy(
                                            PaintXY(
                                                findBulletList2.track_point_x!!,
                                                findBulletList2.track_point_y!!
                                            ), findBulletById.size
                                        )
                                    }
                                }
                            }
                        }
                        runOnUiThread {
                            RefushProgress(
                                initArray(
                                    saveXY,
                                    listBullet[historyCountPosition].cylinder_number!!
                                )
                            )
                        }
                    }.start()
                } else {
                    ToastUtils.show("已经是第一发了")
                }
            }
        }

        /**
         * 下一发
         */
        binding.historyNextShot.setOnClickListener {
            if (!binding.historyImage.isPlayback) {
                if (historyCountPosition != listBullet.size - 1 && isEvent) {
                    historyCountPosition += 1
                    historyCountAdapter?.setSelectIndex(historyCountPosition)
                    historyCountAdapter?.notifyDataSetChanged()
                    binding.historyCurrentRingNum.text =
                        "当前环数: " + listBullet[historyCountPosition].cylinder_number
                    val db = Room.databaseBuilder(
                        MyApp.getApplication(),
                        AppDatabase::class.java, "users_dp"
                    ).build()

                    Thread {
                        val saveXY = mutableListOf<PaintXY>()
                        val findBulletById =
                            db.trackDao().findByTrack(listBullet[historyCountPosition].uid!!)
                        binding.historyImage.reset()

                        var uidtoString = "${listBullet[historyCountPosition].uid.toString()}2"
                        val findBulletList2 = db.trackDao().findByTrack(uidtoString.toLong())

                        setChart?.clear()
                        setChart2?.clear()
                        setChart3?.clear()
                        findBulletById.forEach{
                            var ringNumber = binding.historyImage.getViewRingNumber(
                                it.track_point_x!!,
                                it.track_point_y!!
                            )
                            setChart?.AddData(ringNumber[0].toFloat())
                            setChart2?.AddData(it.track_point_x!!)
                            setChart3?.AddData(it.track_point_y!!)
                        }
                        findBulletById.forEach {
                            Thread.sleep(50)

                            val paintXY = PaintXY(it.track_point_x!!, it.track_point_y!!)
                            saveXY.add(PaintXY(it.track_point_x!!, it.track_point_y!!))
                            runOnUiThread {
                                binding.historyImage.setListxy(paintXY, findBulletById.size)
                            }

                            if (findBulletById[findBulletById.size - 1].uid == it.uid) {
                                binding.historyImage.setPlayback(paintXY)

                                findBulletList2.forEach { findBulletList2 ->
                                    Thread.sleep(50)
                                    runOnUiThread {
                                        binding.historyImage.setListxy(
                                            PaintXY(
                                                findBulletList2.track_point_x!!,
                                                findBulletList2.track_point_y!!
                                            ), findBulletById.size
                                        )
                                    }
                                }
                            }

                        }
                        runOnUiThread {
                            RefushProgress(
                                initArray(
                                    saveXY,
                                    listBullet[historyCountPosition].cylinder_number!!
                                )
                            )


                        }
                    }.start()

                } else {
                    ToastUtils.show("已经是最后一发了")
                }
            }
        }

        /**
         * 上一局
         */
        binding.historyLastBureaus.setOnClickListener {
            if (!binding.historyImage.isPlayback) {
                if (PersonName != "" && Bureaus != "") {
                    InitPerson()
                    lastOrNext = 1
                    binding.historyCount.scrollToPosition(historyCountPosition--)
                }
            }

        }

        /**
         * 下一局
         */
        binding.historyNextBureaus.setOnClickListener {
            if (!binding.historyImage.isPlayback) {

                if (PersonName != "" && Bureaus != "") {
                    InitPerson()
                    lastOrNext = 2
                    binding.historyCount.scrollToPosition(historyCountPosition++)
                }
            }

        }

        /**
         * 导出excel
         */
        binding.historyExcel.setOnClickListener {
            if (bureauBean != null) {
                val file = File(ExcelUtil.getSDPath() + "/excel")
                ExcelUtil.makeDir(file)

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val time = sdf.format(Date()) //Calendar.getInstance().toString();


                val fileName =
                    file.toString() + "/" + binding.historyPersonName.text.toString() + bureauBean?.data_time + "-" + time + ".xls"

                ExcelUtil.initExcel(fileName, getTitles())

                Thread {
                    val recordData = getRecordData()
                    runOnUiThread {
                        ExcelUtil.writeObjListToExcel(recordData, fileName, this)
                    }
                }.start()

            } else {
                ToastUtils.show("请先选择局")
            }

        }


    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList></ArrayList><String>>
     *
     * @return
    </String> */
    private fun getRecordData(): ArrayList<ArrayList<String>>? {
        val recordList = ArrayList<ArrayList<String>>()
        val db = Room.databaseBuilder(
            MyApp.getApplication(),
            AppDatabase::class.java, "users_dp"
        ).build()
        val aims = db.bulletDao().findBulletList(bureauBean?.uid!!)
        var holding_gun = 0f
        var aim_f = 0f
        var shot_f = 0f
        var ach = 0f
        var total = 0f
        for (i in aims.indices) {
            val beanList = ArrayList<String>()
            beanList.add(binding.historyPersonName.text.toString())
            beanList.add(bureauBean?.data_time!!)
            beanList.add(bureauBean?.num.toString())
            beanList.add(aims[i].number.toString())
            beanList.add(aims[i].ju_qiang.toString())
            beanList.add(aims[i].miao_zhun.toString())
            beanList.add(aims[i].ji_fa.toString())
            beanList.add(aims[i].cheng_ji.toString())
            beanList.add(aims[i].zong_ti.toString())
            holding_gun += aims[i].ju_qiang!!
            aim_f += aims[i].miao_zhun!!
            shot_f += aims[i].ji_fa!!
            ach += aims[i].cheng_ji!!
            total += aims[i].zong_ti!!
            recordList.add(beanList)
        }
        val beanList1 = ArrayList<String>()
        beanList1.add("平均成绩")
        beanList1.add("")
        beanList1.add("")
        beanList1.add("")
        beanList1.add((holding_gun / aims.size).toString())
        beanList1.add((aim_f / aims.size).toString())
        beanList1.add((shot_f / aims.size).toString())
        beanList1.add((ach / aims.size).toString())
        beanList1.add((total / aims.size).toString())
        val beanList2 = ArrayList<String>()
        val s: String = dataCha(aims[0].data_time!!, aims[aims.size - 1].data_time!!)
        beanList2.add("总体用时")
        beanList2.add(s)
        recordList.add(beanList1)
        recordList.add(beanList2)
        return recordList
    }

    private fun dataCha(data1: String, data2: String): String {
        var time = 0L
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        try {
            val parse = dateFormat.parse(data1)
            val parse1 = dateFormat.parse(data2)
            time = abs(parse1.time - parse.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val diffSeconds = time / 1000 % 60
        val diffMinutes = time / (60 * 1000) % 60
        val diffHours = time / (60 * 60 * 1000) % 24
        return diffHours.toString() + "时" + diffMinutes + "分" + diffSeconds + "秒"
    }

    /**
     * @return 获取到excel的头
     */
    private fun getTitles(): Array<String>? {
        return arrayOf(
            "姓名", "日期", "打靶局ID", "子弹序号",
            "据枪", "瞄准", "击发", "成绩", "总体"
        )
    }


    var bureauBean: BureauBean? = null

    /**
     * 打印当前成绩
     */
    fun refresh() {
        binding.historyPrint.setOnClickListener {
            Thread {
                if (listBullet.isNotEmpty()) {

                    var string =
                        "射手姓名 :  " + binding.historyPersonName.text.toString() + "\n" +
                                "局    ID :  " + binding.historyBureaus.text.toString() + "\n" +
                                "枪    型 :  " + bureauBean?.qiang_id + " \n" +
                                "总 环 数 :  " + bureauBean?.total_ring_number + " \n" +
                                "时    间 :  " + bureauBean?.data_time + " \n" +
                                "\n" +
                                "\n" +
                                "发序 | 环数 | 方向 | 用时  \n"

                    MainActivity.thermalService?.usbSendDatas(BrightekCommandM.t1b63(2))
                    MainActivity.thermalService?.usbSendDatas(string.toByteArray(charset("gbk")))
                    val sortedPersons = listBullet.sortedBy { it.number }

                    var juqiang = 0
                    var miaozhun = 0
                    var jifa = 0
                    var chengji = 0
                    var zongti = 0
                    var pingjunchengji = 0.0

                    sortedPersons.forEach {

                        juqiang += it.ju_qiang!!
                        miaozhun += it.miao_zhun!!
                        jifa += it.ji_fa!!
                        chengji += it.cheng_ji!!
                        zongti += it.zong_ti!!
                        pingjunchengji += it.cylinder_number!!

                        var fangxiang = ""
                        fangxiang = when (it.direction) {
                            11, 12, 1 -> "↑"
                            2, 3, 4 -> "→"
                            5, 6, 7 -> "↓"
                            8, 9, 10 -> "←"
                            100 -> "靶心"
                            else -> "脱靶"
                        }
                        var number = it.number.toString()
                        var cylinderNumber = it.cylinder_number.toString()

                        if (number.length == 1) {
                            number = " $number"
                        }
                        if (cylinderNumber.length == 3) {
                            cylinderNumber = " $cylinderNumber"
                        }
                        if (fangxiang.length == 1) {
                            fangxiang = " $fangxiang "
                        }
                        val string1 =
                            " $number  | $cylinderNumber | $fangxiang | ${it.data_time} \n"
                        MainActivity.thermalService?.usbSendDatas(string1.toByteArray(charset("gbk")))
                    }

                    val usbView = USBView(this, listBullet)
                    val bitmapByScorw: Bitmap = BItmaoUtils.getBitmapByScorw(usbView)
                    MainActivity.thermalService?.usbSendDatas(BrightekCommandM.t1b61(0)) //居中
                    MainActivity.thermalService?.usbSendDatas(
                        ImageCommand.print1D76(
                            bitmapByScorw, 384
                        )
                    )
                    var pingjunchengji2 =
                        ("%.1f".format(pingjunchengji / listBullet.size).toDouble()).toString()
                    if (pingjunchengji2.length == 3) {
                        pingjunchengji2 = " $pingjunchengji2"
                    }

                    var juqiang1 = (juqiang / listBullet.size).toString()
                    if (juqiang1.length <= 2) {
                        juqiang1 = " $juqiang1"
                    }
                    if (juqiang1.length <= 2) {
                        juqiang1 = " $juqiang1"
                    }
                    var miaozhun1 = (miaozhun / listBullet.size).toString()
                    if (miaozhun1.length <= 2) {
                        miaozhun1 = " $miaozhun1"
                    }
                    if (miaozhun1.length <= 2) {
                        miaozhun1 = " $miaozhun1"
                    }
                    var jifa1 = (jifa / listBullet.size).toString()
                    if (jifa1.length <= 2) {
                        jifa1 = " $jifa1"
                    }
                    if (jifa1.length <= 2) {
                        jifa1 = " $jifa1"
                    }
                    var chengji1 = (chengji / listBullet.size).toString()
                    if (chengji1.length <= 2) {
                        chengji1 = " $chengji1"
                    }
                    if (chengji1.length <= 2) {
                        chengji1 = " $chengji1"
                    }
                    var zongti1 = (zongti / listBullet.size).toString()
                    if (zongti1.length <= 2) {
                        zongti1 = " $zongti1"
                    }
                    if (zongti1.length <= 2) {
                        zongti1 = " $zongti1"
                    }
                    var assessString =
                        "射击评估\n" +
                                "\n" +
                                "\n" +
                                "评估项目 | 报告值 | 结果  \n" +
                                "据    枪 |  $juqiang1   | ${averageResult(juqiang / listBullet.size)}\n" +
                                "瞄    准 |  $miaozhun1   | ${averageResult(miaozhun / listBullet.size)}\n" +
                                "击    发 |  $jifa1   | ${averageResult(jifa / listBullet.size)}\n" +
                                "成    绩 |  $chengji1   | ${averageResult(chengji / listBullet.size)}\n" +
                                "总    体 |  $zongti1   | ${averageResult(zongti / listBullet.size)}\n" +
                                "平均成绩 |  $pingjunchengji2  | ${averageResult(chengji / listBullet.size)}\n" +
                                "\n" +
                                "\n" +
                                "综合评价：\n" +
                                "   "

                    MainActivity.thermalService?.usbSendDatas(BrightekCommandM.t1b63(2))
                    MainActivity.thermalService?.usbSendDatas(assessString.toByteArray(charset("gbk")))

                    MainActivity.thermalService?.usbSendDatas(BrightekCommandM.t1b4a(80))
                }
            }.start()
        }
    }

    private fun averageResult(i: Int): String {
        var string = ""
        if (i >= 90) {
            string = "优秀"
        }
        if (i in 80..89) {
            string = "良好"
        }

        if (i in 70..79) {
            string = "中等"
        }

        if (i in 60..69) {
            string = "及格"
        }
        if (i < 60) {
            string = "不及格"
        }

        return string
    }


    private var lastOrNext = 0

    private fun InitPerson() {
        binding.historyDateText.background = getDrawable(R.drawable.charts)
        binding.historyPersonText.background = getDrawable(R.drawable.data)
        inPersonRecycler()
    }


    var setChart: CreationChart? = null
    var setChart2: CreationChart? = null
    var setChart3: CreationChart? = null
    private fun initLineChart() {
        setChart = CreationChart(binding.historyRtChart, "实际中靶瞄准环数");
        setChart?.init();

        setChart2 = CreationChart(binding.historyRxyChartX, "X轴瞄准对应环数");
        setChart2?.init();
        setChart3 = CreationChart(binding.historyRxyChartY, "Y轴瞄准对应环数");
        setChart3?.init();


    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) //加上判断
            EventBus.getDefault().unregister(this)
    }

    /**
     * 隐藏或显示 导航栏
     *
     * @param activity
     */
    private fun setNavigationBarVisible(activity: Activity, isHide: Boolean) {
        if (isHide) {
            val decorView = activity.window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        } else {
            val decorView = activity.window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView.systemUiVisibility = uiOptions
        }
    }
}