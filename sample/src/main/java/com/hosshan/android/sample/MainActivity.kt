package com.hosshan.android.sample

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.hosaka.rxanimation.animation.AnimationEvent
import com.hosaka.rxanimation.animator.AnimatorEvent
import com.hosaka.rxanimation_kotlin.animation.bindView
import com.hosaka.rxanimation_kotlin.animation.events
import rx.Observable

class MainActivity : AppCompatActivity() {

    val loginButton: Button by bindView(R.id.main_button_login)
    val formLayout: RelativeLayout by bindView(R.id.main_layout_form)
    val mailEditText: EditText by bindView(R.id.main_edittext_mail)
    val passEditText: EditText by bindView(R.id.main_edittext_pass)
    val signupTextView: TextView by bindView(R.id.main_textview_signup)
    val forgotTextView: TextView by bindView(R.id.main_textview_forgot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initialize invisible
        formLayout.visibility = View.INVISIBLE
        loginButton.setOnClickListener { startAnimations() }
    }

    private fun startAnimations() {
        mailEditText.visibility = View.INVISIBLE
        passEditText.visibility = View.INVISIBLE
        signupTextView.visibility = View.INVISIBLE
        forgotTextView.visibility = View.INVISIBLE

        val alphaAnimation: AlphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 3000
        val alphaAnimationObserver = alphaAnimation.bindView(formLayout).map {
            Log.d(MainActivity::class.java.simpleName, it.toString())
            when (it.kind()) {
                AnimationEvent.Kind.START -> {
                    formLayout.visibility = View.VISIBLE
                    false
                }
                AnimationEvent.Kind.REPEAT -> false

                AnimationEvent.Kind.END -> true
            }
        }

        val translationAnimator: ObjectAnimator = ObjectAnimator.ofFloat(formLayout, "translationY", -1000f, 0f);
        translationAnimator.setDuration(3000);
        val translateAnimatorObserver = translationAnimator.events().map {
            Log.d(MainActivity::class.java.simpleName, it.toString())
            when (it.kind()) {
                AnimatorEvent.Kind.START -> false
                AnimatorEvent.Kind.REPEAT -> false
                AnimatorEvent.Kind.CANCEL -> false
                AnimatorEvent.Kind.END -> true
            }
        }

        Observable.combineLatest(translateAnimatorObserver, alphaAnimationObserver, { t1, t2 ->
            t1.and(t2)
        }).subscribe { validate ->
            // post event
            Log.d(MainActivity::class.java.simpleName, "End All Animation")
            signupTextView.visibility = View.VISIBLE
            forgotTextView.visibility = View.VISIBLE
        }
    }

}
