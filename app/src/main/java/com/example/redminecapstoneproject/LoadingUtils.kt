package com.example.redminecapstoneproject

import android.content.Context

open class LoadingUtils {
    companion object {
        private var jarvisLoader: LoaderJarvis? = null
        private var jarvisLoading: LoadingJarvis? = null
        private var jarvisDialog: ReqSentJarvis? = null
        fun showDialog(
            context: Context?,
            isCancelable: Boolean
        ) {
            hideDialog()
            if (context != null) {
                try {
                    jarvisLoader = LoaderJarvis(context)
                    jarvisLoader?.let { jarvisLoader->
                        jarvisLoader.setCanceledOnTouchOutside(true)
                        jarvisLoader.setCancelable(isCancelable)
                        jarvisLoader.show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog() {
            if (jarvisLoader!=null && jarvisLoader?.isShowing!!) {
                jarvisLoader = try {
                    jarvisLoader?.dismiss()
                    null
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun showLoading(
            context: Context?,
            isCancelable: Boolean
        ) {
            hideLoading()
            if (context != null) {
                try {
                    jarvisLoading = LoadingJarvis(context)
                    jarvisLoading?.let { jarvisLoader->
                        jarvisLoader.setCanceledOnTouchOutside(true)
                        jarvisLoader.setCancelable(isCancelable)
                        jarvisLoader.show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideLoading() {
            if (jarvisLoading!=null && jarvisLoading?.isShowing!!) {
                jarvisLoading = try {
                    jarvisLoading?.dismiss()
                    null
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun showDialog2(
            context: Context?,
            isCancelable: Boolean
        ) {
            hideDialog2()
            if (context != null) {
                try {
                    jarvisDialog = ReqSentJarvis(context)
                    jarvisDialog?.let { jarvisLoader->
                        jarvisLoader.setCanceledOnTouchOutside(true)
                        jarvisLoader.setCancelable(isCancelable)
                        jarvisLoader.show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog2() {
            if (jarvisDialog!=null && jarvisDialog?.isShowing!!) {
                jarvisDialog = try {
                    jarvisDialog?.dismiss()
                    null
                } catch (e: Exception) {
                    null
                }
            }
        }

    }
}