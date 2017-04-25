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
 */

package com.mpush.java.api;


/**
 * 功能说明： 所有服务端下行的消息都直接转交给ClientListener来处理
 *
 * @author: ohun@live.cn (夜色)
 * @change: Yiheng Yan
 * @version: 0.5
 * @date: 16-01-23
 * @Copyright (c) 2016. Inc. All rights reserved.
 */
public interface ClientListener {

    /**
     * 建立连接
     * @param client
     */
    void onConnected(Client client);

    /**
     * 断开连接
     * @param client
     */
    void onDisConnected(Client client);

    /**
     * 时功能由SDK收到onHandshakeOk事件时去触发
     * @param client
     * @param heartbeat
     */
    void onHandshakeOk(Client client, int heartbeat);

    /**
     * 接收到服务器推送过来的消息
     * @param client
     * @param content
     * @param messageId
     */
    void onReceivePush(Client client, byte[] content, int messageId);

    /**
     * 将用户踢下线
     * @param deviceId
     * @param userId
     */
    void onKickUser(String deviceId, String userId);
}
