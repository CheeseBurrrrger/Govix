package com.example.govix.navigation

object Screen {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val Saved = "saved"

    const val HospitalList = "hospital_list"
    const val HospitalDetail = "hospital_detail/{hospitalId}"
    const val HospitalRooms = "hospital_rooms/{hospitalId}"
    const val HospitalQueue = "hospital_queue/{hospitalId}"
    const val Emergency = "emergency"

    const val Profile    = "profile"
    const val EditProfile = "edit_profile"

    fun hospitalDetail(hospitalId: Int) = "hospital_detail/$hospitalId"
    fun hospitalRooms(hospitalId: Int) = "hospital_rooms/$hospitalId"
    fun hospitalQueue(hospitalId: Int) = "hospital_queue/$hospitalId"
}
