/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */

package com.mpush.java.api.push;

import com.mpush.java.api.Constants;

/**
 * Created by ohun on 16/10/13.
 *
 * @author ohun@live.cn (夜色)
 */
public final class PushContext {
    public byte[] content;
    public AckModel ackModel = AckModel.NO_ACK;
    public PushCallback callback;
    public int timeout = 1000;

    public PushContext(byte[] content) {
        this.content = content;
    }

    public static PushContext build(byte[] content) {
        return new PushContext(content);
    }

    public static PushContext build(String content) {
        return new PushContext(content.getBytes(Constants.UTF_8));
    }

    public byte[] getContent() {
        return content;
    }

    public PushContext setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public AckModel getAckModel() {
        return ackModel;
    }

    public PushContext setAckModel(AckModel ackModel) {
        this.ackModel = ackModel;
        return this;
    }

    public PushCallback getCallback() {
        return callback;
    }

    public PushContext setCallback(PushCallback callback) {
        this.callback = callback;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public PushContext setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
