package com.lodz.android.agiledevkt.modules.mvc

import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.utils.RxObservableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * 数据
 * Created by zhouL on 2018/11/19.
 */
object ApiModule {

    fun requestResult(isSuccess: Boolean): Observable<String> {
        return Observable.create(object : RxObservableOnSubscribe<String>(isSuccess) {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                val success = getArgs()[0] as Boolean
                if (emitter.isDisposed) {
                    return
                }
                try {
                    UiHandler.postDelayed(1000){
                        if (emitter.isDisposed) {
                            return@postDelayed
                        }
                        if (!success) {
                            emitter.onError(DataException("request fail"))
                            return@postDelayed
                        }
                        emitter.onNext("result is ${System.currentTimeMillis()}")
                        emitter.onComplete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(e)
                }
            }

        })
    }
}