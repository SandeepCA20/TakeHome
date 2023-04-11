package com.android.takehome

import com.android.takehome.retrofit.GitApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var gitApi: GitApi

    @Before
    fun setUp(){
        mockWebServer= MockWebServer()
        gitApi= Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build().create(GitApi::class.java)
    }

    @Test
    fun testGetUser_returnRepos() = runTest{
        val mockResponse= MockResponse()
        val content=Helper.readResourceFile("/repos.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response=gitApi.getRepos("Octo")
        mockWebServer.takeRequest()

        Assert.assertEquals(false,response.body()!!.isEmpty())
    }

    @Test
    fun testGetUser_returnRepos_Empty() = runTest{
        val mockResponse= MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)

        val response=gitApi.getRepos("Octo")
        mockWebServer.takeRequest()

        Assert.assertEquals(true,response.body()!!.isEmpty())
    }
    @Test
    fun testGetUser_returnError() = runTest{
        val mockResponse= MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val response=gitApi.getRepos("Octo")
        mockWebServer.takeRequest()

        Assert.assertEquals(false,response.isSuccessful)
        Assert.assertEquals(404,response.code())
    }
    @Test
    fun testGetUser_returnUsers() = runTest{
        val mockResponse= MockResponse()
        val content=Helper.readResourceFile("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response=gitApi.getUser("Octo")
        mockWebServer.takeRequest()

        Assert.assertEquals("Octo",response.body()!!.name)
        Assert.assertEquals("https://avatars.githubusercontent.com/u/315353?v=4",response.body()!!.avatar_url)
    }

    @Test
    fun testGetUserEmptyBody_returnFailed() = runTest{
        val mockResponse= MockResponse()
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse)

        val response=gitApi.getUser("Octo")
        mockWebServer.takeRequest()

        Assert.assertEquals("",response.body())
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

}