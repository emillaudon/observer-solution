package com.example.observerapp
import android.database.Observable

class ObservableQuoter : Observable<Any>() {

}

class UserDataRepository private constructor() : Observable<String>() {
    private val mFullName: String? = null
    private val mAge = 0

    companion object {
        private val INSTANCE: UserDataRepository? = null
    }

    init {
       // getNewDataFromRemote()
    }
}