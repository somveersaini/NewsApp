package com.sam.newsapi.data.newsapi.remote

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sam.newsapi.TestDataFactory
import com.sam.newsapi.data.newsapi.datasource.remote.NewsApiService
import com.sam.newsapi.data.newsapi.datasource.remote.RemoteDataSource
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RemoteDataSourceTest {

    private lateinit var mockNewsApiService: NewsApiService
    private lateinit var testRemoteDataSource: RemoteDataSource

    private val testNewsModel = TestDataFactory.makeNewsModel()

    @Before
    fun setUp() {
        mockNewsApiService = mock()
        testRemoteDataSource = RemoteDataSource(mockNewsApiService)
    }

    @Test
    fun `remoteDataSource getNewsData completes`() {
        whenever(mockNewsApiService.getNews(any(), any(), any())).thenReturn(Single.just(testNewsModel))
        testRemoteDataSource.getNewsData("test")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `verify remoteDataSource getNewsData calls newsApiServices getNews method`() {
        whenever(mockNewsApiService.getNews(any(), any(), any())).thenReturn(Single.just(testNewsModel))
        testRemoteDataSource.getNewsData("test").test()
        verify(mockNewsApiService).getNews(any(), any(), any())
    }

    @Test
    fun `remoteDataSource getNewsData returns NewsModel onSuccess`() {
        whenever(mockNewsApiService.getNews(any(), any(), any())).thenReturn(Single.just(testNewsModel))
        val testObserver = testRemoteDataSource.getNewsData("test").test()
        testObserver.assertValue(testNewsModel)
    }

    @Test
    fun `remoteDataSource getNewsData returns error in case of newsApiService return error`() {
        whenever(mockNewsApiService.getNews(any(), any(), any())).thenReturn(Single.error(Throwable("singleError")))
        testRemoteDataSource.getNewsData("test").test()
            .assertValueCount(0)
            .assertError { it.localizedMessage == "singleError" }
    }
}
