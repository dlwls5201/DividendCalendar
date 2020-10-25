package com.tistory.dividendcalendar.presentation.calendar.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.domain.model.CalendarItem
import kotlinx.android.synthetic.main.view_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*


class DividendCalendarView : LinearLayout {

    companion object {

        // how many days to show, defaults to six weeks, 42 days
        private const val DAYS_COUNT = 42

        // vertical axis size
        private const val VERTICAL_AXIS_SIZE = 6

        // default date format
        private const val DATE_FORMAT = "MMMM yyyy"
    }

    // date format
    private var dateFormat = DATE_FORMAT

    // current displayed month
    private val currentDate = Calendar.getInstance()

    // dividends
    private val dividendItems = mutableListOf<CalendarItem>()

    //event handling
    private var eventHandler: EventHandler? = null

    // custom view size
    private var viewWidth = 0
    private var viewHeight = 0
    private var barHeight = 0f
    private var headerHeight = 0f

    // seasons' rainbow
    private val rainbow = intArrayOf(
        R.color.summer,
        R.color.fall,
        R.color.winter,
        R.color.spring
    )

    // month-season association (northern hemisphere, sorry australia :)
    private val monthSeason = intArrayOf(2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2)

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initControl(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initControl(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        //int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    /**
     * Load control xml layout
     */
    private fun initControl(
        context: Context,
        attrs: AttributeSet?
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_calendar, this)

        loadDateFormat(attrs)
        assignUiHeaderSize()
        assignClickHandlers()

        updateCalendar()
    }

    private fun loadDateFormat(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DividendCalendarView)
        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.DividendCalendarView_dateFormat) ?: DATE_FORMAT
        } finally {
            ta.recycle()
        }
    }

    private fun assignUiHeaderSize() {
        barHeight = resources.getDimension(R.dimen.calendar_bar_size)
        headerHeight = resources.getDimension(R.dimen.calendar_header_size)
    }

    private var calendarType: CalendarType = CalendarType.PAYMENT_DATE

    enum class CalendarType(val title: String) {
        EX_DATE("배당락일"), PAYMENT_DATE("지급일")
    }

    private fun assignClickHandlers() {
        // add one month and refresh UI
        btnViewCalendarNext.setOnClickListener { v: View? ->
            currentDate.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        // subtract one month and refresh UI
        btnViewCalendarPrev.setOnClickListener { v: View? ->
            currentDate.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        // long-pressing a day
        gvViewCalendar.onItemLongClickListener =
            OnItemLongClickListener { view: AdapterView<*>, cell: View, position: Int, id: Long ->
                // handle long-press
                val date = view.getItemAtPosition(position) as Date

                dividendItems.filter {
                    if (calendarType == CalendarType.EX_DATE) {
                        it.exDate == getDividedDataFormat(date)
                    } else {
                        it.paymentDate == getDividedDataFormat(date)
                    }
                }.let {
                    eventHandler?.onDayLongPress(it)
                }
                true
            }

        gvViewCalendar.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                // handle press
                val date = parent.getItemAtPosition(position) as Date

                dividendItems.filter {
                    if (calendarType == CalendarType.EX_DATE) {
                        it.exDate == getDividedDataFormat(date)
                    } else {
                        it.paymentDate == getDividedDataFormat(date)
                    }
                }.let {
                    eventHandler?.onDayPress(it)
                }
            }

        // change calendar type
        tvCalendarType.text = calendarType.title
        tvCalendarType.setOnClickListener {
            if (calendarType == CalendarType.EX_DATE) {
                calendarType = CalendarType.PAYMENT_DATE
                tvCalendarType.text = calendarType.title
                updateCalendar()
            } else {
                calendarType = CalendarType.EX_DATE
                tvCalendarType.text = calendarType.title
                updateCalendar()
            }
        }
    }


    /**
     * Display dates correctly in grid
     */
    fun updateCalendar(items: List<CalendarItem>) {
        Dlog.d("items : $items")
        dividendItems.clear()
        dividendItems.addAll(items)
        updateCalendar()
    }

    /**
     * Display dates correctly in grid
     */
    private fun updateCalendar() {
        val cells = ArrayList<Date>()
        val calendar = currentDate.clone() as Calendar

        // determine the cell for current month's beginnings
        calendar[Calendar.DAY_OF_MONTH] = 1
        val monthBeginningCell = calendar[Calendar.DAY_OF_WEEK] - 1
        //Dlog.v("현재 월의 날짜 : " + calendar.get(Calendar.DAY_OF_MONTH));
        //Dlog.v("현재 요일 : " + calendar.get(Calendar.DAY_OF_WEEK));
        //Dlog.e("monthBeginningCell : " + monthBeginningCell);

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)
        //Dlog.e("after 현재 월의 날짜 : " + calendar.get(Calendar.DAY_OF_MONTH));

        // fill cells
        while (cells.size < DAYS_COUNT) {
            cells.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // update grid
        gvViewCalendar.adapter = CalendarAdapter(context, cells)

        // update title
        val sdf = SimpleDateFormat(dateFormat)
        tvViewCalendarTitle.text = sdf.format(currentDate.time)

        // set header color according to current season
        val month = currentDate[Calendar.MONTH]
        val season = monthSeason[month]
        val color = rainbow[season]
        llViewCalendarHeader.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    private val childViewHeight: Float
        get() = (viewHeight - barHeight - headerHeight) / VERTICAL_AXIS_SIZE

    /**
     * Assign event handler to be passed needed events
     */
    fun setEventHandler(eventHandler: EventHandler?) {
        this.eventHandler = eventHandler
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    interface EventHandler {
        fun onDayLongPress(items: List<CalendarItem>)
        fun onDayPress(items: List<CalendarItem>)
    }

    private val today = Date()

    private inner class CalendarAdapter(
        context: Context,
        days: ArrayList<Date>
    ) : ArrayAdapter<Date>(context, R.layout.item_view_calendar, days) {

        // for view inflation
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {

            var mView = view

            // inflate item if it does not exist yet
            if (mView == null) {
                mView = inflater.inflate(R.layout.item_view_calendar, parent, false)
            }

            mView?.run {
                val flItemViewCalendarParent: FrameLayout =
                    findViewById(R.id.flItemViewCalendarParent)
                val clItemViewCalendar: ConstraintLayout = findViewById(R.id.clItemViewCalendar)
                val tvItemViewCalendar: TextView = findViewById(R.id.tvItemViewCalendar)

                val ivItemViewCalendarLogo1: ImageView = findViewById(R.id.ivItemViewCalendarLogo1)
                val tvItemViewCalendarCompanyName1: TextView =
                    findViewById(R.id.tvItemViewCalendarCompanyName1)

                val ivItemViewCalendarLogo2: ImageView = findViewById(R.id.ivItemViewCalendarLogo2)
                val tvItemViewCalendarCompanyName2: TextView =
                    findViewById(R.id.tvItemViewCalendarCompanyName2)

                val llItemViewCalendarParent3: LinearLayout =
                    findViewById(R.id.llItemViewCalendarParent3)
                llItemViewCalendarParent3.visibility = View.GONE

                // set the child height according to the parent height.
                flItemViewCalendarParent.layoutParams = FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    childViewHeight.toInt()
                )

                // clear styling
                tvItemViewCalendar.setTypeface(null, Typeface.NORMAL)
                tvItemViewCalendar.setTextColor(Color.BLACK)

                val item = getItem(position) ?: return@run

                val day = item.date
                val month = item.month
                val year = item.year

                // today
                val currentYear = currentDate.get(Calendar.YEAR)
                val currentMonth = currentDate.get(Calendar.MONTH)

                if (month != currentMonth) {
                    clItemViewCalendar.visibility = View.INVISIBLE
                } else {
                    clItemViewCalendar.visibility = View.VISIBLE
                }

                if (month == today.month && day == today.date) {
                    tvItemViewCalendar.setTypeface(null, Typeface.BOLD)
                    flItemViewCalendarParent.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_02
                        )
                    )
                }

                // set text
                tvItemViewCalendar.text = item.date.toString()

                dividendItems.filter {
                    if (calendarType == CalendarType.EX_DATE) {
                        it.exDate == getDividedDataFormat(item)
                    } else {
                        it.paymentDate == getDividedDataFormat(item)
                    }
                }.forEachIndexed { index, dividendItem ->
                    when (index) {
                        0 -> {
                            Glide.with(context)
                                .load(dividendItem.logoUrl)
                                .into(ivItemViewCalendarLogo1)

                            tvItemViewCalendarCompanyName1.text = dividendItem.companyName
                        }
                        1 -> {
                            Glide.with(context)
                                .load(dividendItem.logoUrl)
                                .into(ivItemViewCalendarLogo2)

                            tvItemViewCalendarCompanyName2.text = dividendItem.companyName
                        }
                        else -> {
                            llItemViewCalendarParent3.visibility = View.VISIBLE
                        }
                    }
                }
            }

            return mView!!
        }
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private fun getDividedDataFormat(data: Date): String {
        return sdf.format(data)
    }
}