package com.dls.wow.dlsarch.executor

object DLSTaskExecutor : AbstractTaskExecutor() {

    private var mDelegateExecutor: AbstractTaskExecutor = ThreadPoolTaskExecutor()

    override fun executeOnIOThread(runnable: Runnable) {
        mDelegateExecutor.executeOnIOThread(runnable)
    }

    override fun postOnMainThread(runnable: Runnable) {
        mDelegateExecutor.postOnMainThread(runnable)
    }

}