package fr.openium.template.rest

/**
 * Created by t.coulange on 27/01/2017.
 */
//open class MockApi : Api {
//    lateinit var delegate: BehaviorDelegate<Api>
//
//    fun BehaviorDelegate<Api>.returningFail(code: Int): Api {
//        val body = ResponseBody.create(MediaType.parse("txt"), "Error")
//        val response: Response<Any> = Response.error(code, body)
//        return returning(Calls.response(response))
//    }
//}