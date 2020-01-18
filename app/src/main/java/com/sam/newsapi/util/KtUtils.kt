package com.sam.newsapi.util

fun Boolean.doOn(trueBlock: () -> Unit, falseBlock: () -> Unit) {
    if (this) trueBlock() else falseBlock()
}

fun <T1 : Any> safeWith(p1: T1?, block: (T1) -> Unit) {
    if (p1 != null) block(p1)
}

fun <T1 : Any, T2 : Any> safeDistinctWith(p1: T1?, p2: T2?, block: (T1) -> Unit) {
    if (p1 != null && p1 != p2) block(p1)
}