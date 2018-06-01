package io.pravikant.rtledittext

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import java.math.BigDecimal
import java.util.*

class RightToLeftDecimalEditText : EditText {

    interface RightToLeftDecimalEditTextListener {
        fun onKeyboardEnterClicked()
    }

    companion object {
        const val DEFAULT_DECIMAL_POINTS = 3
    }

    private var decimalPoints = DEFAULT_DECIMAL_POINTS

    private var nextFocusableView: View? = null
    private var listener: RightToLeftDecimalEditTextListener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        setDecimalValue(BigDecimal.ZERO)
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RightToLeftDecimalEditText)
            decimalPoints = typedArray.getInt(R.styleable.RightToLeftDecimalEditText_rtl_decimal_points, DEFAULT_DECIMAL_POINTS)
            val value = typedArray.getFloat(R.styleable.RightToLeftDecimalEditText_rtl_decimal_value, 0f)
            setDecimalValue(BigDecimal.valueOf(value.toDouble()))
            typedArray.recycle()
        }
        initData()
    }

    private fun initData() {
        setSelectAllOnFocus(true)

        inputType = InputType.TYPE_CLASS_NUMBER

        setOnKeyListener({ _, i, keyEvent -> keyEvent.action == KeyEvent.ACTION_UP && onSoftKeyDown(i) })

        setOnFocusChangeListener { _, b ->
            if (b) {
                setSelection(0, text.toString().trim().length)
            }
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().contains(".") && charSequence.subSequence(charSequence.toString().indexOf(".") + 1, charSequence.length).length < decimalPoints
                        || charSequence.toString().contains(",") && charSequence.subSequence(charSequence.toString().indexOf(",") + 1, charSequence.length).length < decimalPoints) {
                    handleDel()
                } else if (charSequence.toString().isEmpty()) {
                    setText(getDefaultValue())
                    moveCursorToEnd()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun onSoftKeyDown(keyCode: Int): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_0 -> handleKey(0)
            KeyEvent.KEYCODE_1 -> handleKey(1)
            KeyEvent.KEYCODE_2 -> handleKey(2)
            KeyEvent.KEYCODE_3 -> handleKey(3)
            KeyEvent.KEYCODE_4 -> handleKey(4)
            KeyEvent.KEYCODE_5 -> handleKey(5)
            KeyEvent.KEYCODE_6 -> handleKey(6)
            KeyEvent.KEYCODE_7 -> handleKey(7)
            KeyEvent.KEYCODE_8 -> handleKey(8)
            KeyEvent.KEYCODE_9 -> handleKey(9)
            KeyEvent.KEYCODE_ENTER -> handleEnter()
        }
        return true
    }

    private fun handleKey(keyValue: Int) {
        // Completely selected
        if (selectionStart == 0 && selectionEnd == text.toString().length) {
            setText(getDefaultValue())
        }
        // Cursor is not in the end
        if (selectionStart == selectionEnd && selectionStart != text.toString().length) {
            moveCursorToEnd()
            return
        }
        // Otherwise
        var text = text.toString().trim()
        var value = BigDecimal.ZERO
        // Not the first number after selection
        if (text.length != 1) {
            text = text.substring(0, text.length - 1)
            value = BigDecimal(text)
        }

        val newValue = value.multiply(BigDecimal.valueOf(getMultiplyFactor())).add(BigDecimal.valueOf(keyValue.toLong()))

        setText(String.format(Locale.getDefault(), getStringFormat(), newValue.divide(BigDecimal.valueOf(getDivideFactor()))))
        moveCursorToEnd()
    }

    private fun handleEnter() {
        listener?.onKeyboardEnterClicked()
        nextFocusableView?.requestFocus()
    }

    private fun handleDel() {
        val text = text.toString().trim()
        val value = BigDecimal(text)
        setText(String.format(Locale.getDefault(), getStringFormat(), value.divide(BigDecimal.valueOf(10))))
        moveCursorToEnd()
    }

    private fun moveCursorToEnd() {
        setSelection(text.toString().length)
    }

    private fun getDefaultValue(): String {
        var defaultValue = "0."

        for (i in 1..decimalPoints) {
            defaultValue += "0"
        }

        return defaultValue
    }

    private fun getMultiplyFactor() = Math.pow(10.0, (decimalPoints + 1).toDouble()).toLong()

    private fun getDivideFactor() = Math.pow(10.0, decimalPoints.toDouble()).toLong()

    private fun getStringFormat() = "%." + decimalPoints + "f"

    /**
     * To set value, use this method instead of setText()
     *
     * @param value BigDecimal value
     * */
    fun setDecimalValue(value: BigDecimal) {
        setText(String.format(Locale.getDefault(), getStringFormat(), value));
    }

    /**
     * This will define the number of digits after the decimal points.
     * Eg. 3 = 7.500
     *
     * @param decimalPoints Int value
     * */
    fun setDecimalPoints(decimalPoints: Int) {
        if (decimalPoints < 1) {
            throw NumberFormatException("decimalPoints can't be less that 1")
        }

        this@RightToLeftDecimalEditText.decimalPoints = decimalPoints
        initData()
    }

    /**
     * This will return the value in EditText
     *
     * @return BigDecimal
     * */
    fun getDecimalPoints(): BigDecimal {
        val value = text.toString().trim()
        if (value.isNotEmpty()) {
            return BigDecimal(value)
        }

        return BigDecimal.ZERO
    }

    /**
     * nextFocusableView will be focused once enter key is clicked in the soft keyboard
     *
     * @param nextFocusableView An EditText
     * */
    fun setNextFocusableView(nextFocusableView: View) {
        this.nextFocusableView = nextFocusableView
    }

    /**
     * Set listener to know when keyboard enter key is clicked.
     *
     * @param listener A RightToLeftDecimalEditTextListener.
     */
    fun setListener(listener: RightToLeftDecimalEditTextListener) {
        this.listener = listener
    }
}