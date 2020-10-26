package com.doctorblue.noname.textinput

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatEditText

abstract class TextInputStyle(
    protected val context: Context,
    protected val textInputLayout: TextInputLayout
) : View.OnFocusChangeListener {


    open lateinit var inputFrame: ViewGroup

    init {
        setupInputFrame()
        textInputLayout.addView(inputFrame)
        textInputLayout.hasInputFrame = true
    }

    var editText: AppCompatEditText? = null

    protected var hintText = textInputLayout.hintText

    open var hasFocus: Boolean = false

    @Dimension
    open var hintTextSize: Float = textInputLayout.hintTextSize

    open var hint: String = textInputLayout.hint

    @ColorInt
    open var defaultColor: Int = textInputLayout.defaultColor

    @ColorInt
    open var activeColor: Int = textInputLayout.activeColor


    open fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is AppCompatEditText) {
            onAddEditText()
            // set edit text
            setEditText(child)
            editText!!.onFocusChangeListener = this
            inputFrame.addView(child, index, params)
            onEditTextAdded()
        } else {
            inputFrame.addView(child, index, params)
        }
    }

    /**
     * {@inheritDoc}
     * do anything you want after add EditText to inputFrame
     */
    open fun onEditTextAdded() {

    }
    /**
     * {@inheritDoc}
     * do anything you want before add EditText to inputFrame
     */
    open fun onAddEditText() {

    }


    /**
     * {@inheritDoc}
     * set your inputFrame here: LinearLayout is the default. You can replace your ViewGroup here
     */
    open fun setupInputFrame() {
        inputFrame = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

    }


    @JvmName("setLocalEditText")
    private fun setEditText(editText: AppCompatEditText) {
        // If we already have an EditText, throw an exception
        require(this.editText == null) { "We already have an EditText, can only have one" }

        if (editText !is TextInputEditText) {
            Log.i(
                TextInputLayout.LOG_TAG,
                "EditText added is not a TextInputEditText. Please switch to using that"
                        + " class instead."
            )
        }
        this.editText = editText
        textInputLayout.editText = editText
    }


}