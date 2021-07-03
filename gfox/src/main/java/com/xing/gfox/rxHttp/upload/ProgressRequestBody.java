package com.xing.gfox.rxHttp.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import com.xing.gfox.base.interfaces.ProgressListener;

public class ProgressRequestBody extends RequestBody {

    private final ProgressListener progressListener;
    private final RequestBody mDelegate;
    private BufferedSink mBufferedSink;
    /* 已经发送的字节数 */
    private long writen = 0L;

    public ProgressRequestBody(RequestBody delegate, ProgressListener progressListener) {
        mDelegate = delegate;
        this.progressListener = progressListener;
    }

    public ProgressRequestBody(File file, MediaType mediaType, ProgressListener progressListener) {
        this(create(file, mediaType), progressListener);
    }

    public ProgressRequestBody(File file, MediaType mediaType) {
        this(create(file, mediaType), null);
    }

    @Override
    public long contentLength() throws IOException {
        return mDelegate.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof BufferedSource) {
            // Log Interceptor
            mDelegate.writeTo(sink);
            return;
        }
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(wrapSink(sink));
        }
        mDelegate.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink wrapSink(Sink sink) {
        return new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writen += byteCount;
                int progress = (int) ((writen / (float) contentLength()) * 100);
                if (progressListener != null) progressListener.onProgress(progress);
            }
        };
    }
}