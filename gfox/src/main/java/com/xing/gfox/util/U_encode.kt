package com.xing.gfox.util

import android.os.Build
import android.text.Html
import android.util.Base64
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

class U_encode {
    companion object {
        fun hasUrlEncoded(str: String): Boolean {
            var encode = false
            val regex = "^[A-Fa-f0-9]+$"

            for (i in str.indices) {
                val c = str[i]
                if (c == '%' && i + 2 < str.length) {
                    // 判断是否符合urlEncode规范
                    val c1 = str[i + 1]
                    val c2 = str[i + 2]
                    if (c1.toString().matches(Regex(regex)) && c2.toString()
                            .matches(Regex(regex))
                    ) {
                        encode = true
                        break
                    } else {
                        break
                    }
                }
            }
            return encode
        }

        /**
         * URL编码解码
         */
        @JvmOverloads
        fun urlEncode(input: String, charsetName: String? = "UTF-8"): String {
            return if (input.isEmpty()) "" else try {
                URLEncoder.encode(input, charsetName)
            } catch (e: UnsupportedEncodingException) {
                throw AssertionError(e)
            }
        }

        @JvmOverloads
        fun urlDecode(input: String, charsetName: String? = "UTF-8"): String {
            return if (input.isEmpty()) "" else try {
                val safeInput = input.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                    .replace("\\+".toRegex(), "%2B")
                URLDecoder.decode(safeInput, charsetName)
            } catch (e: UnsupportedEncodingException) {
                throw AssertionError(e)
            }
        }

        /**
         * base64编码解码
         */
        fun base64Encode(input: ByteArray?): ByteArray {
            return if (input == null || input.isEmpty()) ByteArray(0) else Base64.encode(
                input,
                Base64.NO_WRAP
            )
        }

        fun base64Encode2String(input: ByteArray?): String {
            return if (input == null || input.isEmpty()) "" else Base64.encodeToString(
                input,
                Base64.NO_WRAP
            )
        }

        fun base64Decode(input: String?): ByteArray {
            return if (input == null || input.isEmpty()) ByteArray(0) else Base64.decode(
                input,
                Base64.NO_WRAP
            )
        }

        fun base64Decode(input: ByteArray?): ByteArray {
            return if (input == null || input.isEmpty()) ByteArray(0) else Base64.decode(
                input,
                Base64.NO_WRAP
            )
        }

        /**
         * html文本编码解码
         */
        fun htmlEncode(input: CharSequence?): String {
            if (input == null || input.isEmpty()) return ""
            val sb = StringBuilder()
            var c: Char
            var i = 0
            val len = input.length
            while (i < len) {
                c = input[i]
                when (c) {
                    '<' -> sb.append("&lt;") //$NON-NLS-1$
                    '>' -> sb.append("&gt;") //$NON-NLS-1$
                    '&' -> sb.append("&amp;") //$NON-NLS-1$
                    '\'' ->                     //http://www.w3.org/TR/xhtml1
                        // The named character reference &apos; (the apostrophe, U+0027) was
                        // introduced in XML 1.0 but does not appear in HTML. Authors should
                        // therefore use &#39; instead of &apos; to work as expected in HTML 4
                        // user agents.
                        sb.append("&#39;") //$NON-NLS-1$
                    '"' -> sb.append("&quot;") //$NON-NLS-1$
                    else -> sb.append(c)
                }
                i++
            }
            return sb.toString()
        }

        fun htmlDecode(input: String?): CharSequence {
            if (input == null || input.isEmpty()) return ""
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(input)
            }
        }

        /**
         * binary编码解码
         */
        fun binaryEncode(input: String?): String {
            if (input == null || input.isEmpty()) return ""
            val sb = StringBuilder()
            for (i in input.toCharArray()) {
                sb.append(Integer.toBinaryString(i.toInt())).append(" ")
            }
            return sb.deleteCharAt(sb.length - 1).toString()
        }

        fun binaryDecode(input: String?): String {
            if (input == null || input.isEmpty()) return ""
            val splits = input.split(" ".toRegex()).toTypedArray()
            val sb = StringBuilder()
            for (split in splits) {
                sb.append(split.toInt(2).toChar())
            }
            return sb.toString()
        }
    }
}