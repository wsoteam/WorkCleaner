package cleaner.booster.wso.app.util.callback


import java.util.ArrayList

import cleaner.booster.wso.app.util.model.JunkInfo

interface IScanCallback {
    fun onBegin()

    fun onProgress(info: JunkInfo)

    fun onFinish(children: ArrayList<JunkInfo>)
}
