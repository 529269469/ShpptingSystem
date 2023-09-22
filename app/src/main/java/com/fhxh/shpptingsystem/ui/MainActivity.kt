package com.fhxh.shpptingsystem.ui

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.media.AudioManager
import android.media.SoundPool
import android.os.*
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.brightek.thermallibrary.CString
import com.brightek.thermallibrary.ImageCommand
import com.brightek.thermallibrary.ThermalService
import com.fhxh.shpptingsystem.MyApp
import com.fhxh.shpptingsystem.R
import com.fhxh.shpptingsystem.base_app.Constant
import com.fhxh.shpptingsystem.databinding.ActivityMainBinding
import com.fhxh.shpptingsystem.room.AppDatabase
import com.fhxh.shpptingsystem.room.dao.UserDao
import com.fhxh.shpptingsystem.room.entity.BulletBean
import com.fhxh.shpptingsystem.room.entity.BureauBean
import com.fhxh.shpptingsystem.room.entity.TrackBean
import com.fhxh.shpptingsystem.room.entity.User
import com.fhxh.shpptingsystem.sdk.BrightekCommandM
import com.fhxh.shpptingsystem.ui.adapter.MainAdapter
import com.fhxh.shpptingsystem.ui.bean.*
import com.fhxh.shpptingsystem.ui.face.Open2FaceActivity
import com.fhxh.shpptingsystem.ui.face.OpenFaceActivity
import com.fhxh.shpptingsystem.utils.*
import com.fhxh.shpptingsystem.utils.SocketClient.OnDataReceiveListener
import com.fhxh.shpptingsystem.utils.serial.SerialInter
import com.fhxh.shpptingsystem.utils.serial.SerialManage
import com.fhxh.shpptingsystem.utils.serial.SerialManage2
import com.fhxh.shpptingsystem.utils.usb.USBDeviceParcelable
import com.fhxh.shpptingsystem.utils.usb.USBView
import com.fhxh.shpptingsystem.view.CreationChart
import com.google.gson.Gson
import com.hjq.toast.ToastUtils
import com.lztek.toolkit.Lztek
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var alertDialog: AlertDialog? = null
    private var settingDialog: AlertDialog? = null
    val TAG = "main"

    companion object {
        var thermalService: ThermalService? = null
    }

    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setNavigationBarVisible(this, true)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val decodeInt = MMKV.defaultMMKV()?.decodeInt("mainTextAllNum")
        binding.mainTextAllNum.text = "总发数 $decodeInt"

        EventBus.getDefault().register(this);
        val users_dp = Room.databaseBuilder(
            MyApp.getApplication(),
            AppDatabase::class.java, "users_dp"
        ).build()
        id = intent.getLongExtra("id", 0)

        Thread {
            val userDao: UserDao = users_dp.userDao()
            val userData = userDao.loadAllById(id)
            if (userData != null) {
                runOnUiThread {
                    binding.mainEditPersonName.text = userData.user_name
                }
            }
        }.start()

        initView()

        initPlay()
        //初始化点击事件
        initOnClickListener()

        //折线图
        initLineChart()

        //初始化语言播放器
        initVoice()

        //串口初始化
        initSerial()

        initSerial2()

        initSocket()

        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                if(qNum-qNumThem<50){
                    binding.mainIvGunType.setImageResource(R.mipmap.q2)
                    isType=true
                }
                qNumThem=qNum
                if(bNum-bNumThem<50){
                    binding.mainLo.setBackgroundResource(R.mipmap.group38)
                    isType2=true
                }
                bNumThem=bNum

            }
        }
        timer.schedule(task, 0, 3000) // 每秒执行一次任务

    }

    var qNumThem=0
    var bNumThem=0

    var usbDevice: USBDeviceParcelable? = null
    private fun initView() {
        usbDevice = intent.getParcelableExtra("usbDevice")
        thermalService = ThermalService(handler, this@MainActivity)
        thermalService?.usbConns(usbDevice?.usbDevice)


    }

    /**
     * 语音播放设置
     */
    private fun initVoice() {
        if (!SpeechUtils.IsInit()) {
            SpeechUtils.getInstance(this@MainActivity)
        }
    }

    private var socketClient: SocketClient? = null

    private var isSocketClient = false

    private fun initSocket() {
        val defaultMMKV = MMKV.defaultMMKV()
        val setting_ip = defaultMMKV?.decodeString("dialog_setting_ip")
        if (setting_ip != null && setting_ip != "") {
            Constant.ip = setting_ip
        } else {
            defaultMMKV?.encode("dialog_setting_ip", Constant.ip)
        }



        socketClient = SocketClient.getInstance()
        socketClient?.connect(Constant.ip, Constant.port_int)

        socketClient?.setOnDataReceiveListener(object : OnDataReceiveListener {
            //连接成功
            override fun onConnectSuccess() {
                sendSocketBasic()
                isSocketClient = true
            }

            //连接失败
            override fun onConnectFail() {
                isSocketClient = false
            }

            override fun onSocketCOntent(msg: String?) {

            }

            //接受信息
            override fun onDataReceive(buffer: ByteArray, size: Int, requestCode: Int) {
                val s: String = byteToString(buffer)
                Log.e(TAG, "onDataReceive: $s:")
            }

        })


    }

    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.e(TAG, msg.what.toString())
            when (msg.what) {
                CString.usb_Connected -> {
                    Log.e(TAG, "USB已连接")
                }

                CString.usb_Connecting -> {
                    Log.e(TAG, "USB连接中")
                }

                CString.usb_DisConnected -> {
                    Log.e(TAG, "USB连接断开")
                }

                CString.usb_ReadData -> {
                    val usbReadData = msg.data.getString(CString.reData) //接受msg传递过来的参数
                    Log.e(TAG, "USB数据$usbReadData")
                }

                100 -> {
                    ToastUtils.show("开始考试")
                    startActivity(Intent(this@MainActivity, Open2FaceActivity::class.java))
                    finish()
                }
            }

        }
    }

    data class PersonData(
        val name: String,
        val img: String
    )

    /**
     * eventBus接收器
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DeleteEvent?) {
        val msg = event?.msg
        val personData = Gson().fromJson(msg, PersonData::class.java)
        val defaultMMKV = MMKV.defaultMMKV()
        defaultMMKV?.encode("Name", personData.name)
        binding.mainEditPersonName.text = personData.name

    }


    fun byteToString(data: ByteArray): String {
        var index = data.size
        for (i in data.indices) {
            if (data[i].toInt() == 0) {
                index = i
                break
            }
        }
        val temp = ByteArray(index)
        Arrays.fill(temp, 0.toByte())
        System.arraycopy(data, 0, temp, 0, index)
        var str = ""
        try {
            str = String(temp)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return ""
        }
        return str
    }

    private var ipAddress: String? = null

    override fun onResume() {
        super.onResume()
        initData()
        startService(Intent(this, SocketServerService::class.java))
    }


    /**
     * 向总控发送基本信息
     */
    fun sendSocketBasic() {
        ipAddress = NetWorkUtils.getLocalIpAddress(this)
        val infoEntity = InfoEntity()
        infoEntity.mainTextJuId = binding.mainTextJuId.text.toString()
        infoEntity.no = binding.mainLo.text.toString()
        infoEntity.ip = ipAddress
        infoEntity.bullets = "10"
        infoEntity.personName = binding.mainEditPersonName.text.toString()
        infoEntity.type = "1"
        infoEntity.main_text_all_num = binding.mainTextAllNum.text.toString()
        infoEntity.main_text_gun_type = binding.mainTextGunType.text.toString()
        infoEntity.main_text_surplus_num = binding.mainTextSurplusNum.text.toString()
        infoEntity.isStart = false
        try {
            socketClient?.sendStrSocket(Gson().toJson(infoEntity))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var sp: SoundPool =
        SoundPool(4, AudioManager.STREAM_MUSIC, 0) // 创建SoundPool对象  // 声明SoundPool的引用
    private var hm: HashMap<Int, Int> = HashMap()  // 声明一个HashMap来存放声音文件

    private fun initPlay() {
        try {
            hm[1] = sp.load(assets.openFd("open92.mp3"), 1)
            hm[2] = sp.load(assets.openFd("open95.mp3"), 1)
            hm[3] = sp.load(assets.openFd("shot92.mp3"), 1)
            hm[4] = sp.load(assets.openFd("shot95.mp3"), 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 播放声音
     */
    private fun playSound(sound: Int, loop: Int, volume: Float) {
        if (sp != null) {
            sp.play(hm[sound]!!, volume, volume, 1, loop, 1.0f)

        }
    }


    var setChart: CreationChart? = null
    private fun initLineChart() {
        setChart = CreationChart(binding.mainChart,"瞄准折.线图");
        setChart?.init();
    }




    /**
     * 第一次进来进行初始化
     * 波特率设为9600，并进行设置其他信息
     *
     * 之后再打开波特率为115200
     */
    var str95 = ""
    var str92 = ""
    var isType = true
    var qNum=0
    private fun initSerial() {
        SerialManage.getInstance().init(object : SerialInter {
            override fun connectMsg(path: String?, isSucc: Boolean) {
                val msg = if (isSucc) "成功" else "失败"
                Log.e("枪数据接受", "串口 " + attr.path + " -连接" + msg)
            }

            override fun readData(path: String?, bytes: ByteArray?, size: Int) {
                val strthem = ByteUtil.bytes2HexStr(bytes)
                val str = strthem.substring(0, size * 2)
                Log.e(TAG, "枪数据接受————: $str")
                if (isType){
                    binding.mainIvGunType.setImageResource(R.mipmap.q)
                    isType=false
                }
                qNum++
                if (str.contains("01AA55")) {
                    val split = str.split("01AA55")
                    if (str.length > 40) {
                        ToastUtils.show("系统错误，请重启设备")
                    }
                    split.forEach {
                        if (it != "") {
                            var qStr = "01AA55$it"
                            if (qStr.length > 16) {
                                val substring = qStr.substring(0, 16)
                                val sub5 = qStr.substring(8, 10)
                                val emitterType = ByteUtil.hexStr2decimal(sub5)
                                if (emitterType == 95) {
                                    if (str95 != substring) {
                                        try {
                                            Thread {
                                                getOpenEmitter(substring)
                                            }.start()
                                        } catch (o: Exception) {
                                            Log.e(TAG, "枪数据接受: 枪掉线: $o")
                                        }
                                    }
                                    str95 = substring
                                } else if (emitterType == 92) {
                                    if (str92 != substring) {
                                        try {
                                            Thread {
                                                getOpenEmitter(substring)
                                            }.start()
                                        } catch (o: Exception) {
                                            Log.e(TAG, "枪数据接受: 枪掉线: $o")
                                        }
                                    }
                                    str92 = substring
                                }


                            }

                        }
                    }
                }

            }
        })//串口初始化

        val decodeInt = MMKV.defaultMMKV()?.decodeInt("SerialFirst")
        if (decodeInt == 0) {
            MMKV.defaultMMKV()?.encode("SerialFirst", 1)
            Thread {
                val db = Room.databaseBuilder(
                    MyApp.getApplication(),
                    AppDatabase::class.java, "users_dp"
                ).build()
                val userDao = db.userDao()
                userDao.insertAll(User(user_name = "学员", password = "123456", path = ""))
                val defaultMMKV = MMKV.defaultMMKV()
                defaultMMKV?.encode("Name", "学员")

                SerialManage.getInstance().open(Constant.CHANNEL1, Constant.BAUDRATE_9600)//打开
                SerialManage.getInstance().send(Constant.MODE_START)

                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.UART)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.RFBR)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.PWR)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.RFCH)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.PID)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.MODE_END)
            }.start()

        } else {
            SerialManage.getInstance().open(Constant.CHANNEL1, Constant.BAUDRATE_115200);//打开

        }

    }

    /**
     * 接受发射器信号
     * 枪逻辑处理
     */

    var themTime: Long = 0
    var juId = ""
    var bulletNumberThem = 128
    var qiangType = ""
    val listxyThem = mutableListOf<PaintXY>()

    var parseIntThem = 0

    @SuppressLint("NewApi", "SetTextI18n", "SimpleDateFormat")
    private fun getOpenEmitter(str: String) {
        val sub1 = str.substring(0, 2)
        val sub2 = str.substring(2, 4)
        val sub3 = str.substring(4, 6)
        val sub4 = str.substring(6, 8)
        val sub5 = str.substring(8, 10)
        val sub6 = str.substring(10, 12)
        val sub8 = str.substring(14, 16)
        val makeCheckSum = CalculateCheckDigit.makeCheckSum("$sub1$sub2$sub3$sub4$sub5$sub6")
        //子弹数量
        val bulletNum = ByteUtil.hexStr2decimal(sub6)

        val listXY = mutableListOf<PaintXY>()
        listXY.addAll(listxyThem)
        listxyThem.clear()
        //校验和
        if (makeCheckSum.equals(sub8, ignoreCase = true)) {
            // >=8为已上膛

            // 0为默认值 打一枪+1
            //枪型
            val emitterType = ByteUtil.hexStr2decimal(sub5)

            var toBinaryString = Integer.toBinaryString(Integer.parseInt(sub4, 16))

            if (toBinaryString.length < 8) {
                val i1 = 8 - toBinaryString.length
                for (i in 0 until i1) {
                    toBinaryString = "0$toBinaryString"
                }
            }

            val parseInt = Integer.parseInt(toBinaryString.substring(2, toBinaryString.length), 2)
            Log.e(TAG, "$toBinaryString 枪数据接受————: $parseInt")
            setType(toBinaryString, parseInt, bulletNum, emitterType, listXY)

        }
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setType(
        toBinaryString: String,
        parseInt: Int,
        bulletNum: Int,
        emitterType: Int,
        listXY: MutableList<PaintXY>
    ) {
        if (parseInt == 0 && toBinaryString.substring(0, 1) == "1") {
            setChart?.clear()

            parseIntThem = 0
            bulletNumberThem = parseInt
            runOnUiThread {
                binding.mainTextSurplusNum.text = "子弹数 $bulletNum"
            }
            listXYLian.clear()
            binding.mainTargetView.isPlayback = false
            isDan = true
            runOnUiThread {
                list.clear()
                mainAdapter?.setList(list)
            }
            if (emitterType == 95) {
                qiangType = "95"
                playSound(2, 0, 1.0f)
            } else {
                qiangType = "92"
                playSound(1, 0, 1.0f)
            }
            //保存局数据
            binding.mainTargetView.reset()
            themTime = System.currentTimeMillis()
            savaJu(emitterType, bulletNum)
        } else if (parseInt > 0 && isDan && parseInt - parseIntThem >= 1) {
            setChart?.clear()

            parseIntThem = parseInt
            if (parseInt == bulletNum) {
                isDan = false
            }
            Log.e(TAG, "枪数据接受: 第$parseInt 发")

            var mainTextAllNum = MMKV.defaultMMKV()?.decodeInt("mainTextAllNum")
            MMKV.defaultMMKV()?.encode("mainTextAllNum", (mainTextAllNum!! + 1))
            runOnUiThread {
                binding.mainTextAllNum.text = "总发数 ${mainTextAllNum!! + 1}"
                binding.mainTextSurplusNum.text = "子弹数 $bulletNum"
            }

            //模拟器击发
            //emitterType 95式=4 92式=3
            if (emitterType == 95) {
                playSound(4, 0, 1.0f)
            } else {
                playSound(3, 0, 1.0f)
            }
            bulletNumberThem = parseInt

            val db = Room.databaseBuilder(
                MyApp.getApplication(),
                AppDatabase::class.java, "users_dp"
            ).build()
            if (listXYLian.isEmpty()) {
                listXYLian.add(PaintXY(0f, 0f))
            }

            var direction = binding.mainTargetView.getViewDirection(
                listXYLian[listXYLian.size - 1].x,
                listXYLian[listXYLian.size - 1].y
            )

            val themTime2 = (System.currentTimeMillis() - themTime) / 1000
            themTime = System.currentTimeMillis()
            val currentTimeMillis =
                System.currentTimeMillis().toString() + "" + nextInt(1000)
            val curDate = Date(System.currentTimeMillis()) //获取当前时间
            val formatter = SimpleDateFormat("YYYY-MM-dd HH:mm:ss") //设置时间格式
            val createDate = formatter.format(curDate) //格式转换

            //环数
            var ringNumber = binding.mainTargetView.getViewRingNumber(
                listXYLian[listXYLian.size - 1].x,
                listXYLian[listXYLian.size - 1].y
            )

            if (listXY.isEmpty()) {
                if (listXYLian[listXYLian.size - 1].x == 0f) {
                    listXY.add(PaintXY(0f, 0f))
                    SpeechUtils.SpeakVoice2("脱靶")
                } else {
                    val paintXY = listXYLian[listXYLian.size - 1]
                    ringNumber = binding.mainTargetView.getViewRingNumber(
                        listXYLian[listXYLian.size - 1].x,
                        listXYLian[listXYLian.size - 1].y
                    )
                    if (ringNumber[0] != 10.9) {
                        val x =
                            ImgXYCalc.getRandomFloat(paintXY.x - 0.01f, paintXY.x + 0.01f)
                        val y =
                            ImgXYCalc.getRandomFloat(paintXY.y - 0.01f, paintXY.y + 0.01f)

                        listXY.add(PaintXY(x, y))
                    } else {
                        listXY.add(paintXY)
                    }

                    if (ringNumber[0] == 0.0) {
                        SpeechUtils.SpeakVoice2("脱靶")
                    } else {
                        SpeechUtils.SpeakVoice2(ringNumber[0].toString() + "环")
                    }
                }

            } else {
                if(listXY.isEmpty()){
                    return
                }
                val paintXY = listXY[listXY.size - 1]
                ringNumber = binding.mainTargetView.getViewRingNumber(
                    listXY[listXY.size - 1].x,
                    listXY[listXY.size - 1].y
                )
                if (ringNumber[0] != 10.9) {
                    val x = ImgXYCalc.getRandomFloat(paintXY.x - 0.01f, paintXY.x + 0.01f)
                    val y = ImgXYCalc.getRandomFloat(paintXY.y - 0.01f, paintXY.y + 0.01f)
                    listXY.add(PaintXY(x, y))

                } else {
                    listXY.add(paintXY)
                }
                Log.e(
                    TAG,
                    "随机坐标: ${listXY[listXY.size - 1].x}    ${listXY[listXY.size - 1].y}"
                )
                direction = binding.mainTargetView.getViewDirection(
                    listXY[listXY.size - 1].x,
                    listXY[listXY.size - 1].y
                )
                if (ringNumber[0] == 0.0) {
                    SpeechUtils.SpeakVoice2("脱靶")
                } else {
                    if (ringNumber[0] == 10.9) {
                        direction = 100
                        SpeechUtils.SpeakVoice2(ringNumber[0].toString() + "环")
                    } else {
                        SpeechUtils.SpeakVoice2(ringNumber[0].toString() + "环")
                    }
                }
            }


            val analysisAchieve = AnalysisAchievementUtils2.setEvement(
                listXY,
                ringNumber!![0],binding.mainTargetView
            )

            runOnUiThread {
                binding.mainTargetView.setDanListxy(listXY[listXY.size - 1])
                binding.mainTargetView.setDanRest()

                binding.mainTextShootNum.text = parseInt.toString()
                binding.mainTextCurrentRingnum.text = ringNumber[0].toString()
                binding.mainProgressHoldingGuntBar.setAnimProgress(
                    analysisAchieve[0],
                    "据枪\n"
                )
                binding.mainProgressAimBar.setAnimProgress(analysisAchieve[1], "瞄准\n")
                binding.mainProgressFiringBar.setAnimProgress(analysisAchieve[2], "击发\n")
                binding.mainProgressAchievementGuntBar.setAnimProgress(
                    analysisAchieve[3],
                    "成绩\n"
                )
                binding.mainProgressTotalityBar.setAnimProgress(
                    analysisAchieve[4],
                    "总体\n"
                )
            }

            val bulletDao = db.bulletDao()
            val bulletBean = BulletBean(
                currentTimeMillis.toLong(),
                juId.toLong(),
                parseInt,
                ringNumber[0],
                direction,
                "${themTime2.toString()} 秒",
                createDate,
                listXY[listXY.size - 1].x,
                listXY[listXY.size - 1].y,
                analysisAchieve[0],
                analysisAchieve[1],
                analysisAchieve[2],
                analysisAchieve[3],
                analysisAchieve[4]
            )
            bulletDao.insertAll(bulletBean)
            list.add(bulletBean)
            runOnUiThread {
                mainAdapter?.setList(list)
                binding.mainRecycler.smoothScrollToPosition(list.size - 1)
            }

            //保存轨迹
            val trackDao = db.trackDao()
            for (i in listXY.indices) {
                val trackBean = TrackBean(
                    null,
                    currentTimeMillis.toLong(),
                    listXY[i].x,
                    listXY[i].y
                )
                trackDao.insertAll(trackBean)
            }

            if (listXYLian.isNotEmpty()) {
                for (i in 0 until 10) {
                    try {
                        val trackBean = TrackBean(
                            null,
                            "${currentTimeMillis}2".toLong(),
                            listXYLian[listXYLian.size - (10 - i)].x,
                            listXYLian[listXYLian.size - (10 - i)].y
                        )
                        trackDao.insertAll(trackBean)
                    } catch (e: Exception) {
                        Log.e(TAG, "枪数据接受: 灰色数据保存失败")
                    }
                }
            }

            //保存局
            val bureauDao = db.bureauDao()
            var total_ring_number = 0.0

            for (element in list) {
                total_ring_number += element.cylinder_number!!
            }
            val defaultMMKV = MMKV.defaultMMKV()
            val decodeInt = defaultMMKV?.decodeInt("num")

            val calendar = Calendar.getInstance()
            calendar.time = curDate

            //socket发送子弹数据
            val bulletBean2 = BulletBean2()
            bulletBean2.ip = ipAddress
            bulletBean2.x = listXY[listXY.size - 1].x
            bulletBean2.y = listXY[listXY.size - 1].y
            bulletBean2.ju_qiang = analysisAchieve[0]
            bulletBean2.miao_zhun = analysisAchieve[1]
            bulletBean2.ji_fa = analysisAchieve[2]
            bulletBean2.cheng_ji = analysisAchieve[3]
            bulletBean2.zong_ti = analysisAchieve[4]
            bulletBean2.cylinder_number = ringNumber[0]
            bulletBean2.cylinder_number2 = "%.1f".format(total_ring_number).toDouble()
            bulletBean2.direction = direction
            bulletBean2.data_time = "$themTime2 秒"
            bulletBean2.time = createDate
            bulletBean2.number = parseInt
            try {
                socketClient?.sendStrSocket(Gson().toJson(bulletBean2))
            } catch (e: Exception) {
                e.printStackTrace()
            }


            val userDao = db.userDao()
            val findByName = userDao.findByName(binding.mainEditPersonName.text.toString())
            val findBureauId = bureauDao.findById(juId.toLong()) ?: return

            findBureauId.total_ring_number = "%.1f".format(total_ring_number).toDouble()
            bureauDao.update(findBureauId)
            runOnUiThread {
                binding.maintextAllJuNum.text = "%.1f".format(total_ring_number)
                binding.mainTextJuId.text = decodeInt.toString()
            }

            if (parseInt == findBureauId.sum_up_send) {
                isDan = false
                SpeechUtils.SpeakVoice2(
                    " 、${ringNumber[0].toString() + "环"} 。 成绩报告。总发数。${list.size}发。总环数，${
                        "%.1f".format(
                            total_ring_number
                        ).toDouble()
                    } 环"
                )
//
                val defaultMMKV = MMKV.defaultMMKV()
                val setting_print = defaultMMKV?.decodeBool("dialog_setting_print")
                if (setting_print!!) {
                    try {
                        refresh()
                    } catch (e: Exception) {
                        Log.e(TAG, "getOpenEmitter: $e")
                    }
                }
            }
        }

    }


    fun refresh() {
        /**
         * 打印当前成绩
         */
        Log.e(TAG, "枪数据接受: 开始打印")
        Thread {
            val db = Room.databaseBuilder(
                MyApp.getApplication(),
                AppDatabase::class.java, "users_dp"
            ).build()

            val bureauBean = db.bureauDao().findById(juId.toLong())
            val listBullet = db.bulletDao().findBulletList(bureauBean.uid!!)
            if (listBullet.isNotEmpty()) {

                var string =
                    "射手姓名 :  " + binding.mainEditPersonName.text.toString() + "\n" +
                            "局    ID :  " + binding.mainTextJuId.text.toString() + "\n" +
                            "枪    型 :  " + bureauBean.qiang_id + " \n" +
                            "总 环 数 :  " + bureauBean.total_ring_number + " \n" +
                            "时    间 :  " + bureauBean.data_time + " \n" +
                            "\n" +
                            "\n" +
                            "发序 | 环数 | 方向 | 用时  \n"

                thermalService?.usbSendDatas(BrightekCommandM.t1b63(2))
                thermalService?.usbSendDatas(string.toByteArray(charset("gbk")))
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
                    thermalService?.usbSendDatas(string1.toByteArray(charset("gbk")))
                }

                val usbView = USBView(this, listBullet)
                val bitmapByScorw: Bitmap = BItmaoUtils.getBitmapByScorw(usbView)
                thermalService?.usbSendDatas(BrightekCommandM.t1b61(0)) //居中
                thermalService?.usbSendDatas(
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

                thermalService?.usbSendDatas(BrightekCommandM.t1b63(2))
                thermalService?.usbSendDatas(assessString.toByteArray(charset("gbk")))

                thermalService?.usbSendDatas(BrightekCommandM.t1b4a(80))
            }

        }.start()


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

    /**
     * 00 有照射 没开枪
     * 01 有照射 开枪
     * 02 错误ID
     * 03 格式错误
     * 04 无激光照射
     */
    private var lztek: Lztek? = null
    private var isType2=true
    var bNum=0
    private fun initSerial2() {
        SerialManage2.getInstance().init(object : SerialInter {
            override fun connectMsg(path: String?, isSucc: Boolean) {
                val msg = if (isSucc) "成功" else "失败"
                Log.e("串口连接回调", "串口 " + attr.path + " -连接" + msg)
            }

            override fun readData(path: String?, bytes: ByteArray?, size: Int) {

                val strthem = ByteUtil.bytes2HexStr(bytes)
                val str = strthem.substring(0, size * 2)
                Log.e(TAG, "靶数据接受: $str")
                if (isType2){
                    binding.mainLo.setBackgroundResource(R.mipmap.group37)
                    isType2=false
                }
                bNum++
                try {
                    getOpenTarget(str)
                } catch (o: Exception) {

                }


            }
        })
        SerialManage2.getInstance().open(Constant.CHANNEL2, Constant.BAUDRATE_115200)
        // 设置lo模块
        lztek = Lztek.create(this@MainActivity)
        val ethEnable = lztek?.ethEnable
//        Log.e(TAG, "initSerial2:ethEnable= $ethEnable")
        // 设置高电平
        Thread.sleep(20)
        val b = lztek?.gpioEnable(Constant.k8)
        Thread.sleep(20)
        lztek?.setGpioOutputMode(Constant.k8)
        Thread.sleep(20)
        lztek?.setGpioValue(Constant.k8, 0)
        Log.e(TAG, "靶串口通讯设置:b= $b")


    }


    var isDan = false
    val listXYLian = mutableListOf<PaintXY>()

    /**
     * 接受靶信号 AA55 07 19 01 15 000135
     */
    var chongfu = 0
    private fun getOpenTarget(str: String) {
        val sub1 = str.substring(0, 2)
        val sub2 = str.substring(2, 4)
        val sub3 = str.substring(4, 6)
        val sub4 = str.substring(6, 8)
        val sub5 = str.substring(8, 10)
        val sub6 = str.substring(10, 12)
        val sub7 = str.substring(12, 14)
        val sub8 = str.substring(14, 16)
        val sub9 = str.substring(16, 18)
        /**
         * 00 有照射 没开枪
         * 01 有照射 开枪
         * 02 错误ID
         * 03 格式错误
         * 04 无激光照射
         */
        if (chongfu < 3) {
            if (sub7 == "02" || sub7 == "03" || sub7 == "04") {
                chongfu++
                return
            }
        }
        chongfu = 0

        val makeCheckSum = CalculateCheckDigit.makeCheckSum("$sub1$sub2$sub3$sub4$sub5$sub6$sub7")
        val makeCheckSum2 = CalculateCheckDigit.makeCheckSum("$sub1$sub2$sub3$sub4$sub5$sub6")

        if (makeCheckSum.equals(sub9, ignoreCase = true) || makeCheckSum2.equals(
                sub9,
                ignoreCase = true
            )
        ) {
            //轨迹
            val saveXY = mutableListOf<PaintXY>()
            //XXX YYY为float 不大于1
            val top = ByteUtil.hexStr2decimal(sub3)
            val bottom = ByteUtil.hexStr2decimal(sub4)
            val left = ByteUtil.hexStr2decimal(sub5)
            val right = ByteUtil.hexStr2decimal(sub6)
            val xxx = ((top.toFloat() + bottom.toFloat()) / 2) / 25
            val yyy = ((left.toFloat() + right.toFloat()) / 2) / 24

            var xxxx = 0f
            var yyyy = 0f
            if (xxx != 0f && yyy != 0f) {
                xxxx = 1 - xxx
                yyyy = 1 - yyy
            }
//                Log.e(TAG, "靶数据接受: $str")
//                Log.e(TAG, "靶数据接受: x= $xxxx     y=$xxxx")
            val paintXY = PaintXY(xxxx, yyyy)
            listXYLian.add(paintXY)
            listxyThem.add(paintXY)
            saveXY.add(paintXY)
            runOnUiThread {
                if (isDan) {
                    binding.mainTargetView.setListxy(paintXY)
                    var ringNumber = binding.mainTargetView.getViewRingNumber(
                        xxxx,
                        yyyy
                    )
                    if (listXYLian.size%20==0){
                        setChart?.AddData(ringNumber[0].toFloat())
                        Log.e(TAG, "靶折线图: ${ringNumber[0].toFloat()}")
                    }
                }
                //socket发送轨迹
                val trackBean2 = TrackBean2()
                trackBean2.ip = ipAddress
                trackBean2.x = xxxx
                trackBean2.y = yyyy

                try {
                    socketClient?.sendStrSocket(Gson().toJson(trackBean2))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

    }


    /**
     * 保存局
     */
    @SuppressLint("NewApi", "SetTextI18n", "SimpleDateFormat", "WeekBasedYear")
    private fun savaJu(emitterType: Int, bulletNum: Int) {
        juId = System.currentTimeMillis().toString() + "" + nextInt(1000)
        val curDate = Date(System.currentTimeMillis()) //获取当前时间
        val formatter = SimpleDateFormat("YYYY-MM-dd HH:mm:ss") //设置时间格式
        val createDate = formatter.format(curDate) //格式转换
        val calendar = Calendar.getInstance()
        calendar.time = curDate
        val db = Room.databaseBuilder(
            MyApp.getApplication(),
            AppDatabase::class.java, "users_dp"
        ).build()
        val defaultMMKV = MMKV.defaultMMKV()
        val decodeInt = defaultMMKV?.decodeInt("num")
        defaultMMKV?.encode("num", (decodeInt!! + 1))

        runOnUiThread {
            binding.mainTextJuId.text = (decodeInt!! + 1).toString()
            binding.mainTextGunType.text = "枪型 ${emitterType.toString()}"
        }
        Thread {
            val userDao = db.userDao()
            val findByName = userDao.findByName(binding.mainEditPersonName.text.toString())

            val bureauDao = db.bureauDao()
            val bureauBean = BureauBean(
                juId.toLong(),
                decodeInt!! + 1,
                findByName.uid,
                0.0,
                bulletNum,//子弹数
                emitterType.toString(),//枪型
                "1",
                createDate,
                "50",
                calendar.get(Calendar.YEAR).toString(),
                (calendar.get(Calendar.MONTH) + 1).toString(),
                calendar.get(Calendar.DAY_OF_MONTH).toString()
            )
            try {
                bureauDao.insertAll(bureauBean)
                ipAddress = NetWorkUtils.getLocalIpAddress(this)
                socketClient?.sendStrSocket("{\"BureauBean\":100,\"ip\":\"$ipAddress\"}")
            } catch (e: Exception) {
                Log.e(TAG, "保存局异常: ${e.toString()}")
            }

        }.start()
    }


    private var mainAdapter: MainAdapter? = null
    private val list = mutableListOf<BulletBean>()


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initData() {
        binding.mainProgressHoldingGuntBar.setAnimProgress(0, "据枪\n")
        binding.mainProgressAimBar.setAnimProgress(0, "瞄准\n")
        binding.mainProgressFiringBar.setAnimProgress(0, "击发\n")
        binding.mainProgressAchievementGuntBar.setAnimProgress(0, "成绩\n")
        binding.mainProgressTotalityBar.setAnimProgress(0, "总体\n")

        //枪号
        val defaultMMKV = MMKV.defaultMMKV()
        val number = defaultMMKV?.decodeInt("NUMBER")
        binding.mainLo.text = "${number!! + 1}"
        val name = defaultMMKV.decodeString("Name")
        if (name != null && name != "") {
            binding.mainEditPersonName.text = name
        }

        //局数
        val num = defaultMMKV.decodeInt("num")
        if (num == 0) {
            binding.mainTextJuId.text = "${num + 1}"
        } else {
            binding.mainTextJuId.text = "$num"
        }

        val layoutManager = LinearLayoutManager(this)
        binding.mainRecycler.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        mainAdapter = MainAdapter(mutableListOf())
        binding.mainRecycler.adapter = mainAdapter
        mainAdapter?.setList(list)
        //点击回放
        mainAdapter?.setOnItemClickListener { adapter, view, position ->
            if (!binding.mainTargetView.isPlayback) {
                mainAdapter?.selectPosition = position
                mainAdapter?.notifyDataSetChanged()
                binding.mainTargetView.reset()
                isDan = false
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "users_dp"
                ).build()

                Thread {
                    val trackDao = db.trackDao()
                    val findBulletList = trackDao.findByTrack(list[position].uid!!)
                    var uidtoString = "${list[position].uid.toString()}2"
                    val findBulletList2 = trackDao.findByTrack(uidtoString.toLong())

                    findBulletList.forEach {
                        Thread.sleep(50)
                        runOnUiThread {
                            binding.mainTargetView.setListxy(
                                PaintXY(
                                    it.track_point_x!!,
                                    it.track_point_y!!
                                ), findBulletList.size
                            )
                        }
                        if (findBulletList[findBulletList.size - 1].uid == it.uid) {

                            runOnUiThread {
                                binding.mainTargetView.setPlayback(
                                    PaintXY(
                                        it.track_point_x!!,
                                        it.track_point_y!!
                                    )
                                )
                            }

                            findBulletList2.forEach { findBulletList2 ->
                                Thread.sleep(50)
                                runOnUiThread {
                                    binding.mainTargetView.setListxy(
                                        PaintXY(
                                            findBulletList2.track_point_x!!,
                                            findBulletList2.track_point_y!!
                                        ), findBulletList.size
                                    )
                                }
                            }
                            runOnUiThread {
                                isDan = true
                                binding.mainProgressHoldingGuntBar.setAnimProgress(
                                    list[position].ju_qiang!!,
                                    "据枪\n"
                                )
                                binding.mainProgressAimBar.setAnimProgress(
                                    list[position].miao_zhun!!,
                                    "瞄准\n"
                                )
                                binding.mainProgressFiringBar.setAnimProgress(
                                    list[position].ji_fa!!,
                                    "击发\n"
                                )
                                binding.mainProgressAchievementGuntBar.setAnimProgress(
                                    list[position].cheng_ji!!,
                                    "成绩\n"
                                )
                                binding.mainProgressTotalityBar.setAnimProgress(
                                    list[position].zong_ti!!,
                                    "总体\n"
                                )
                            }
                        }
                    }
                }.start()
            }
        }

    }


    private var mIsPlaying = false // 是否正在播放动画

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun initOnClickListener() {

        //标题头
        binding.mainTitle.setOnClickListener {
            initSocket()
        }

        /**
         * 历史记录
         */
        binding.mainHistory.setOnClickListener {
            startActivity(
                Intent(this, HistoryActivity::class.java).putExtra(
                    "usbDevice",
                    usbDevice
                )
            )
        }

        /**
         * 设置
         */
        binding.mainSetting.setOnClickListener {
            settingMain()
        }


        /**
         * 设置组号枪号
         */
        binding.mainLo.setOnClickListener {
            settingGroup()
        }

        //枪型
        binding.mainTextGunType.setOnClickListener {


        }

        //总发数
        binding.mainTextAllNum.setOnClickListener {


        }

        //更改姓名
        binding.mainEditPersonName.setOnClickListener {
            val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_name, null)
            val name_name = inflate.findViewById<EditText>(R.id.name_name)
            val name_quxiao = inflate.findViewById<TextView>(R.id.name_quxiao)
            val name_queding = inflate.findViewById<TextView>(R.id.name_queding)

            val popupWindow = PopupWindow(this)
            popupWindow.contentView = inflate
            popupWindow.width = 500
            popupWindow.height = 400
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = true
            val lp: WindowManager.LayoutParams = window.attributes
            lp.alpha = 0.7f
            window.attributes = lp
            popupWindow.setOnDismissListener {
                val lp1: WindowManager.LayoutParams = window.attributes
                lp1.alpha = 1f
                window.attributes = lp1
            }

            popupWindow.showAtLocation(binding.mainEditPersonName, Gravity.CENTER, 0, 0)

            name_quxiao.setOnClickListener {
                if (name_name.text.toString() == "") {
                    ToastUtils.show("请输入姓名")
                    return@setOnClickListener
                }

                val users_dp = Room.databaseBuilder(
                    MyApp.getApplication(),
                    AppDatabase::class.java, "users_dp"
                ).build()

                Thread {
                    val userDao = users_dp.userDao()
                    var findByName = userDao.findByName(name_name.text.toString())
                    if (findByName == null) {
                        val user = User()
                        user.user_name = name_name.text.toString()
                        user.password = "123456"
                        user.path = ""
                        userDao.insertAll(user)
                    }
                    findByName = userDao.findByName(name_name.text.toString())
                    runOnUiThread {
                        startActivity(
                            Intent(this@MainActivity, OpenFaceActivity::class.java).putExtra(
                                "id",
                                findByName.uid
                            )
                        )
                        popupWindow.dismiss()

                    }
                }.start()


            }
            name_queding.setOnClickListener {
                popupWindow.dismiss()
                startActivity(Intent(this@MainActivity, Open2FaceActivity::class.java))

            }

        }

        binding.mainClose.setOnClickListener {
            finish()
        }

        binding.mainShutDown.setOnClickListener {
            if (System.currentTimeMillis() - mShutDownTime > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次关机", Toast.LENGTH_SHORT).show()
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mShutDownTime = System.currentTimeMillis()
            } else {
                try {
                    Runtime.getRuntime().exec("reboot -p") //关机
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    var mShutDownTime: Long = 0

    /**
     * 设置
     */
    @SuppressLint("MissingInflatedId")
    private fun settingMain() {
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_setting, null)
        val popupWindow = PopupWindow(this)
        popupWindow.contentView = inflate
        popupWindow.width = 500
        popupWindow.height = 300
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = window.attributes
        lp.alpha = 0.7f
        window.attributes = lp
        popupWindow.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = window.attributes
            lp1.alpha = 1f
            window.attributes = lp1

        }
        popupWindow.showAtLocation(binding.mainEditPersonName, Gravity.CENTER, 0, 0)


        val dimiss = inflate.findViewById<AppCompatButton>(R.id.dialog_setting_dimiss)
        val ok = inflate.findViewById<AppCompatButton>(R.id.dialog_setting_ok)
        val dialog_setting_ip = inflate.findViewById<EditText>(R.id.dialog_setting_ip)
        val dialog_setting_play = inflate.findViewById<CheckBox>(R.id.dialog_setting_play)
        val dialog_setting_print = inflate.findViewById<CheckBox>(R.id.dialog_setting_print)
        val dialog_setting_spinner = inflate.findViewById<Spinner>(R.id.dialog_setting_spinner)

        val defaultMMKV = MMKV.defaultMMKV()
        val setting_ip = defaultMMKV?.decodeString("dialog_setting_ip")
        var setting_play = defaultMMKV?.decodeBool("dialog_setting_play")
        var setting_print = defaultMMKV?.decodeBool("dialog_setting_print")
        var setting_spinner = defaultMMKV?.decodeInt("dialog_setting_spinner")

        dialog_setting_play.isChecked = setting_play!!
        dialog_setting_print.isChecked = setting_print!!
        dialog_setting_ip.setText(setting_ip)

        //靶面选择
        dialog_setting_spinner.setSelection(setting_spinner!!)

        dialog_setting_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setting_spinner = position

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        //是否播报
        dialog_setting_play.setOnCheckedChangeListener { compoundButton, b ->
            setting_play = b
        }
        //是否打印
        dialog_setting_print.setOnCheckedChangeListener { compoundButton, b ->
            setting_print = b
        }

        dimiss.setOnClickListener { view1: View? -> popupWindow.dismiss() }

        ok.setOnClickListener {
            popupWindow.dismiss()
            defaultMMKV?.encode("dialog_setting_ip", dialog_setting_ip.text.trim().toString())
            defaultMMKV?.encode("dialog_setting_play", setting_play!!)
            defaultMMKV?.encode("dialog_setting_print", setting_print!!)
            defaultMMKV?.encode("dialog_setting_spinner", setting_spinner!!)
            defaultMMKV?.encode(
                "dialog_setting_spinner",
                dialog_setting_spinner.selectedItemPosition
            )
            when (setting_spinner) {
                0 -> binding.mainTargetView.setImageResource(R.mipmap.xhhdtarget_di)
                1 -> binding.mainTargetView.setImageResource(R.mipmap.xhhdtarget_di_2)
                2 -> binding.mainTargetView.setImageResource(R.mipmap.xhhdtarget_di_3)
                3 -> binding.mainTargetView.setImageResource(R.mipmap.xhhdtarget_di_5)
                4 -> binding.mainTargetView.setImageResource(R.mipmap.xhhdtarget_di_4)
            }


        }
    }

    /**
     * 设置组号枪号
     */
    private fun settingGroup() {
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_loport, null)
        val groupNo = inflate.findViewById<AppCompatSpinner>(R.id.dialog_loport_group_no)
        val gunNo = inflate.findViewById<AppCompatSpinner>(R.id.dialog_loport_gun_no)
        val dimiss = inflate.findViewById<AppCompatButton>(R.id.dialog_loport_dimiss)
        val ok = inflate.findViewById<AppCompatButton>(R.id.dialog_loport_ok)

        val builder = AlertDialog.Builder(this)
        builder.setView(inflate).setTitle("设置组别枪号")
        alertDialog = builder.create()
        alertDialog?.show()
        dimiss.setOnClickListener { view1: View? -> alertDialog?.dismiss() }

        val defaultMMKV = MMKV.defaultMMKV()
        val group = defaultMMKV?.decodeInt("GROUP")
        val number = defaultMMKV?.decodeInt("NUMBER")
        groupNo.setSelection(group!!)
        gunNo.setSelection(number!!)

        ok.setOnClickListener {
            binding.mainLo.text = "${gunNo.selectedItemPosition + 1}"
            defaultMMKV.encode("GROUP", groupNo.selectedItemPosition)
            defaultMMKV.encode("NUMBER", gunNo.selectedItemPosition)
            Thread {
                //设置枪串口
                SerialManage.getInstance().send(Constant.MODE_START)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.At_ALL)
                Constant.RFCH =
                    "AT+RFCH=${(groupNo.selectedItemPosition + 1) * 10 + (gunNo.selectedItemPosition + 1)}\r\n"
                SerialManage.getInstance().send(Constant.RFCH)
                Thread.sleep(100)
                Constant.PID = "AT+PID=${groupNo.selectedItemPosition + 1}\r\n"
                SerialManage.getInstance().send(Constant.PID)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.RFBR)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.PWR)
                Thread.sleep(100)
                SerialManage.getInstance().send(Constant.MODE_END)
                //设置靶串口
                SerialManage2.getInstance().colse()
                // 设置高电平
                val b = lztek?.gpioEnable(Constant.k8)
//                Log.e(TAG, "initSerial2:b= $b")
                if (b!!) {
                    val lztekNum =
                        (groupNo.selectedItemPosition + 1) * 10 + (gunNo.selectedItemPosition + 1)
                    Log.e(TAG, "settingGroup: $lztekNum")
                    lztek?.setGpioOutputMode(Constant.k8)
                    lztek?.setGpioValue(Constant.k8, 1)
                    Thread.sleep(20)
                    SerialManage2.getInstance().open(Constant.CHANNEL2, Constant.BAUDRATE_9600)
                    var sendGpioData =
                        "C00009000108E600" + DataUtils.Completion1(DataUtils.IntToHex(lztekNum))
                            .toString() + "430000"
                    Thread.sleep(20)
                    SerialManage2.getInstance().send(sendGpioData)
                    Thread.sleep(20)
                    SerialManage2.getInstance().colse()
                    Thread.sleep(20)
                    SerialManage2.getInstance().open(Constant.CHANNEL2, Constant.BAUDRATE_115200)
                    Thread.sleep(20)
                    val b2 = lztek?.gpioEnable(Constant.k8)
//                    Log.e(TAG, "initSerial2:b2 $b2")
                    lztek?.setGpioOutputMode(Constant.k8)
                    lztek?.setGpioValue(Constant.k8, 0)
                }

            }.start()
            alertDialog?.dismiss()
        }
    }


    /**
     * 隐藏或显示 导航栏
     *
     * @param activity
     */
    fun setNavigationBarVisible(activity: Activity, isHide: Boolean) {
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


    override fun onDestroy() {
        SerialManage.getInstance().colse()
        SerialManage2.getInstance().colse()
        if (alertDialog != null) alertDialog?.dismiss()
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


}