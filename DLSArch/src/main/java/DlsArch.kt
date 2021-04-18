import android.app.Application
import com.dls.wow.dlsarch.operator.DataResponseOperator
import com.dls.wow.dlsarch.operator.GlobalOperator
import com.dls.wow.dlsarch.response.DataResponse

object DlsArch {

    var globalOperator: GlobalOperator? = null

    fun initial(application: Application) {
        globalOperator = object :DataResponseOperator<Any>(){
            override fun onSuccess(response: DataResponse.Success<Any>) {
            }

            override fun onServerError(response: DataResponse.ServerError<Any>) {
                TODO("Not yet implemented")
            }

            override fun onException(response: DataResponse.Error<Any>) {
                TODO("Not yet implemented")
            }

        }
    }

}