package com.qiqiao.tools.common;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Simon
 * 暂时只做阿里云的短信服务,不做其他的就写死了
 */
public class MessageTools {

    public static void sendMessage(String phone,String code,String id,String secret) throws ExecutionException, InterruptedException {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(id)
                .accessKeySecret(secret)
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("企桥医健")
                .templateCode("SMS_464035616")
                .phoneNumbers(phone)
                .templateParam("{\"code\":\""+code+"\"}")
                .build();
        client.sendSms(sendSmsRequest);
        client.close();
    }
}
