package com.sam.newsapi.data.newsapi

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sam.newsapi.TestDataFactory
import com.sam.newsapi.data.newsapi.datasource.local.LocalDataSource
import com.sam.newsapi.data.newsapi.datasource.remote.RemoteDataSource
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NewsRepositoryImplTest {

    private lateinit var mockRemoteDataSource: RemoteDataSource
    private lateinit var mockLocalDataSource: LocalDataSource
    private lateinit var testNewsRepositoryImpl: NewsRepositoryImpl

    private val testNewsModel = TestDataFactory.makeNewsModel()
    private val testRemoteSuccessSingle = Single.just(testNewsModel)
    private val testRemoteErrorSingle = Single.error<NewsModel>(Throwable("remoteSingleError"))
    private val testLocalSuccessSingle = Single.just(testNewsModel)
    private val testLocalErrorSingle = Single.error<NewsModel>(Throwable("localSingleError"))

    @Before
    fun setUp() {
        mockRemoteDataSource = mock()
        mockLocalDataSource = mock()
        testNewsRepositoryImpl = NewsRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)
        whenever(mockLocalDataSource.insertNewsData(any())).then {}
    }

    @Test
    fun `newsRepositoryImpl getNewsData completes`() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteSuccessSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalSuccessSingle)
        testNewsRepositoryImpl.getNewsData("test")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `verify newsRepositoryImpl getNewsData calls both localDataSource and remoteDataSource getNewsData method`() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteSuccessSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalSuccessSingle)
        testNewsRepositoryImpl.getNewsData("test").test()
        verify(mockRemoteDataSource).getNewsData(any())
        verify(mockLocalDataSource).getNewsData(any())
    }

    @Test
    fun `newsRepositoryImpl getNewsData returns NewsModel onSuccess`() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteSuccessSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalSuccessSingle)
        val testObserver = testNewsRepositoryImpl.getNewsData("test").test()
        testObserver.assertValue(testNewsModel)
    }

    @Test
    fun `newsRepositoryImpl getNewsData returns NewsModel onSuccess when localDataSource gives error but remoteDataSource success `() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteSuccessSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalErrorSingle)
        val testObserver = testNewsRepositoryImpl.getNewsData("test").test()
        testObserver.assertValue(testNewsModel)
    }
    @Test
    fun `newsRepositoryImpl getNewsData returns NewsModel onSuccess when remoteDataSource gives error but localDataSource success `() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteErrorSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalSuccessSingle)
        val testObserver = testNewsRepositoryImpl.getNewsData("test").test()
        testObserver.assertValue(testNewsModel)
    }

    @Test
    fun `newsRepositoryImpl getNewsData returns no value in case both dataSource return error`() {
        whenever(mockRemoteDataSource.getNewsData(any())).thenReturn(testRemoteErrorSingle)
        whenever(mockLocalDataSource.getNewsData(any())).thenReturn(testLocalErrorSingle)
        testNewsRepositoryImpl.getNewsData("test").test()
            .assertValueCount(0)
    }
}