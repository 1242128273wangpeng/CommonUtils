package com.shouji2345.net

data class PostRequest(var postController: String, var postMethod: String,
                       var commonParamJson: CommonParamJson,
                       var extraParamJson: ExtraParamJson?)

class CommonParamJson(var appVersion: String, var appCode: Int,
                      var channel: String, var platform: String,
                      var imei: String,
                      var mac: String,
                      var deviceId: String)

data class ExtraParamJson(var scope: String)