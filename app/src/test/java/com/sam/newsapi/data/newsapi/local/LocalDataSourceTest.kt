package com.sam.newsapi.data.newsapi.local

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sam.newsapi.TestDataFactory
import com.sam.newsapi.data.newsapi.datasource.local.LocalDataSource
import com.sam.newsapi.data.newsapi.datasource.local.db.NewsDatabase
import com.sam.newsapi.data.newsapi.datasource.local.db.dao.NewsDao
import com.sam.newsapi.data.newsapi.datasource.local.db.entity.NewsEntity
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class LocalDataSourceTest {
    
    private lateinit var mockNewsDatabase: NewsDatabase
    private lateinit var mockNewsDao: NewsDao
    private lateinit var testLocalDataSource: LocalDataSource

    private val testNewsModel = TestDataFactory.makeNewsModel()
    private val testNewsEntity = NewsEntity(testNewsModel.key, testNewsModel.articles)

    @Before
    fun setUp() {
        mockNewsDatabase = mock()
        mockNewsDao = mock()
        whenever(mockNewsDatabase.newsDao()).thenReturn(mockNewsDao)
        testLocalDataSource = LocalDataSource(mockNewsDatabase)
    }

    @Test
    fun `localDataSource getNewsData completes`() {
        whenever(mockNewsDao.getNewsEntityByNewsId(any())).thenReturn(Single.just(testNewsEntity))
        testLocalDataSource.getNewsData("test")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `verify localDataSource getNewsData calls NewsDatabase newsDao and getNewsEntityByNewsId method`() {
        whenever(mockNewsDao.getNewsEntityByNewsId(any())).thenReturn(Single.just(testNewsEntity))
        testLocalDataSource.getNewsData("test").test()
        verify(mockNewsDatabase).newsDao()
        verify(mockNewsDao).getNewsEntityByNewsId(any())
    }

    @Test
    fun `localDataSource getNewsData returns NewsModel onSuccess`() {
        whenever(mockNewsDao.getNewsEntityByNewsId(any())).thenReturn(Single.just(testNewsEntity))
        val testObserver = testLocalDataSource.getNewsData("ok").test()
        testObserver.assertValue(testNewsModel)
    }

}